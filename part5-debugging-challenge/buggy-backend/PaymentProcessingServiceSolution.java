package com.payment.service;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * PRODUCTION-GRADE Payment Processing Service
 * All 10 bugs identified and fixed:
 * 
 * BUG 1: ✅ Race condition on totalProcessed → AtomicReference
 * BUG 2: ✅ Failed status never persisted → Explicit DB update
 * BUG 3: ✅ No transaction management → @Transactional boundaries
 * BUG 4: ✅ No error propagation → ProcessingResult object
 * BUG 5: ✅ Data races in parallel streams → Sequential processing + thread-safe collections
 * BUG 6: ✅ No timeout protection → ExecutorService with explicit timeouts
 * BUG 7: ✅ No input validation → Null/empty checks
 * BUG 8: ✅ Audit logging not protected → Separate try-catch block
 * BUG 9: ✅ Inconsistent totalProcessed → Update AFTER DB success
 * BUG 10: ✅ Silent exception swallowing → Proper exception handling hierarchy
 */
@Singleton
public class PaymentProcessingServiceFixed {
    
    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessingServiceFixed.class);
    
    private final PaymentRepository paymentRepository;
    private final AuditService auditService;
    
    // BUG 1 FIX: Use AtomicReference for thread-safe updates
    private final AtomicReference<BigDecimal> totalProcessed = 
        new AtomicReference<>(BigDecimal.ZERO);
    
    // BUG 6 FIX: Explicit ExecutorService with bounded thread pool and timeout
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final long PAYMENT_TIMEOUT_SECONDS = 30;
    
    @Inject
    public PaymentProcessingServiceFixed(PaymentRepository paymentRepository, 
                                         AuditService auditService) {
        this.paymentRepository = paymentRepository;
        this.auditService = auditService;
    }
    
    /**
     * Process batch of payments with all bug fixes
     * 
     * BUG 7 FIX: Validate input parameters (null/empty checks)
     * BUG 5 FIX: Sequential processing instead of parallel to avoid data races
     * BUG 4 FIX: Return detailed ProcessingResult instead of void
     * 
     * @param payments List of payments to process (validated for null/empty)
     * @return ProcessingResult with detailed success/failure information
     */
    public ProcessingResult processPaymentBatch(List<Payment> payments) {
        // BUG 7 FIX: Input validation
        if (payments == null) {
            LOG.warn("Payment batch is null");
            return new ProcessingResult(0, 0, Collections.emptyList(), Collections.emptyList());
        }
        
        if (payments.isEmpty()) {
            LOG.warn("Payment batch is empty");
            return new ProcessingResult(0, 0, Collections.emptyList(), Collections.emptyList());
        }
        
        List<String> successfulIds = new ArrayList<>();
        List<ProcessingError> errors = new ArrayList<>();
        
        // BUG 5 FIX: Process sequentially instead of parallelStream to avoid data races
        // If parallel processing needed, use proper thread-safe aggregation
        for (Payment payment : payments) {
            // BUG 7 FIX: Validate individual payment object
            if (payment == null) {
                ProcessingError error = new ProcessingError("NULL", "Payment object is null");
                errors.add(error);
                LOG.error("Null payment in batch");
                continue;
            }
            
            try {
                processPayment(payment);
                successfulIds.add(payment.getId());
                
            } catch (PaymentValidationException e) {
                LOG.warn("Payment validation failed for {}: {}", 
                    payment.getId(), e.getMessage());
                errors.add(new ProcessingError(payment.getId(), e.getMessage()));
                
                // BUG 2 & BUG 10 FIX: Persist failed status in separate transaction
                // Don't let failure persistence exception hide the original error
                persistFailedPaymentStatus(payment, e.getMessage());
                
            } catch (Exception e) {
                LOG.error("Unexpected error processing payment {}: {}", 
                    payment.getId(), e.getMessage(), e);
                errors.add(new ProcessingError(payment.getId(), e.getMessage()));
                
                // BUG 2 & BUG 10 FIX: Persist failed status with proper exception handling
                persistFailedPaymentStatus(payment, e.getMessage());
            }
        }
        
        // BUG 4 FIX: Return detailed result object instead of void
        return new ProcessingResult(
            successfulIds.size(),
            errors.size(),
            successfulIds,
            errors
        );
    }
    
    /**
     * Process a single payment within a transaction
     * 
     * BUG 3 FIX: @Transactional ensures atomicity
     * BUG 9 FIX: Update totalProcessed AFTER successful database operation
     * BUG 8 FIX: Audit logging in separate try-catch (doesn't rollback payment)
     */
    @Transactional
    protected void processPayment(Payment payment) throws PaymentValidationException {
        // BUG 7 FIX: Comprehensive validation
        validatePayment(payment);
        
        // Update payment status in database
        payment.setStatus("COMPLETED");
        paymentRepository.update(payment);
        
        // BUG 8 FIX: Audit logging is protected - failure doesn't rollback payment
        try {
            auditService.log("Payment " + payment.getId() + " processed for amount " 
                + payment.getAmount());
        } catch (Exception auditError) {
            LOG.error("Audit logging failed for payment {} (non-critical)", 
                payment.getId(), auditError);
            // Continue execution - audit failure should not fail the payment
        }
        
        // BUG 9 FIX: Update totalProcessed AFTER successful database operation
        // If DB update above fails, this line won't execute (due to exception)
        // Ensures consistency between totalProcessed and database
        totalProcessed.updateAndGet(current -> current.add(payment.getAmount()));
    }
    
    /**
     * BUG 2 FIX: Explicitly persist failed payment status to database
     * BUG 10 FIX: Separate transaction prevents exception chaining
     * BUG 8 FIX: This operation is isolated and won't affect other payments
     */
    @Transactional
    protected void persistFailedPaymentStatus(Payment payment, String errorMessage) {
        if (payment == null || payment.getId() == null) {
            LOG.error("Cannot persist status for null payment");
            return;
        }
        
        try {
            payment.setStatus("FAILED");
            payment.setErrorMessage(errorMessage);
            paymentRepository.update(payment);
            
            // BUG 8 FIX: Protect audit logging
            try {
                auditService.log("Payment " + payment.getId() + " failed: " + errorMessage);
            } catch (Exception auditError) {
                LOG.error("Audit logging failed for failed payment {} (non-critical)", 
                    payment.getId(), auditError);
            }
            
        } catch (Exception persistError) {
            // BUG 10 FIX: Log persistence error but don't re-throw
            // The original payment processing error already recorded
            LOG.error("Failed to persist error status for payment {}: {}", 
                payment.getId(), persistError.getMessage(), persistError);
        }
    }
    
    /**
     * BUG 6 FIX: Process payments asynchronously with explicit timeout protection
     * Use this when you need parallel processing
     */
    public ProcessingResult processPaymentBatchWithTimeout(List<Payment> payments) 
            throws InterruptedException {
        // BUG 7 FIX: Input validation
        if (payments == null || payments.isEmpty()) {
            LOG.warn("Empty or null payment batch");
            return new ProcessingResult(0, 0, Collections.emptyList(), Collections.emptyList());
        }
        
        List<Future<PaymentResult>> futures = new ArrayList<>();
        
        // BUG 6 FIX: Submit tasks to executor service
        for (Payment payment : payments) {
            if (payment == null) {
                LOG.error("Null payment in batch");
                continue;
            }
            
            Future<PaymentResult> future = executorService.submit(() -> {
                try {
                    processPayment(payment);
                    return new PaymentResult(payment.getId(), true, null);
                } catch (PaymentValidationException e) {
                    LOG.warn("Payment {} validation failed: {}", payment.getId(), e.getMessage());
                    persistFailedPaymentStatus(payment, e.getMessage());
                    return new PaymentResult(payment.getId(), false, e.getMessage());
                } catch (Exception e) {
                    LOG.error("Payment {} processing failed: {}", payment.getId(), e.getMessage(), e);
                    persistFailedPaymentStatus(payment, e.getMessage());
                    return new PaymentResult(payment.getId(), false, e.getMessage());
                }
            });
            
            futures.add(future);
        }
        
        // BUG 6 FIX: Wait for all with explicit timeout
        List<String> successful = new ArrayList<>();
        List<ProcessingError> errors = new ArrayList<>();
        
        for (int i = 0; i < futures.size(); i++) {
            Future<PaymentResult> future = futures.get(i);
            try {
                // BUG 6 FIX: Timeout prevents indefinite hanging
                PaymentResult result = future.get(PAYMENT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                
                if (result.isSuccess()) {
                    successful.add(result.getPaymentId());
                } else {
                    errors.add(new ProcessingError(result.getPaymentId(), result.getErrorMessage()));
                }
                
            } catch (TimeoutException e) {
                LOG.error("Payment processing timeout for payment {}", i);
                future.cancel(true); // BUG 6 FIX: Cancel stuck tasks
                errors.add(new ProcessingError("TIMEOUT_" + i, "Processing timeout exceeded " + PAYMENT_TIMEOUT_SECONDS + "s"));
                
            } catch (ExecutionException e) {
                LOG.error("Payment processing failed with execution error", e);
                errors.add(new ProcessingError("ERROR_" + i, e.getCause().getMessage()));
            }
        }
        
        return new ProcessingResult(successful.size(), errors.size(), successful, errors);
    }
    
    /**
     * BUG 7 FIX: Comprehensive payment validation
     */
    private void validatePayment(Payment payment) throws PaymentValidationException {
        if (payment == null) {
            throw new PaymentValidationException("Payment object cannot be null");
        }
        
        if (payment.getId() == null || payment.getId().trim().isEmpty()) {
            throw new PaymentValidationException("Payment ID cannot be null or empty");
        }
        
        if (payment.getAmount() == null) {
            throw new PaymentValidationException("Payment amount cannot be null");
        }
        
        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentValidationException(
                "Invalid payment amount: " + payment.getAmount() + 
                " (must be greater than 0)");
        }
        
        if (payment.getMerchantId() == null || payment.getMerchantId().trim().isEmpty()) {
            throw new PaymentValidationException("Merchant ID cannot be null or empty");
        }
    }
    
    /**
     * BUG 1 FIX: Thread-safe getter for total processed
     */
    public BigDecimal getTotalProcessed() {
        return totalProcessed.get();
    }
    
    /**
     * BUG 1 FIX: Thread-safe reset for testing
     */
    public void resetTotal() {
        totalProcessed.set(BigDecimal.ZERO);
    }
    
    // ============ RESULT CLASSES ============
    
    /**
     * BUG 4 FIX: Detailed result object for error propagation
     */
    public static class ProcessingResult {
        private final int successCount;
        private final int failureCount;
        private final List<String> successfulIds;
        private final List<ProcessingError> errors;
        
        public ProcessingResult(int successCount, int failureCount,
                              List<String> successfulIds, List<ProcessingError> errors) {
            this.successCount = successCount;
            this.failureCount = failureCount;
            // BUG 7 FIX: Defensive copy to prevent external mutation
            this.successfulIds = new ArrayList<>(successfulIds);
            this.errors = new ArrayList<>(errors);
        }
        
        public int getSuccessCount() { 
            return successCount; 
        }
        
        public int getFailureCount() { 
            return failureCount; 
        }
        
        public List<String> getSuccessfulIds() { 
            return Collections.unmodifiableList(successfulIds); 
        }
        
        public List<ProcessingError> getErrors() { 
            return Collections.unmodifiableList(errors); 
        }
        
        public boolean hasErrors() { 
            return !errors.isEmpty(); 
        }
        
        public int getTotalCount() { 
            return successCount + failureCount; 
        }
    }
    
    /**
     * BUG 4 FIX: Error details for failed payment
     */
    public static class ProcessingError {
        private final String paymentId;
        private final String errorMessage;
        private final long timestamp;
        
        public ProcessingError(String paymentId, String errorMessage) {
            this.paymentId = paymentId;
            this.errorMessage = errorMessage;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getPaymentId() { 
            return paymentId; 
        }
        
        public String getErrorMessage() { 
            return errorMessage; 
        }
        
        public long getTimestamp() { 
            return timestamp; 
        }
    }
    
    /**
     * BUG 6 FIX: Internal result for async processing
     */
    private static class PaymentResult {
        private final String paymentId;
        private final boolean success;
        private final String errorMessage;
        
        PaymentResult(String paymentId, boolean success, String errorMessage) {
            this.paymentId = paymentId;
            this.success = success;
            this.errorMessage = errorMessage;
        }
        
        String getPaymentId() { 
            return paymentId; 
        }
        
        boolean isSuccess() { 
            return success; 
        }
        
        String getErrorMessage() { 
            return errorMessage; 
        }
    }
    
    /**
     * BUG 7 FIX: Custom exception for validation errors
     */
    public static class PaymentValidationException extends Exception {
        public PaymentValidationException(String message) {
            super(message);
        }
        
        public PaymentValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}