package com.payment.usecases;

public interface UseCaseRequest {

    interface SingleParamUseCaseRequest<T> extends UseCaseRequest{
        T data();
    }
}
