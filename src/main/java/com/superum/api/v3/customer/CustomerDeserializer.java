package com.superum.api.v3.customer;

import com.superum.api.v3.customer.dto.SuppliedCustomer;

public interface CustomerDeserializer {

    Customer toCreatable(SuppliedCustomer suppliedCustomer);

    Customer toUpdatable(SuppliedCustomer suppliedCustomer, int id);

}
