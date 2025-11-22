package com.payment.usecases.merchantUsecase;

import com.payment.payloads.UpdateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayloadWithMerchantId;
import com.payment.responses.UpdateMerchantResponse;
import com.payment.services.MerchantService;
import com.payment.support.Result;
import com.payment.usecases.UseCase;
import com.payment.usecases.UseCaseContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class UpdateMerchantUseCase implements UseCase<UpdateMerchantPayloadWithMerchantId, UpdateMerchantResponse> {

    private final MerchantService merchantService;

    @Inject
    public UpdateMerchantUseCase(MerchantService merchantService) {
        this.merchantService = merchantService;
    }


    @Override
    public Result<UpdateMerchantResponse> execute(UseCaseContext context, UpdateMerchantPayloadWithMerchantId request) {
        var result =  this.merchantService.updateMerchant(request);
        return null;
    }
}
