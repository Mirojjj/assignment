package com.payment.responses;

import com.payment.usecases.UseCaseResponse;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@Serdeable
@RecordBuilder
public record DeleteMerchantResponse(
        String merchantId,
        String message
) implements UseCaseResponse {
}
