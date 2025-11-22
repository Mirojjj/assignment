package com.payment.support;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Optional;

@Serdeable
public record Result<T>(T data,
                        Error error) {

    public boolean hasError() {
        return Optional.ofNullable(error).isPresent();
    }

    public static Result fail(Error error) {
        return new Result(null, error);
    }

    public static <T> Result<T> ok() {
        return new Result(null, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result(data, null);
    }


}
