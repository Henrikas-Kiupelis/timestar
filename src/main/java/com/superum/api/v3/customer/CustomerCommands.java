package com.superum.api.v3.customer;

import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.api.v3.customer.dto.SuppliedCustomer;

public interface CustomerCommands {

    FetchedCustomer create(SuppliedCustomer suppliedCustomer);

    void update(SuppliedCustomer suppliedCustomer, int id);

    void delete(int id);

}
