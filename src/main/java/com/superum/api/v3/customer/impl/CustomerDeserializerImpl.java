package com.superum.api.v3.customer.impl;

import com.superum.api.v3.customer.*;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.api.v3.customer.dto.SuppliedCustomer;
import eu.goodlike.libraries.jooq.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.CustomerRecord;

import java.time.Instant;
import java.time.LocalDate;

@Service
public class CustomerDeserializerImpl implements CustomerDeserializer {

    @Override
    public Customer toCreatable(SuppliedCustomer suppliedCustomer) {
        suppliedCustomer.validateForCreation();

        String startDateStr = suppliedCustomer.getStartDate();
        String name = suppliedCustomer.getName();
        String phone = suppliedCustomer.getPhone();
        String website = suppliedCustomer.getWebsite();
        String picture = suppliedCustomer.getPicture();
        String comment = suppliedCustomer.getComment();

        LocalDate startDate = startDateStr == null ? null : LocalDate.parse(startDateStr);
        Instant now = Instant.now();
        return Customer.valueOf(now, now, startDate, name, phone, website, picture, comment, customerRepository);
    }

    @Override
    public Customer toUpdatable(SuppliedCustomer suppliedCustomer, int id) {
        suppliedCustomer.validateForUpdating();

        FetchedCustomer fetchedCustomer = customerQueries.read(id, customerSerializer::toReturnable)
                .orElseThrow(() -> CustomerErrors.customerIdError(id));

        String startDateStr = suppliedCustomer.getStartDate();
        String name = suppliedCustomer.getName();
        String phone = suppliedCustomer.getPhone();
        String website = suppliedCustomer.getWebsite();
        String picture = suppliedCustomer.getPicture();
        String comment = suppliedCustomer.getComment();

        if (startDateStr == null) startDateStr = fetchedCustomer.getStartDate();
        if (name == null) name = fetchedCustomer.getName();
        if (phone == null) phone = fetchedCustomer.getPhone();
        if (website == null) website = fetchedCustomer.getWebsite();
        if (picture == null) picture = fetchedCustomer.getPicture();
        if (comment == null) comment = fetchedCustomer.getComment();

        LocalDate startDate = startDateStr == null ? null : LocalDate.parse(startDateStr);
        Instant createdAt = Instant.ofEpochMilli(fetchedCustomer.getCreatedAt());
        Instant updatedAt = Instant.now();
        return Customer.valueOf(createdAt, updatedAt, startDate, name, phone, website, picture, comment, customerRepository);
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerDeserializerImpl(Queries<CustomerRecord, Integer> customerQueries,
                                    CustomerSerializer customerSerializer, CustomerRepository customerRepository) {
        this.customerQueries = customerQueries;
        this.customerSerializer = customerSerializer;
        this.customerRepository = customerRepository;
    }

    // PRIVATE

    private final Queries<CustomerRecord, Integer> customerQueries;
    private final CustomerSerializer customerSerializer;
    private final CustomerRepository customerRepository;

}
