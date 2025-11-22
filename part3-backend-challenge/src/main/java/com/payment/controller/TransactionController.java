package com.payment.controller;

import com.payment.payloads.TransactionRequestPayload;
import com.payment.payloads.TransactionRequestPayloadBuilder;
import com.payment.rest.RestResponse;
import com.payment.usecases.TransactionControllerUseCase;
import com.payment.usecases.UseCaseContext;
import io.micronaut.http.annotation.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Header;
import io.swagger.v3.oas.annotations.Operation;
import com.payment.entity.TransactionMaster;
import com.payment.repository.TransactionRepository;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Transaction Controller - BASIC IMPLEMENTATION PROVIDED
 * 
 * TODO for Junior Developer:
 * 1. Create TransactionService and inject it
 * 2. Implement actual database queries
 * 3. Add proper pagination
 * 4. Add date filtering
 * 5. Add status filtering
 * 6. Return proper TransactionResponse DTOs
 * 7. Add error handling
 */


/**
private final TransactionRepository transactionRepository;

public TransactionController(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
}

// TODO: Create TransactionService to handle business logic
// TODO: Move repository calls to service layer

@Get("/{merchantId}/transactions")
@Operation(
        summary = "Get merchant transactions",
        description = "Returns paginated list of transactions for a merchant. TODO: Implement proper pagination, filtering, and database queries."
)
public HttpResponse<Map<String, Object>> getTransactions(
        @PathVariable String merchantId,
        @QueryValue Optional<Integer> page,
        @QueryValue Optional<Integer> size,
        @QueryValue Optional<String> startDate,
        @QueryValue Optional<String> endDate,
        @QueryValue Optional<String> status
) {
    // TODO: Replace this stub with actual implementation
    var transactions = transactionRepository.findByMerchantId(merchantId);
    return HttpResponse.ok(Map.of(
            "message", "TODO: Implement proper pagination and filtering",
            "merchantId", merchantId,
            "page", page.orElse(0),
            "size", size.orElse(10),
            "totalTransactions", transactions.size(),
            "transactions", transactions,
            "note", "Basic query implemented. Junior developer should add pagination, date filtering, and status filtering."
    ));
}

@Post("/{merchantId}/transactions")
@Operation(
        summary = "Create new transaction",
        description = "Creates a new transaction for a merchant. TODO: Add validation, error handling, and business logic."
)
public HttpResponse<Map<String, Object>> createTransaction(
        @PathVariable String merchantId,
        @Body TransactionMaster transaction
) {
    // TODO: Add validation
    // TODO: Add error handling
    // TODO: Move to service layer
    transaction.setMerchantId(merchantId);
    TransactionMaster saved = transactionRepository.save(transaction);
    return HttpResponse.created(Map.of(
            "message", "Transaction created",
            "transactionId", saved.getTxnId(),
            "note", "TODO: Add proper validation and error handling"
    ));
}

 */

@Controller("/api/v1/merchants")
@Tag(name = "Transactions")
public class TransactionController {

    private final Logger logger = Logger.getLogger(TransactionController.class.getName());
    private TransactionControllerUseCase transactionControllerUseCase;

    @Inject
    public TransactionController(TransactionControllerUseCase transactionControllerUseCase) {
        this.transactionControllerUseCase = transactionControllerUseCase;
    }

    @Get("/{merchantId}/transactions")
    @Operation(
            summary = "Get merchant transactions",
            description = "Returns paginated list of transactions for a merchant. TODO: Implement proper pagination, filtering, and database queries."
    )
    public RestResponse getMerchantTransactions(@PathVariable String merchantId,
                                                             @QueryValue Optional<Integer> page,
                                                             @QueryValue Optional<Integer> size,
                                                             @QueryValue Optional<String> startDate,
                                                             @QueryValue Optional<String> endDate,
                                                             @QueryValue Optional<String> status) {
        logger.info("Get merchant transactions for merchant " + merchantId);
        try {
            TransactionRequestPayload request = TransactionRequestPayloadBuilder.builder()
                    .merchantId(merchantId)
                    .page(page)
                    .size(size)
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(status)
                    .build();

            var result = this.transactionControllerUseCase.execute(UseCaseContext.empty(), request);

            if (result.hasError()) {
                // Could be validation or not found
                return RestResponse.error("404", "Merchant or transactions not found");
            }

            // If no transactions found, return 404 explicitly
            if (result.data() == null) {
                return RestResponse.error("404", "No transactions found for merchant " + merchantId);
            }

            return RestResponse.success(result.data());

        } catch (IllegalArgumentException e) {
            logger.warning("Invalid request: " + e.getMessage());
            return RestResponse.error("400", "Invalid request parameters: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
            return RestResponse.error("500", "Internal server error while fetching transactions");
        }
    }
}
