package com.payment.entity;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
@MappedEntity(value = "transaction_master", schema = "operators")
public class TransactionMaster {

    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long txnId;

    private String merchantId;
    private Long gpAcquirerId;
    private Long gpIssuerId;
    private java.sql.Date txnDate;
    private Instant localTxnDateTime;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String cardType;
    private String cardLast4;
    private String authCode;
    private String responseCode;
    
    @DateCreated
    private Instant createdAt;

    // Constructors
    public TransactionMaster() {
    }

    // Getters and Setters
    public Long getTxnId() {
        return txnId;
    }

    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getGpAcquirerId() {
        return gpAcquirerId;
    }

    public void setGpAcquirerId(Long gpAcquirerId) {
        this.gpAcquirerId = gpAcquirerId;
    }

    public Long getGpIssuerId() {
        return gpIssuerId;
    }

    public void setGpIssuerId(Long gpIssuerId) {
        this.gpIssuerId = gpIssuerId;
    }

    public java.sql.Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(java.sql.Date txnDate) {
        this.txnDate = txnDate;
    }

    public Instant getLocalTxnDateTime() {
        return localTxnDateTime;
    }

    public void setLocalTxnDateTime(Instant localTxnDateTime) {
        this.localTxnDateTime = localTxnDateTime;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
