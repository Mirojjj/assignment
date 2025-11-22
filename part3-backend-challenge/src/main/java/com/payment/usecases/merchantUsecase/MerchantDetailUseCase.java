package com.payment.usecases.merchantUsecase;

import com.payment.payloads.MerchantDetailRequestPayload;
import com.payment.responses.MerchantDetailsResponse;
import com.payment.services.MerchantService;
import com.payment.support.Result;
import com.payment.usecases.UseCase;
import com.payment.usecases.UseCaseContext;
import com.payment.usecases.UseCaseRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class MerchantDetailUseCase implements UseCase<MerchantDetailRequestPayload,MerchantDetailsResponse> {

    private final MerchantService merchantService;

    @Inject
    public MerchantDetailUseCase(MerchantService merchantService) {
        this.merchantService = merchantService;
    }


    @Override
    public Result<MerchantDetailsResponse> execute(UseCaseContext context,  MerchantDetailRequestPayload request) {
        var result = this.merchantService.getMerchantDetails(request.merchantId());
        return null;
    }
}
