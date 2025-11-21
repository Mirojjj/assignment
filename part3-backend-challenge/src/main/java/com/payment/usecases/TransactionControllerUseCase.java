package com.payment.usecases;

import com.payment.payloads.TransactionRequestPayload;
import com.payment.responses.TransactionResponse;
import com.payment.services.FetchMerchantsService;
import com.payment.support.Result;
import jakarta.inject.Inject;
import java.util.logging.Logger;

public class TransactionControllerUseCase implements UseCase<TransactionRequestPayload, TransactionResponse> {

    private final Logger logger = Logger.getLogger(TransactionControllerUseCase.class.getName());
    private final FetchMerchantsService fetchMerchantsService;

    @Inject
    public TransactionControllerUseCase(FetchMerchantsService fetchMerchantsService) {
        this.fetchMerchantsService = fetchMerchantsService;
    }

    @Override
    public Result<TransactionResponse> execute(UseCaseContext context, TransactionRequestPayload request) {
        try {
            if (request == null || request.merchantId() == null || request.merchantId().isEmpty()) {
                logger.warning("Invalid request: merchantId is required");
                return Result.fail(new Error("\"Invalid request: merchantId is required\""));
            }

            var result = this.fetchMerchantsService.fetch(request);
            return Result.ok(result);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid argument: " + e.getMessage());
            return Result.fail(new Error("Invalid argument: " + e.getMessage()));
        } catch (Exception e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
            return Result.fail(new Error("Failed to fetch transactions: " + e.getMessage()));
        }
    }
}
