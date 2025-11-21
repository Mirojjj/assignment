package com.payment.payloads;

import com.payment.usecases.UseCaseRequest;
import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.Builder;

import java.util.Optional;

@RecordBuilder
public record TransactionRequestPayload (
        String merchantId,
        Optional<Integer> page,
        Optional<Integer> size,
        Optional<String> startDate,
        Optional<String> endDate,
        Optional<String> status
) implements UseCaseRequest {
}
