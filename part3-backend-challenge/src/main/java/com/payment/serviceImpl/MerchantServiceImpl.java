package com.payment.serviceImpl;

import com.payment.dto.merchantDto.MerchantDto;
import com.payment.entity.TransactionMaster;
import com.payment.enums.TransactionStatus;
import com.payment.payloads.CreateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayloadWithMerchantId;
import com.payment.repository.TransactionRepository;
import com.payment.responses.*;
import com.payment.services.MerchantService;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MerchantServiceImpl implements MerchantService {

    private final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
    private final TransactionRepository transactionRepository;

    @Inject
    public MerchantServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public MerchantListResponse getAllMerchants() {
        var result = this.transactionRepository.findAll();

        logger.info("resulttt" + result);

        List<MerchantDto> merchantDtos = result.stream()
                .map(tx -> new MerchantDto(
                        tx.getTxnId(),
                        tx.getAmount(),
                        tx.getCurrency(),
                        tx.getStatus(),
                        tx.getCardType(),
                        tx.getCardLast4(),
                        tx.getGpAcquirerId(),
                        tx.getGpIssuerId(),
                        tx.getMerchantId(),
                        tx.getAuthCode()
                )).toList();

        return new MerchantListResponse(merchantDtos);
    }

    @Override
    public MerchantDetailsResponse getMerchantDetails(String merchantId) {
        var result = this.transactionRepository.findByMerchantId(merchantId);
        return null;
    }

    @Override
    public CreateMerchantResponse createNewMerchant(CreateMerchantPayload createMerchantPayload) {
        TransactionMaster entity =  toTransactionMaster(createMerchantPayload);

        var result = this.transactionRepository.save(entity);

        logger.info("New merchant created Successfully");

        return CreateMerchantResponseBuilder.builder()
                .merchantId(result.getMerchantId())
                .message("New merchant created Successfully")
                .build();
    }

    private TransactionMaster toTransactionMaster(CreateMerchantPayload request) {
        TransactionMaster tm = new TransactionMaster();
        tm.setTxnId(System.currentTimeMillis());
        tm.setAmount(new BigDecimal(request.amount()));
        tm.setTxnDate(java.sql.Date.valueOf(LocalDate.now()));
        tm.setLocalTxnDateTime(Instant.now());
        tm.setAuthCode(request.authCode());
        tm.setCurrency(request.currency());
        tm.setMerchantId(UUID.randomUUID().toString());
        tm.setGpAcquirerId(request.gpAcquirerId());
        tm.setGpIssuerId(request.gpIssuerId());
        tm.setResponseCode(request.responseCode());
        tm.setStatus(TransactionStatus.pending.name());
        tm.setCardLast4(request.cardLast4());
        tm.setCardType(request.cardType());
        return tm;
    }

    @Override
    public UpdateMerchantResponse updateMerchant(UpdateMerchantPayloadWithMerchantId updateMerchantPayload) {
        return null;
    }

    @Override
    public DeleteMerchantResponse deleteMerchant(String merchantId) {
        return null;
    }
}
