package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@Serdeable
@RecordBuilder
public record TransactionsDto(
        Long txnId,
        BigDecimal amount,
        String currency,
        String status,
        String timestamp,
        String cardType,
        String cardLast4,
        String acquirer,
        String issuer
) {
}
