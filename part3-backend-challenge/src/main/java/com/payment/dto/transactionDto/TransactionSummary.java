package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@Serdeable
@RecordBuilder
public record TransactionSummary(
        int totalTransactions,
        BigDecimal totalAmount,
        String currency,
        StatusBreakdown byStatus
) {
}
