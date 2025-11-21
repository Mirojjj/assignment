package com.payment.serviceImpl;

import com.payment.entity.TransactionMaster;
import com.payment.payloads.TransactionRequestPayload;
import com.payment.repository.TransactionRepository;
import com.payment.responses.TransactionResponse;
import com.payment.responses.TransactionResponseBuilder;
import com.payment.services.FetchMerchantsService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

public class fetchMerchantsServiceImpl implements FetchMerchantsService {
    private final Logger logger = Logger.getLogger(fetchMerchantsServiceImpl.class.getName());
    private final TransactionRepository transactionRepository;

    @Inject
    public fetchMerchantsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse fetch(TransactionRequestPayload request) {
        logger.info("merchant id" + request.merchantId());

        try {
            int page = request.page().orElse(0);
            int size = request.size().orElse(10);
            int offset = page * size;

            List<TransactionMaster> transactions = this.transactionRepository.findTransactions(
                    request.merchantId(),
                    request.startDate().orElse(null),
                    request.endDate().orElse(null),
                    request.status().orElse(null),
                    size,
                    offset
            );

            int totalCount = this.transactionRepository.countTransactions(
                    request.merchantId(),
                    request.startDate().orElse(null),
                    request.endDate().orElse(null),
                    request.status().orElse(null)
            );

            TransactionResponse transactionResponse = toTransactionResponse(request.merchantId(), page, size, transactions);

            logger.info("transactionslist"+transactions);


            return transactionResponse;

        } catch (IllegalArgumentException e) {
            logger.warning("Invalid argument: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
            throw new RuntimeException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }

    private TransactionResponse toTransactionResponse(String merchantId, int page, int size, List<TransactionMaster> transactions) {
        return TransactionResponseBuilder.builder()
                .message("Success")
                .page(page)
                .size(size)
                .totalTransactions(transactions.size())
                .transactions(transactions)
                .merchantId(merchantId)
                .build();
    }
}
