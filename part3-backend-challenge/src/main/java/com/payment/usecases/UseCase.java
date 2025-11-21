package com.payment.usecases;

import com.payment.support.Result;

@FunctionalInterface
public interface UseCase<I extends UseCaseRequest, O extends UseCaseResponse> {
    Result<O> execute(UseCaseContext context, I request);
}

