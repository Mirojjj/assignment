package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record MerchantDetailRequestPayload(
        String merchantId
) implements UseCaseRequest {
}
