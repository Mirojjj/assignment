package com.payment.usecases.merchantUsecase;

import com.payment.payloads.DeleteMerchantPayload;
import com.payment.responses.DeleteMerchantResponse;
import com.payment.services.MerchantService;
import com.payment.support.Result;
import com.payment.usecases.UseCase;
import com.payment.usecases.UseCaseContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DeleteMerchantUseCase implements UseCase<DeleteMerchantPayload, DeleteMerchantResponse> {

    private final MerchantService merchantService;

    @Inject
    public DeleteMerchantUseCase(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Override
    public Result<DeleteMerchantResponse> execute(UseCaseContext context, DeleteMerchantPayload request) {
        var result = this.merchantService.deleteMerchant(request.merchantId());
        return null;
    }
}
