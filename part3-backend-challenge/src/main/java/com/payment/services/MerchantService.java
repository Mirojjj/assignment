package com.payment.services;

import com.payment.payloads.CreateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayloadWithMerchantId;
import com.payment.responses.*;
import jakarta.inject.Singleton;

@Singleton
public interface MerchantService {

    MerchantListResponse getAllMerchants();

    MerchantDetailsResponse getMerchantDetails(String merchantId);

    CreateMerchantResponse createNewMerchant(CreateMerchantPayload createMerchantPayload);

    UpdateMerchantResponse updateMerchant(UpdateMerchantPayloadWithMerchantId updateMerchantPayload);

    DeleteMerchantResponse deleteMerchant(String merchantId);
}
