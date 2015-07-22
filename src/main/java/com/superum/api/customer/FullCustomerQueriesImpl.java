package com.superum.api.customer;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FullCustomerQueriesImpl implements FullCustomerQueries {

    @Override
    public boolean exists(FullCustomer customer, int partitionId) {
        return false;
    }

    @Override
    public FullCustomer updatePartial(FullCustomer customer, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer safeDelete(int customerId, int partitionId) {
        return null;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
