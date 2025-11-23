package com.payment.dto.merchantDto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@Introspected
@RecordBuilder
public record MerchantDto(
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
){ }
