package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@Serdeable
@Introspected
@RecordBuilder
public record DeleteMerchantPayload(
        String merchantId
) implements UseCaseRequest {
}
