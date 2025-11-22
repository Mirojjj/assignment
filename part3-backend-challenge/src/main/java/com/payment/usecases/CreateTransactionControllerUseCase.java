package com.payment.usecases;

import com.payment.dto.transactionDto.CreateTransactionRequestPayloadWithMerchantId;
import com.payment.responses.CreateTransactionResponse;
import com.payment.services.CreateTransactionService;
import com.payment.support.Result;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTransactionControllerUseCase implements UseCase<CreateTransactionRequestPayloadWithMerchantId, CreateTransactionResponse>{

    private final Logger logger = LoggerFactory.getLogger(CreateTransactionControllerUseCase.class);
    private final CreateTransactionService createTransactionService;

    @Inject
    public CreateTransactionControllerUseCase(CreateTransactionService createTransactionService) {
        this.createTransactionService = createTransactionService;
    }


    @Override
    public Result<CreateTransactionResponse> execute(UseCaseContext context, CreateTransactionRequestPayloadWithMerchantId request) {
        if(request.merchantId() == null || request == null || request.merchantId().isEmpty()) {
            logger.warn("Invalid request: merchantId is required");
            return Result.fail(new Error("\"Invalid request: merchantId is required\""));
        }
        var result = this.createTransactionService.createTransaction(request);
        return Result.ok(result);
    }
}
