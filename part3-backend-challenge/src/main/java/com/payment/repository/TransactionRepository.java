package com.payment.repository;

import com.payment.dto.transactionDto.*;
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
                    SELECT tm.txn_id              AS txn_id,
                               tm.amount              AS amount,
                               tm.currency            AS currency,
                               tm.status              AS status,
                               tm.local_txn_date_time AS timestamp,
                               tm.card_type           AS card_type,
                               tm.card_last4          AS card_last4,
                               acq.member_name        AS acquirer,
                               iss.member_name        AS issuer
                    FROM operators.transaction_master tm
                             LEFT JOIN operators.members acq ON tm.gp_acquirer_id = acq.member_id
                             LEFT JOIN operators.members iss ON tm.gp_issuer_id = iss.member_id
                    WHERE tm.merchant_id = :merchantId
                      AND (:startDate IS NULL OR tm.txn_date >= CAST(:startDate AS DATE))
                      AND (:endDate IS NULL OR tm.txn_date <= CAST(:endDate AS DATE))
                      AND (:status IS NULL OR tm.status = :status)
                    ORDER BY tm.txn_date DESC
                    LIMIT :limit OFFSET :offset
            """)
    List<TransactionsDto> findTransactions(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate,
            @Nullable String status,
            int limit,
            int offset
    );

    @Query("""
        SELECT COUNT(*) AS count
        FROM operators.transaction_master
        WHERE merchant_id = :merchantId
          AND (:startDate IS NULL OR txn_date >= CAST(:startDate AS DATE))
          AND (:endDate IS NULL OR txn_date <= CAST(:endDate AS DATE))
    """)
    long countTransactions(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate
    );

    @Query("""
    SELECT 
        COUNT(*) AS total_transactions,
        COALESCE(SUM(amount), 0) AS total_amount,
        MIN(currency) AS currency
    FROM operators.transaction_master
    WHERE merchant_id = :merchantId
      AND (:startDate IS NULL OR txn_date >= CAST(:startDate AS DATE))
      AND (:endDate IS NULL OR txn_date <= CAST(:endDate AS DATE))
""")
    TransactionSummaryAggregation fetchSummaryAggregation(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate
    );

    @Query("""
    SELECT 
        SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END) AS completed,
        SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) AS pending,
        SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END) AS failed
    FROM operators.transaction_master
    WHERE merchant_id = :merchantId
      AND (:startDate IS NULL OR txn_date >= CAST(:startDate AS DATE))
      AND (:endDate IS NULL OR txn_date <= CAST(:endDate AS DATE))
""")
    StatusBreakdown fetchStatusBreakdown(
            String merchantId,
            @Nullable String startDate,
            @Nullable String endDate
    );

    @Query("""
              SELECT 
                 td.txn_detail_id AS detail_id,
                 td.master_txn_id AS master_txn_id,
                 td.detail_type AS type,
                 td.amount AS amount,
                 td.description AS description
              FROM operators.transaction_details td
              WHERE td.master_txn_id IN (:txnIds)
            """)
    List<TransactionDetailRow> findDetailsForTransactions(List<Long> txnIds);
}

