package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record DeleteMerchantPayload(
        String merchantId
) implements UseCaseRequest {
}
