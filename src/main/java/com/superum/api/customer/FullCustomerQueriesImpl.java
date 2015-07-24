package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FullCustomerQueriesImpl implements FullCustomerQueries {

    @Override
    public int exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        return 0;
    }

    @Override
    public FullCustomer updatePartial(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        return null;
    }

    @Override
    public FullCustomer safeDelete(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        return null;
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        return 0;
    }

    @Override
    public int count(int partitionId) {


        return 0;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
