package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@Serdeable
@RecordBuilder
public record TransactionDetailRow(
        Long detailId,
        Long masterTxnId,
        String type,
        BigDecimal amount,
        String description
) {}