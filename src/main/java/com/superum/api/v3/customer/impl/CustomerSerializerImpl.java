package com.superum.api.v3.customer.impl;

import com.superum.api.v3.customer.CustomerSerializer;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import org.jooq.Record;
import org.springframework.stereotype.Service;

import static timestar_v2.Tables.CUSTOMER;

@Service
public class CustomerSerializerImpl implements CustomerSerializer {

    @Override
    public FetchedCustomer toReturnable(Record record) {
        if (record == null)
            return null;

        int id = record.getValue(CUSTOMER.ID);
        java.sql.Date startDateSQL = record.getValue(CUSTOMER.START_DATE);
        String startDate = startDateSQL == null ? null : startDateSQL.toString();
        String name = record.getValue(CUSTOMER.NAME);
        String phone = record.getValue(CUSTOMER.PHONE);
        String website = record.getValue(CUSTOMER.WEBSITE);
        String picture = record.getValue(CUSTOMER.PICTURE);
        String comment = record.getValue(CUSTOMER.COMMENT);
        long createdAt = record.getValue(CUSTOMER.CREATED_AT);
        long updatedAt = record.getValue(CUSTOMER.UPDATED_AT);
        return new FetchedCustomer(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }

}
