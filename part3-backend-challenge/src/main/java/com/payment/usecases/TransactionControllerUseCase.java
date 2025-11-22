package com.payment.usecases;

import com.payment.payloads.TransactionRequestPayload;
import com.payment.responses.TransactionResponse;
import com.payment.services.TransactionService;
import com.payment.support.Result;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionControllerUseCase implements UseCase<TransactionRequestPayload, TransactionResponse> {

    private final Logger logger = LoggerFactory.getLogger(TransactionControllerUseCase.class);
    private final TransactionService transactionService;

    @Inject
    public TransactionControllerUseCase(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Result<TransactionResponse> execute(UseCaseContext context, TransactionRequestPayload request) {
        try {
            if (request == null || request.merchantId() == null || request.merchantId().isEmpty()) {
                logger.warn("Invalid request: merchantId is required");
                return Result.fail(new Error("\"Invalid request: merchantId is required\""));
            }

            var result = this.transactionService.fetchTransactionsList(request);
            return Result.ok(result);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid argument: " + e.getMessage());
            return Result.fail(new Error("Invalid argument: " + e.getMessage()));
        } catch (Exception e) {
            logger.warn("Error fetching transactions: " + e.getMessage());
            return Result.fail(new Error("Failed to fetch transactions: " + e.getMessage()));
        }
    }
}
