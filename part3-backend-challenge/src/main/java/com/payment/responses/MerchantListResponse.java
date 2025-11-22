package com.payment.responses;

import com.payment.dto.merchantDto.MerchantDto;
import com.payment.usecases.UseCaseResponse;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@Serdeable
@RecordBuilder
public record MerchantListResponse(
        List<MerchantDto> merchants
) implements UseCaseResponse {
}
