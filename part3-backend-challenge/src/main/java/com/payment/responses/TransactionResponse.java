package com.payment.responses;

import com.payment.dto.transactionDto.*;
import com.payment.usecases.UseCaseResponse;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@Serdeable
@RecordBuilder
public record TransactionResponse(
        String merchantId,
        DateRange dateRange,
        TransactionSummary summary,
        List<TransactionDtoWithDetails> transactions,
        Pagination pagination
) implements UseCaseResponse {
}