package com.payment.serviceImpl;


import com.payment.dto.transactionDto.CreateTransactionRequestPayloadWithMerchantId;
import com.payment.entity.TransactionMaster;
import com.payment.enums.TransactionStatus;
import com.payment.payloads.CreateTransactionRequestPayload;
import com.payment.repository.TransactionRepository;
import com.payment.responses.CreateTransactionResponse;
import com.payment.responses.CreateTransactionResponseBuilder;
import com.payment.services.CreateTransactionService;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class CreateTransactionServiceImpl implements CreateTransactionService {

    private final Logger logger = LoggerFactory.getLogger(CreateTransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;

    @Inject
    public CreateTransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequestPayloadWithMerchantId request) {

        TransactionMaster entity =  toTransactionMaster(request);

        var result = this.transactionRepository.save(entity);

        logger.info("Transaction inserted into the database");

        return CreateTransactionResponseBuilder.builder()
                .transactionId(result.getTxnId().toString())
                .responseMessage("Successfully inserted New Transaction")
                .build();

    }

    private TransactionMaster toTransactionMaster(CreateTransactionRequestPayloadWithMerchantId request) {
        TransactionMaster tm = new TransactionMaster();
        tm.setTxnId(System.currentTimeMillis());
        tm.setAmount(new BigDecimal(request.amount()));
        tm.setTxnDate(java.sql.Date.valueOf(LocalDate.now()));
        tm.setLocalTxnDateTime(Instant.now());
        tm.setAuthCode(request.authCode());
        tm.setCurrency(request.currency());
        tm.setMerchantId(request.merchantId());
        tm.setGpAcquirerId(request.gpAcquirerId());
        tm.setGpIssuerId(request.gpIssuerId());
        tm.setResponseCode(request.responseCode());
        tm.setStatus(TransactionStatus.pending.name());
        tm.setCardLast4(request.cardLast4());
        tm.setCardType(request.cardType());
        return tm;
    }


}
