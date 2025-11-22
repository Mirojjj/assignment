package com.payment.dto.merchantDto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@Serdeable
@Introspected
@RecordBuilder
public record MerchantDto(
        Long txnId,
        BigDecimal amount,
        String currency,
        String status,
        String cardType,
        String cardLast4,
        Long acquirer,
        Long issuer,
        String merchantId,
        String authCode
) {

}
