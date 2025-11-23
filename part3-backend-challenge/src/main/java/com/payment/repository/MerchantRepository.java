package com.payment.repository;

import com.payment.entity.Merchant;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MerchantRepository extends CrudRepository<Merchant, Integer > {
    Merchant findByMerchantId(Integer merchantId);


    @Query("""
    UPDATE merchants.merchants
    SET merchant_name     = COALESCE(:merchantName, merchant_name),
        merchant_status   = COALESCE(:merchantStatus, merchant_status),
        contact_info      = COALESCE(:contactInfo, contact_info),
        merchant_category = COALESCE(:merchantCategory, merchant_category),
        merchant_location = COALESCE(:merchantLocation, merchant_location),
        merchant_rating   = COALESCE(:merchantRating, merchant_rating),
        num_orders        = COALESCE(:numOrders, num_orders),
        payment_method    = COALESCE(:paymentMethod, payment_method),
        merchant_logo     = COALESCE(:merchantLogo, merchant_logo),
        merchant_website  = COALESCE(:merchantWebsite, merchant_website),
        merchant_type     = COALESCE(:merchantType, merchant_type),
        merchant_tags     = COALESCE(:merchantTags, merchant_tags),
        last_updated      = NOW()
    WHERE merchant_id = :merchantId
""")
    long updateMerchant(
            Integer merchantId,
            @Nullable String merchantName,
            @Nullable String merchantStatus,
            @Nullable String contactInfo,
            @Nullable String merchantCategory,
            @Nullable String merchantLocation,
            @Nullable BigDecimal merchantRating,
            @Nullable Integer numOrders,
            @Nullable String paymentMethod,
            @Nullable String merchantLogo,
            @Nullable String merchantWebsite,
            @Nullable String merchantType,
            @Nullable List<String> merchantTags
    );

    @Query("""
DELETE FROM merchants.merchants WHERE merchant_id = :merchantId
""")
    long deleteMerhcantByMerchantId(
            Integer merchantId
    );
}


