package com.payment.repository;

import com.payment.entity.TransactionMaster;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Repository for TransactionMaster entities.
 * 
 * TODO: Add custom query methods for:
 * - Finding transactions by merchant ID with date range
 * - Paginated queries
 * - Aggregation queries for summary calculation
 * - Join queries with transaction details
 */
@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TransactionRepository extends CrudRepository<TransactionMaster, Long> {

    // Example: Basic finder method (provided)
    List<TransactionMaster> findByMerchantId(String merchantId);

    @Query("""
            SELECT *
              FROM operators.transaction_master
              WHERE merchant_id = :merchantId
                AND (:startDate IS NULL OR txn_date >= CAST(:startDate AS DATE))
                AND (:endDate IS NULL OR txn_date <= CAST(:endDate AS DATE))
                AND (:status IS NULL OR status = :status)
              ORDER BY txn_date DESC
              LIMIT :limit OFFSET :offset
    """)
    List<TransactionMaster> findTransactions(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate,
            @Nullable String status,
            int limit,
            int offset
    );

    // Count total transactions for pagination
    @Query("""
        SELECT COUNT(*)\s
        FROM operators.transaction_master
        WHERE merchant_id = :merchantId
          AND (:startDate IS NULL OR txn_date >= CAST(:startDate AS DATE))
          AND (:endDate IS NULL OR txn_date <= CAST(:endDate AS DATE))
          AND (:status IS NULL OR status = :status);
    """)
    int countTransactions(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate,
            @Nullable String status
    );


    // TODO: Add your custom query methods here
    // Examples:
    // - Page<TransactionMaster> findByMerchantIdAndTxnDateBetween(...)
    // - TransactionSummary calculateSummary(...)
    // - List<TransactionWithDetails> findTransactionsWithDetails(...)
}
