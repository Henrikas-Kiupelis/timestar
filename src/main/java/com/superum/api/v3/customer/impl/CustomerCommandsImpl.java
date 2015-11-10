package com.superum.api.v3.customer.impl;

import com.superum.api.v2.customer.UnsafeCustomerDeleteException;
import com.superum.api.v3.customer.*;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.api.v3.customer.dto.SuppliedCustomer;
import com.superum.exception.DatabaseException;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.libraries.jooq.QueriesForeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.CustomerRecord;

@Service
@Transactional
public class CustomerCommandsImpl implements CustomerCommands {

    @Override
    public FetchedCustomer create(SuppliedCustomer suppliedCustomer) {
        Customer customer = customerDeserializer.toCreatable(suppliedCustomer);
        return customer.create()
                .orElseThrow(() -> new DatabaseException("Couldn't create customer: " + customer));
    }

    @Override
    public void update(SuppliedCustomer suppliedCustomer, int id) {
        Customer customer = customerDeserializer.toUpdatable(suppliedCustomer, id);
        if (customer.update(id) == 0)
            throw new DatabaseException("Couldn't update customer: " + customer);
    }

    @Override
    public void delete(int id) {
        if (!customerQueries.exists(id))
            throw CustomerErrors.customerIdError(id);

        if (customerForeignQueries.isUsed(id))
            throw new UnsafeCustomerDeleteException("Customer with id " + id + " still has entries in other tables!");

        if (customerRepository.delete(id) == 0)
            throw new DatabaseException("Couldn't update customer with id: " + id);
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerCommandsImpl(CustomerDeserializer customerDeserializer,
                                Queries<CustomerRecord, Integer> customerQueries,
                                QueriesForeign<Integer> customerForeignQueries,
                                CustomerRepository customerRepository) {
        this.customerDeserializer = customerDeserializer;
        this.customerQueries = customerQueries;
        this.customerForeignQueries = customerForeignQueries;
        this.customerRepository = customerRepository;
    }

    // PRIVATE

    private final CustomerDeserializer customerDeserializer;
    private final Queries<CustomerRecord, Integer> customerQueries;
    private final QueriesForeign<Integer> customerForeignQueries;
    private final CustomerRepository customerRepository;

}
