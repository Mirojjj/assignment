package com.payment.responses;

import com.payment.entity.TransactionMaster;
import com.payment.usecases.UseCaseResponse;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;
import java.util.Optional;

@Serdeable
@RecordBuilder
public record TransactionResponse(
        String message,
        String merchantId,
        Integer page,
        Integer size,
        Integer totalTransactions,
        List<TransactionMaster> transactions
) implements UseCaseResponse {
}