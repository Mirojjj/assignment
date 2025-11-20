package com.payment.repository;

import com.payment.entity.TransactionMaster;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

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

    // TODO: Add your custom query methods here
    // Examples:
    // - Page<TransactionMaster> findByMerchantIdAndTxnDateBetween(...)
    // - TransactionSummary calculateSummary(...)
    // - List<TransactionWithDetails> findTransactionsWithDetails(...)
}
