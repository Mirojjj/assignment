package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record UpdateMerchantPayloadWithMerchantId(
        String merchatnId,
        Long gpAcquirerId,
        Long gpIssuerId,
        Integer amount,
        String currency,
        String cardType,
        String cardLast4,
        String authCode,
        String responseCode
)implements UseCaseRequest {
}
