package com.payment.usecases;

public interface UseCaseContext extends UseCaseRequest {
    class EmptyUseCaseContext implements UseCaseContext {
    }

    static UseCaseContext empty() {
        return new EmptyUseCaseContext();
    }
}
