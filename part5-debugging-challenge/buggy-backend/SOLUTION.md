# Bug 1: Backend - Payment Processing Service - SOLUTION

## Issues Found

### 1. **Race Condition on `totalProcessed` (Critical)**
```java
private BigDecimal totalProcessed = BigDecimal.ZERO;
// ...
totalProcessed = totalProcessed.add(payment.getAmount());
```

**Problem:** Multiple threads modifying shared mutable state without synchronization.

**Impact:**
- Lost updates when multiple threads read-modify-write simultaneously
- Incorrect total calculated
- Non-deterministic behavior (works sometimes, fails other times)

### 2. **Failed Payments Not Persisted (Critical)**
```java
catch (Exception e) {
    payment.setStatus("FAILED");
    // Note: Not saving failed status to DB
}
```

**Problem:** Failed payment status is set in memory but never saved to database.

**Impact:**
- Database shows payment as pending when it actually failed
- No audit trail of failures
- Retry logic might process same payment multiple times

### 3. **No Transaction Management (Critical)**
```java
public void processPaymentBatch(List<Payment> payments) {
    payments.parallelStream().forEach(payment -> {
        // Multiple DB operations without transaction boundary
        paymentRepository.update(payment);
        auditService.log(...);
    });
}
```

**Problem:** Each payment processed independently without transaction context.

**Impact:**
- If audit logging fails, payment still marked as completed
- Inconsistent database state
- Cannot rollback batch on error

### 4. **No Error Propagation (Major)**
```java
} catch (Exception e) {
    payment.setStatus("FAILED");
}
```

**Problem:** Exceptions swallowed silently, no notification to caller.

**Impact:**
- Calling code thinks everything succeeded
- No alerting or logging of failures
- Difficult to debug production issues

### 5. **Parallel Stream Misuse (Minor)**
```java
payments.parallelStream().forEach(payment -> {
```

**Problem:** Using parallel streams with I/O operations and shared state.

**Impact:**
- Unpredictable thread pool usage
- Context switching overhead
- Makes debugging harder

## Root Cause

The developer tried to optimize performance with parallel processing but:
- Didn't consider thread-safety of shared state
- Didn't implement proper error handling
- Didn't use transactions to maintain consistency

This is a classic case of premature optimization causing correctness issues.

## Fixed Code

```java
package com.payment.service;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Fixed version of PaymentProcessingService
 * Properly handles concurrency, transactions, and error handling
 */
@Singleton
public class PaymentProcessingService {
    
    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessingService.class);
    
    private final PaymentRepository paymentRepository;
    private final AuditService auditService;
    
    // FIX 1: Use AtomicReference for thread-safe updates
    private final AtomicReference<BigDecimal> totalProcessed = 
        new AtomicReference<>(BigDecimal.ZERO);
    
    @Inject
    public PaymentProcessingService(PaymentRepository paymentRepository, 
                                   AuditService auditService) {
        this.paymentRepository = paymentRepository;
        this.auditService = auditService;
    }
    
    /**
     * Processes a batch of payments with proper error handling and transaction management
     * 
     * @param payments List of payments to process
     * @return ProcessingResult containing success/failure counts and details
     */
    public ProcessingResult processPaymentBatch(List<Payment> payments) {
        List<String> successfulIds = new ArrayList<>();
        List<ProcessingError> errors = new ArrayList<>();
        
        // FIX 2: Process sequentially for simplicity and correctness
        // If performance is critical, use proper concurrent data structures
        for (Payment payment : payments) {
            try {
                processPayment(payment);
                successfulIds.add(payment.getId());
            } catch (Exception e) {
                LOG.error("Failed to process payment {}: {}", 
                    payment.getId(), e.getMessage(), e);
                errors.add(new ProcessingError(payment.getId(), e.getMessage()));
                
                // FIX 3: Persist failed status to database
                try {
                    updateFailedPaymentStatus(payment, e.getMessage());
                } catch (Exception persistError) {
                    LOG.error("Failed to persist error status for payment {}", 
                        payment.getId(), persistError);
                }
            }
        }
        
        // FIX 4: Return detailed result object
        return new ProcessingResult(
            successfulIds.size(),
            errors.size(),
            successfulIds,
            errors
        );
    }
    
    /**
     * Process a single payment within a transaction
     * FIX 5: Transactional boundary ensures atomicity
     */
    @Transactional
    protected void processPayment(Payment payment) {
        // Validate payment
        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Invalid amount: " + payment.getAmount());
        }
        
        // Update payment status
        payment.setStatus("COMPLETED");
        paymentRepository.update(payment);
        
        // Audit log (within same transaction)
        auditService.log("Payment " + payment.getId() + " processed for amount " 
            + payment.getAmount());
        
        // Update total (thread-safe)
        totalProcessed.updateAndGet(current -> current.add(payment.getAmount()));
    }
    
    /**
     * Update failed payment status in a separate transaction
     */
    @Transactional
    protected void updateFailedPaymentStatus(Payment payment, String errorMessage) {
        payment.setStatus("FAILED");
        payment.setErrorMessage(errorMessage);
        paymentRepository.update(payment);
        
        auditService.log("Payment " + payment.getId() + " failed: " + errorMessage);
    }
    
    public BigDecimal getTotalProcessed() {
        return totalProcessed.get();
    }
    
    /**
     * Result object for batch processing
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
            this.successfulIds = successfulIds;
            this.errors = errors;
        }
        
        // Getters...
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public List<String> getSuccessfulIds() { return successfulIds; }
        public List<ProcessingError> getErrors() { return errors; }
        public boolean hasErrors() { return !errors.isEmpty(); }
    }
    
    /**
     * Error details for failed payment
     */
    public static class ProcessingError {
        private final String paymentId;
        private final String errorMessage;
        
        public ProcessingError(String paymentId, String errorMessage) {
            this.paymentId = paymentId;
            this.errorMessage = errorMessage;
        }
        
        // Getters...
        public String getPaymentId() { return paymentId; }
        public String getErrorMessage() { return errorMessage; }
    }
}
```

## Key Improvements

### 1. Thread Safety
- **Before:** Direct assignment to shared `BigDecimal`
- **After:** `AtomicReference<BigDecimal>` with `updateAndGet()`
- **Alternative:** Could use `synchronized` block or `ReentrantLock`

### 2. Error Handling
- **Before:** Exceptions silently caught
- **After:** Logged, persisted to DB, returned to caller
- **Benefit:** Full visibility into failures

### 3. Transaction Management
- **Before:** No transaction boundaries
- **After:** `@Transactional` on individual payment processing
- **Benefit:** Atomic operations, can rollback on error

### 4. Error Propagation
- **Before:** No return value, no indication of failures
- **After:** Returns `ProcessingResult` with success/failure details
- **Benefit:** Caller can handle errors appropriately

### 5. Parallel Processing
- **Before:** Parallel stream with shared state
- **After:** Sequential processing (simpler, correct)
- **Alternative:** If performance critical, use `ExecutorService` with proper concurrent collections

## Alternative Solutions

### Option 1: Keep Parallel Processing (Advanced)
```java
public ProcessingResult processPaymentBatch(List<Payment> payments) {
    // Use concurrent collections
    List<String> successfulIds = new CopyOnWriteArrayList<>();
    List<ProcessingError> errors = new CopyOnWriteArrayList<>();
    
    payments.parallelStream().forEach(payment -> {
        try {
            processPayment(payment);
            successfulIds.add(payment.getId());
        } catch (Exception e) {
            errors.add(new ProcessingError(payment.getId(), e.getMessage()));
            updateFailedPaymentStatus(payment, e.getMessage());
        }
    });
    
    return new ProcessingResult(successfulIds.size(), errors.size(), 
        successfulIds, errors);
}
```

### Option 2: Use CompletableFuture (Modern Approach)
```java
public CompletableFuture<ProcessingResult> processPaymentBatchAsync(
        List<Payment> payments) {
    
    List<CompletableFuture<Void>> futures = payments.stream()
        .map(payment -> CompletableFuture.runAsync(() -> 
            processPayment(payment)))
        .collect(Collectors.toList());
    
    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenApply(v -> buildResult(futures));
}
```

## Testing Strategy

```java
@MicronautTest
class PaymentProcessingServiceTest {
    
    @Inject
    PaymentProcessingService service;
    
    @Test
    void testConcurrentProcessing() throws InterruptedException {
        // Create 100 payments
        List<Payment> payments = createTestPayments(100);
        
        // Process in multiple threads
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> 
                service.processPaymentBatch(payments.subList(i*10, (i+1)*10))
            );
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        // Verify total is correct
        BigDecimal expectedTotal = payments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        assertEquals(expectedTotal, service.getTotalProcessed());
    }
    
    @Test
    void testFailedPaymentPersisted() {
        Payment invalidPayment = new Payment("ID-1", BigDecimal.valueOf(-100));
        
        ProcessingResult result = service.processPaymentBatch(
            List.of(invalidPayment));
        
        assertTrue(result.hasErrors());
        
        // Verify failed status in DB
        Payment persisted = paymentRepository.findById("ID-1").get();
        assertEquals("FAILED", persisted.getStatus());
    }
}
```

## Score Breakdown

**Full Credit (7/7):**
- Identified all 4+ bugs
- Explained race conditions clearly
- Fixed with transactions and thread-safe operations
- Provided complete working solution

**Partial Credit (4-6):**
- Found 2-3 major bugs
- Basic fix but missing some aspects
- No test verification

**Minimal Credit (1-3):**
- Found only 1 bug
- Fix doesn't fully address issues
- No explanation of root cause
