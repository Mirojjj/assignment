package com.payment.responses;

import com.payment.usecases.UseCaseResponse;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@RecordBuilder
public record MerchantDetailsResponse(
        Integer merchantId,
        String merchantName,
        String merchantStatus,
        String contactInfo,
        String merchantCategory,
        String merchantLocation,
        BigDecimal merchantRating,
        Integer numOrders,
        String paymentMethod,
        String merchantLogo,
        String merchantWebsite,
        String merchantType,
        List<String> merchantTags
) implements UseCaseResponse {
}
