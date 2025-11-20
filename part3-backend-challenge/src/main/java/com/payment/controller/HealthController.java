package com.payment.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Instant;
import java.util.Map;

/**
 * Status controller - EXAMPLE PROVIDED
 * Use this as a reference for creating your TransactionController
 * Note: Health endpoint is provided by Micronaut Management at /health
 */
@Controller("/api/v1/status")
@Tag(name = "Status")
public class HealthController {

    @Get
    @Operation(summary = "API status check", description = "Returns the status of the API")
    public HttpResponse<Map<String, Object>> status() {
        return HttpResponse.ok(Map.of(
            "status", "UP",
            "timestamp", Instant.now().toString(),
            "service", "payment-api",
            "version", "1.0.0"
        ));
    }
}
