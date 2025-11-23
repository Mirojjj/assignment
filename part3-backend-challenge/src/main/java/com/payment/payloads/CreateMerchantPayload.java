package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Introspected
@Serdeable
@RecordBuilder
public record CreateMerchantPayload(
        @NotNull String merchantName,
        @NotNull String merchantStatus,
        @NotNull String contactInfo,
        @NotNull String merchantCategory,
        @NotNull String merchantLocation,
        @NotNull String paymentMethod,
        @NotNull String merchantWebsite,
        @NotNull String merchantType,
        @NotNull List<String> merchantTags
) implements UseCaseRequest {
}
