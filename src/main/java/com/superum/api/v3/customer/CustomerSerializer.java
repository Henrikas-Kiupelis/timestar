package com.superum.api.v3.customer;

import com.superum.api.v3.customer.dto.FetchedCustomer;
import org.jooq.Record;

public interface CustomerSerializer {

    FetchedCustomer toReturnable(Record record);

}
