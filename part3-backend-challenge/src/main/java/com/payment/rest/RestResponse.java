package com.payment.rest;


import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@Serdeable
@RecordBuilder
public record RestResponse<T>(String response_code, String response_message, T  data ) {

    public static <T> RestResponse success() {
        return RestResponseBuilder.builder()
                .response_code("0")
                .response_message("SUCCESS")
                .data(null)
                .build();
    }

    public static <T> RestResponse success(T data) {
        return RestResponseBuilder.builder()
                .response_code("0")
                .response_message("SUCCESS")
                .data(data)
                .build();
    }

    public static <T> RestResponse success(T data,String code,String message) {
        return RestResponseBuilder.builder()
                .response_code(code)
                .response_message(message)
                .data(data)
                .build();
    }

    public static RestResponse error(String code, String message) {
        return RestResponseBuilder.builder()
                .response_code(code)
                .response_message(message)
                .build();
    }
}