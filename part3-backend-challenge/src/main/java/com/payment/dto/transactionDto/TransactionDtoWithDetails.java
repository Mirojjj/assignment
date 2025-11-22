package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@RecordBuilder
public record TransactionDtoWithDetails(
        Long txnId,
        BigDecimal amount,
        String currency,
        String status,
        String timestamp,
        String cardType,
        String cardLast4,
        String acquirer,
        String issuer,
        List<TransactionDetailDto> details
) {}