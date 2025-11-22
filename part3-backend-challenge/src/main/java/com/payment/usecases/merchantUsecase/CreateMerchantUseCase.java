package com.payment.usecases.merchantUsecase;

import com.payment.payloads.CreateMerchantPayload;
import com.payment.responses.CreateMerchantResponse;
import com.payment.services.MerchantService;
import com.payment.support.Result;
import com.payment.usecases.UseCase;
import com.payment.usecases.UseCaseContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CreateMerchantUseCase implements UseCase<CreateMerchantPayload, CreateMerchantResponse> {

    private final MerchantService merchantService;

    @Inject
    public CreateMerchantUseCase(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Override
    public Result<CreateMerchantResponse> execute(UseCaseContext context, CreateMerchantPayload request) {
        var result = this.merchantService.createNewMerchant(request);
        return Result.ok(result);
    }
}
