package com.payment.services;

import com.payment.dto.transactionDto.CreateTransactionRequestPayloadWithMerchantId;
import com.payment.payloads.CreateTransactionRequestPayload;
import com.payment.responses.CreateTransactionResponse;

public interface CreateTransactionService {

    CreateTransactionResponse createTransaction(CreateTransactionRequestPayload request);

}
