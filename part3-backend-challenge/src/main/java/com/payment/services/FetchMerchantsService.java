package com.payment.services;

import com.payment.payloads.TransactionRequestPayload;
import com.payment.responses.TransactionResponse;

public interface FetchMerchantsService {
    TransactionResponse fetch(TransactionRequestPayload request);
}
