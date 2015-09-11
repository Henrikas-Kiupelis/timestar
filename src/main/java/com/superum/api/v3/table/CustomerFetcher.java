package com.superum.api.v3.table;

import com.superum.api.v2.customer.ValidCustomerDTO;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static timestar_v2.Tables.CUSTOMER;
import static timestar_v2.Tables.GROUP_OF_STUDENTS;

@Repository
public class CustomerFetcher {

    /**
     * @return list of all customers; unlike the normal method call, there is no limit in this case; also, null
     * customer is attached to the tail of this list if and only if there are groups which do not have a customer
     */
    public List<ValidCustomerDTO> getAllCustomers(int partitionId) {
        List<ValidCustomerDTO> customers =  sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                .orderBy(CUSTOMER.ID)
                .fetch()
                .map(ValidCustomerDTO::valueOf);

        if (sql.fetchExists(GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.CUSTOMER_ID.isNull()))
            // "null" in this case means that there exist groups without a customer; they must be considered
            customers.add(null);

        return customers;
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerFetcher(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
