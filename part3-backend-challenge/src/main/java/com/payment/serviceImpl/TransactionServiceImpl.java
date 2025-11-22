package com.payment.serviceImpl;

import com.payment.dto.transactionDto.*;
import com.payment.helpers.DateUtils;
import com.payment.payloads.TransactionRequestPayload;
import com.payment.repository.TransactionRepository;
import com.payment.responses.TransactionResponse;
import com.payment.responses.TransactionResponseBuilder;
import com.payment.services.TransactionService;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    private final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());
    private final TransactionRepository transactionRepository;

    @Inject
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse fetchTransactionsList(TransactionRequestPayload request) {
        logger.info("merchant id: " + request.merchantId());

        try {
            if (request.merchantId() == null || request.merchantId().isEmpty()) {
                logger.warning("Invalid request: merchantId is required");
                throw new IllegalArgumentException("merchantId is required");
            }

            String merchantId = request.merchantId();
            int page = request.page().orElse(0);
            int size = request.size().orElse(20);
            int offset = page * size;

            if (page < 0 || size <= 0) {
                logger.warning("Invalid pagination parameters: page=" + page + ", size=" + size);
                throw new IllegalArgumentException("page must be >= 0 and size must be > 0");
            }

            String start = DateUtils.toIsoUtcStart(request.startDate().orElse(null));
            String end = DateUtils.toIsoUtcEnd(request.endDate().orElse(null));

            Instant startInstant = Instant.parse(start);
            Instant endInstant = Instant.parse(end);

            if (startInstant.isAfter(endInstant)) {
                logger.warning("Start date " + start + " cannot be after end date " + end);
                throw new IllegalArgumentException("startDate cannot be after endDate");
            }


            List<TransactionsDto> transactionsList = this.transactionRepository.findTransactions(
                    merchantId,
                    start,
                    end,
                    request.status().orElse(null),
                    size,
                    offset
            );

            logger.info("transactionsList"+transactionsList);

            // Get total count for all matching transactions
            long longTotalCountValue = this.transactionRepository.countTransactions(
                    merchantId,
                    start,
                    end
            );

            int totalCount = (int) longTotalCountValue;

            logger.info("Found " + transactionsList.size() + " transactions on page " + page + ", total: " + totalCount);

            Map<Long, List<TransactionDetailDto>> detailsGrouped = fetchTransactionDetails(transactionsList);

            List<TransactionDtoWithDetails> transactions = buildTransactionList(transactionsList, detailsGrouped);

            TransactionSummaryAggregation summaryAgg = transactionRepository.fetchSummaryAggregation(
                    merchantId, start, end
            );

            StatusBreakdown breakdown = transactionRepository.fetchStatusBreakdown(
                    merchantId, start, end
            );

            TransactionSummary summary = new TransactionSummary(
                    summaryAgg.totalTransactions(),
                    summaryAgg.totalAmount(),
                    summaryAgg.currency(),
                    breakdown
            );

            DateRange dateRange = toDateRange(start, end);

            Pagination paginationInfo = toPaginationInfo(page, size, totalCount);

            return toTransactionResponse(merchantId, dateRange, transactions, paginationInfo, summary);

        } catch (IllegalArgumentException e) {
            logger.warning("Invalid argument: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
            throw new RuntimeException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }



    private Map<Long, List<TransactionDetailDto>> fetchTransactionDetails(List<TransactionsDto> transactionsList) {
        if (transactionsList.isEmpty()) {
            return Map.of();
        }

        List<Long> txnIds = transactionsList.stream()
                .map(TransactionsDto::txnId)
                .toList();

        List<TransactionDetailRow> details = transactionRepository.findDetailsForTransactions(txnIds);

        if (details.isEmpty()) {
            return Map.of();
        }

        return details.stream()
                .collect(Collectors.groupingBy(
                        TransactionDetailRow::masterTxnId,
                        Collectors.mapping(
                                row -> new TransactionDetailDto(
                                        row.detailId(),
                                        row.type(),
                                        row.amount(),
                                        row.description()
                                ),
                                Collectors.toList()
                        )
                ));
    }

    private List<TransactionDtoWithDetails> buildTransactionList(List<TransactionsDto> transactionsList, Map<Long, List<TransactionDetailDto>> detailsGrouped) {
        return transactionsList.stream()
                .map(t -> new TransactionDtoWithDetails(
                        t.txnId(),
                        t.amount(),
                        t.currency(),
                        t.status(),
                        DateUtils.parseToIsoUtc(t.timestamp()),
                        t.cardType(),
                        t.cardLast4(),
                        t.acquirer(),
                        t.issuer(),
                        detailsGrouped.getOrDefault(t.txnId(), List.of())
                ))
                .collect(Collectors.toList());
    }

    private Pagination toPaginationInfo(int page, int size, int totalElements) {
        int totalPages = totalElements > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return PaginationBuilder.builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    private DateRange toDateRange(String start, String end) {
        return DateRangeBuilder.builder()
                .start(start)
                .end(end)
                .build();
    }

    private TransactionResponse toTransactionResponse(String merchantId, DateRange dateRange, List<TransactionDtoWithDetails> transactions, Pagination pagination, TransactionSummary transactionSummary) {
        return TransactionResponseBuilder.builder()
                .merchantId(merchantId)
                .dateRange(dateRange)
                .summary(transactionSummary)
                .transactions(transactions)
                .pagination(pagination)
                .build();
    }
}
