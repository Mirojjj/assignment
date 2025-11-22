package com.payment.dto.transactionDto;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RecordBuilder
public record CreateTransactionRequestPayloadWithMerchantId(
        String merchantId,
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
