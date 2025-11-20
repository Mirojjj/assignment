package com.payment.repository;

import com.payment.entity.TransactionDetail;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for TransactionDetail entities.
 */
@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TransactionDetailRepository extends CrudRepository<TransactionDetail, Long> {

    List<TransactionDetail> findByMasterTxnId(Long masterTxnId);

    List<TransactionDetail> findByMasterTxnIdInList(List<Long> masterTxnIds);
}
