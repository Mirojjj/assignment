package com.payment.dto.transactionDto;

import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@Serdeable
@RecordBuilder
public record Pagination(
        int page,
        int size,
        int totalPages,
        int totalElements
) {
}
