package com.superum.api.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullCustomerServiceImpl implements FullCustomerService{

    @Override
    public FullCustomer insert(FullCustomer customer, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer read(int customerId, int partitionId) {
        return null;
    }

    @Override
    public FullCustomer delete(int customerId, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> readAllForTeacher(int teacherId, int partitionId) {
        return null;
    }

    @Override
    public List<FullCustomer> all(int page, int amount, int partitionId) {
        return null;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerServiceImpl(FullCustomerQueries fullCustomerQueries) {
        this.fullCustomerQueries = fullCustomerQueries;
    }

    // PRIVATE

    private final FullCustomerQueries fullCustomerQueries;

}
