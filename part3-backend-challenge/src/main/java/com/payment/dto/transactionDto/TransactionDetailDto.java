package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@Serdeable
@RecordBuilder
public record TransactionDetailDto(
        Long detailId,
        String type,
        BigDecimal amount,
        String description
) {
}
