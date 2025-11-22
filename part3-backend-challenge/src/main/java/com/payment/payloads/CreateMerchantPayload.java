package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RecordBuilder
public record CreateMerchantPayload(
        @NotBlank @Positive Long gpAcquirerId,
        @NotBlank @Positive Long gpIssuerId,
        @NotBlank @Positive Integer amount,
        @NotBlank String currency,
        @NotBlank String cardType,
        @NotBlank String cardLast4,
        @NotBlank String authCode,
        @NotBlank String responseCode
) implements UseCaseRequest {
}
