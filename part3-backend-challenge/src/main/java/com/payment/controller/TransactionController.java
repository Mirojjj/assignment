package com.payment.controller;

import com.payment.dto.transactionDto.CreateTransactionRequestPayloadWithMerchantId;
import com.payment.dto.transactionDto.CreateTransactionRequestPayloadWithMerchantIdBuilder;
import com.payment.payloads.*;
import com.payment.rest.RestResponse;
import com.payment.usecases.CreateTransactionControllerUseCase;
import com.payment.usecases.TransactionControllerUseCase;
import com.payment.usecases.UseCaseContext;
import com.payment.usecases.merchantUsecase.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Transaction Controller - BASIC IMPLEMENTATION PROVIDED
 * <p>
 * TODO for Junior Developer:
 * 1. Create TransactionService and inject it
 * 2. Implement actual database queries
 * 3. Add proper pagination
 * 4. Add date filtering
 * 5. Add status filtering
 * 6. Return proper TransactionResponse DTOs
 * 7. Add error handling
 */


/**
 private final TransactionRepository transactionRepository;

 public TransactionController(TransactionRepository transactionRepository) {
 this.transactionRepository = transactionRepository;
 }

 // TODO: Create TransactionService to handle business logic
 // TODO: Move repository calls to service layer

 @Get("/{merchantId}/transactions")
 @Operation( summary = "Get merchant transactions",
 description = "Returns paginated list of transactions for a merchant. TODO: Implement proper pagination, filtering, and database queries."
 )
 public HttpResponse<Map<String, Object>> getTransactions(
 @PathVariable String merchantId,
 @QueryValue Optional<Integer> page,
 @QueryValue Optional<Integer> size,
 @QueryValue Optional<String> startDate,
 @QueryValue Optional<String> endDate,
 @QueryValue Optional<String> status
 ) {
 // TODO: Replace this stub with actual implementation
 var transactions = transactionRepository.findByMerchantId(merchantId);
 return HttpResponse.ok(Map.of(
 "message", "TODO: Implement proper pagination and filtering",
 "merchantId", merchantId,
 "page", page.orElse(0),
 "size", size.orElse(10),
 "totalTransactions", transactions.size(),
 "transactions", transactions,
 "note", "Basic query implemented. Junior developer should add pagination, date filtering, and status filtering."
 ));
 }

 @Post("/{merchantId}/transactions")
 @Operation( summary = "Create new transaction",
 description = "Creates a new transaction for a merchant. TODO: Add validation, error handling, and business logic."
 )
 public HttpResponse<Map<String, Object>> createTransaction(
 @PathVariable String merchantId,
 @Body TransactionMaster transaction
 ) {
 // TODO: Add validation
 // TODO: Add error handling
 // TODO: Move to service layer
 transaction.setMerchantId(merchantId);
 TransactionMaster saved = transactionRepository.save(transaction);
 return HttpResponse.created(Map.of(
 "message", "Transaction created",
 "transactionId", saved.getTxnId(),
 "note", "TODO: Add proper validation and error handling"
 ));
 }

 */

@Controller("/api/v1/merchants")
@Tag(name = "Transactions")
public class TransactionController {

    private final Logger logger = Logger.getLogger(TransactionController.class.getName());
    private TransactionControllerUseCase transactionControllerUseCase;
    private CreateTransactionControllerUseCase createTransactionControllerUseCase;
    private FetchMerchantListUseCase fetchMerchantListUseCase;
    private CreateMerchantUseCase createMerchantUseCase;
    private DeleteMerchantUseCase deleteMerchantUseCase;
    private MerchantDetailUseCase merchantDetailUseCase;
    private UpdateMerchantUseCase updateMerchantUseCase;


    @Inject
    public TransactionController(TransactionControllerUseCase transactionControllerUseCase,
                                 CreateTransactionControllerUseCase createTransactionControllerUseCase,
                                 FetchMerchantListUseCase fetchMerchantListUseCase,
                                 CreateMerchantUseCase createMerchantUseCase,
                                 DeleteMerchantUseCase deleteMerchantUseCase,
                                 MerchantDetailUseCase merchantDetailUseCase,
                                 UpdateMerchantUseCase updateMerchantUseCase

    ) {
        this.transactionControllerUseCase = transactionControllerUseCase;
        this.createTransactionControllerUseCase = createTransactionControllerUseCase;
        this.fetchMerchantListUseCase = fetchMerchantListUseCase;
        this.createMerchantUseCase = createMerchantUseCase;
        this.deleteMerchantUseCase = deleteMerchantUseCase;
        this.merchantDetailUseCase = merchantDetailUseCase;
        this.updateMerchantUseCase = updateMerchantUseCase;
    }

    @Get("/{merchantId}/transactions")
    @Operation(
            summary = "Get merchant transactions",
            description = "Returns paginated list of transactions for a merchant. TODO: Implement proper pagination, filtering, and database queries."
    )
    public RestResponse getMerchantTransactions(@PathVariable String merchantId,
                                                @QueryValue Optional<Integer> page,
                                                @QueryValue Optional<Integer> size,
                                                @QueryValue Optional<String> startDate,
                                                @QueryValue Optional<String> endDate,
                                                @QueryValue Optional<String> status) {
        logger.info("Get merchant transactions for merchant " + merchantId);
        TransactionRequestPayload request = TransactionRequestPayloadBuilder.builder()
                .merchantId(merchantId)
                .page(page)
                .size(size)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .build();

        var result = this.transactionControllerUseCase.execute(UseCaseContext.empty(), request);

        if (result.hasError()) {
            // Could be validation or not found
            return RestResponse.error("400", "Error while fetching transactions list");
        }

        // If no transactions found, return 404 explicitly
        if (result.data() == null) {
            return RestResponse.error("404", "No transactions found for merchant " + merchantId);
        }

        return RestResponse.success(result.data());

    }


    @Post("/transactions")
    @Operation(
            summary = "Create new transaction",
            description = "Creates a new transaction for a merchant. TODO: Add validation, error handling, and business logic."
    )
    public RestResponse createTransaction(@Body CreateTransactionRequestPayload request) {
        logger.info("Received create transaction payload" + request);

//        CreateTransactionRequestPayloadWithMerchantId requestEntity = toCreateTransactionRequestPayloadWithMerchantId(merchantId, request);


        var result = this.createTransactionControllerUseCase.execute(UseCaseContext.empty(), request);
        if (result.hasError()) {
            return RestResponse.error("400", "Error inserting data into db");
        }

        return RestResponse.success(result.data());
    }

    private CreateTransactionRequestPayloadWithMerchantId toCreateTransactionRequestPayloadWithMerchantId(String merchantId, CreateTransactionRequestPayload request) {
        return CreateTransactionRequestPayloadWithMerchantIdBuilder.builder()
                .merchantId(merchantId)
                .amount(request.amount())
                .authCode(request.authCode())
                .gpAcquirerId(request.gpAcquirerId())
                .gpIssuerId(request.gpIssuerId())
                .cardLast4(request.cardLast4())
                .currency(request.currency())
                .responseCode(request.responseCode())
                .cardType(request.cardType())
                .build();
    }

    @Get("getAllMerchants")
    public RestResponse getAllMerchants() {
        var result = this.fetchMerchantListUseCase.execute();
        if (result.hasError()) {
            return RestResponse.error("400", "Error while fetching all merchants");
        }
        return  RestResponse.success(result.data());
    }

    @Get("/{merchantId}")
    public RestResponse getMerchantDetail(@PathVariable String merchantId) {
        MerchantDetailRequestPayload requestPayload = MerchantDetailRequestPayloadBuilder.builder().merchantId(merchantId).build();
        var result = this.merchantDetailUseCase.execute(UseCaseContext.empty(), requestPayload);
        if (result.hasError()) {
            return RestResponse.error("400", "Error while fetching merchant detail");
        }
        return RestResponse.success(result.data());
    }

    @Post
    public RestResponse createMerchant(@Body CreateMerchantPayload request) {

        var result = this.createMerchantUseCase.execute(UseCaseContext.empty(), request);
        if (result.hasError()) {
            return RestResponse.error("400", "Error while creating merchant");
        }
        return RestResponse.success(result.data());
    }

    @Put("/{merchantId}")
    public RestResponse updateMerchant(@PathVariable String merchantId, @Body UpdateMerchantPayload request) {
        UpdateMerchantPayloadWithMerchantId requestEntity = UpdateMerchantPayloadWithMerchantIdBuilder.builder()
                .merchantId(Integer.parseInt(merchantId))
                .merchantName(request.merchantName())
                .merchantStatus(request.merchantStatus())
                .contactInfo(request.contactInfo())
                .merchantCategory(request.merchantCategory())
                .merchantLocation(request.merchantLocation())
                .merchantLogo(request.merchantLogo())
                .merchantType(request.merchantType())
                .numOrders(request.numOrders())
                .paymentMethod(request.paymentMethod())
                .merchantTags(request.merchantTags())
                .merchantRating(request.merchantRating())
                .merchantWebsite(request.merchantWebsite())
                .build();

        var result = this.updateMerchantUseCase.execute(UseCaseContext.empty(), requestEntity);

        if(result.hasError()) {
            return RestResponse.error("400", "Error while updating merchant");
        }

        return RestResponse.success(result.data());
    }

    @Delete("/{merchantId}")
    public RestResponse deleteMerchant(@PathVariable String merchantId) {

        DeleteMerchantPayload requestPayload = DeleteMerchantPayloadBuilder.builder().merchantId(merchantId).build();

        var result = this.deleteMerchantUseCase.execute(UseCaseContext.empty(), requestPayload);

        if(result.hasError()) {
            return RestResponse.error("400", "Error while deleting merchant");
        }
        return RestResponse.success(result.data());
    }
}

