package com.payment.entity;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
@MappedEntity(value = "transaction_details", schema = "operators")
public class TransactionDetail {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long txnDetailId;

    private Long masterTxnId;
    private String detailType;
    private BigDecimal amount;
    private String currency;
    private String description;
    private Instant localTxnDateTime;
    
    @DateCreated
    private Instant createdAt;

    // Constructors
    public TransactionDetail() {
    }

    // Getters and Setters
    public Long getTxnDetailId() {
        return txnDetailId;
    }

    public void setTxnDetailId(Long txnDetailId) {
        this.txnDetailId = txnDetailId;
    }

    public Long getMasterTxnId() {
        return masterTxnId;
    }

    public void setMasterTxnId(Long masterTxnId) {
        this.masterTxnId = masterTxnId;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getLocalTxnDateTime() {
        return localTxnDateTime;
    }

    public void setLocalTxnDateTime(Instant localTxnDateTime) {
        this.localTxnDateTime = localTxnDateTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
