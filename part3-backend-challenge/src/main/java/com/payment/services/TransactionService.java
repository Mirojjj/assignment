package com.payment.services;

import com.payment.payloads.TransactionRequestPayload;
import com.payment.responses.CreateTransactionResponse;
import com.payment.responses.TransactionResponse;

public interface TransactionService {

    TransactionResponse fetchTransactionsList(TransactionRequestPayload request);
}
