package com.payment.usecases.merchantUsecase;

import com.payment.responses.MerchantListResponse;
import com.payment.services.MerchantService;
import com.payment.support.Result;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class FetchMerchantListUseCase{

    private final Logger logger = Logger.getLogger(FetchMerchantListUseCase.class.getName());
    private final MerchantService merchantService;

    @Inject
    public FetchMerchantListUseCase(MerchantService merchantService) {
        this.merchantService = merchantService;
    }


    public Result<MerchantListResponse> execute() {
        var result = this.merchantService.getAllMerchants();
        logger.info("resulttt" + result);
        return Result.ok(result);
    }
}
