package com.superum.api.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullCustomerServiceImpl implements FullCustomerService {

    @Override
    public FullCustomer createCustomer(FullCustomer customer, int partitionId) {
        return customer.withId(1);
    }

    @Override
    public FullCustomer readCustomer(int customerId, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer updateCustomer(FullCustomer customer, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer deleteCustomer(int customerId, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        return null;
    }

    @Override
    public int count(int partitionId) {
        return 0;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerServiceImpl(FullCustomerQueries fullCustomerQueries) {
        this.fullCustomerQueries = fullCustomerQueries;
    }

    // PRIVATE

    private final FullCustomerQueries fullCustomerQueries;

}
