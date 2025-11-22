package com.payment.responses;

import com.payment.usecases.UseCaseResponse;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record UpdateMerchantResponse (
        String merchantId,
        String message
) implements UseCaseResponse {
}
