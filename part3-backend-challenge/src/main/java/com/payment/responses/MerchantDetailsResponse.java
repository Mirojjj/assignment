package com.payment.responses;

import com.payment.usecases.UseCaseResponse;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record MerchantDetailsResponse(
        Long txnId,
        BigDecimal amount,
        String currency,
        String status,
        String timestamp,
        String cardType,
        String cardLast4,
        String acquirer,
        String issuer,
        String merchantId
) implements UseCaseResponse {
}
