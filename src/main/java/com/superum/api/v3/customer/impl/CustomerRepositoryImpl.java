package com.superum.api.v3.customer.impl;

import com.superum.api.v3.customer.CustomerRepository;
import com.superum.api.v3.customer.CustomerSerializer;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.helper.PartitionAccount;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static timestar_v2.Tables.CUSTOMER;

@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public Optional<FetchedCustomer> create(long createdAt, long updatedAt, java.sql.Date startDate, String name,
                                            String phone, String website, String picture, String comment) {
        return sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, new PartitionAccount().partitionId())
                .set(CUSTOMER.CREATED_AT, createdAt)
                .set(CUSTOMER.UPDATED_AT, updatedAt)
                .set(CUSTOMER.START_DATE, startDate)
                .set(CUSTOMER.NAME, name)
                .set(CUSTOMER.PHONE, phone)
                .set(CUSTOMER.WEBSITE, website)
                .set(CUSTOMER.PICTURE, picture)
                .set(CUSTOMER.COMMENT, comment)
                .returning()
                .fetch().stream().findAny()
                .map(customerSerializer::toReturnable);
    }

    @Override
    public int update(long updatedAt, java.sql.Date startDate, String name, String phone, String website,
                      String picture, String comment, int id) {
        return sql.update(CUSTOMER)
                .set(CUSTOMER.UPDATED_AT, updatedAt)
                .set(CUSTOMER.START_DATE, startDate)
                .set(CUSTOMER.NAME, name)
                .set(CUSTOMER.PHONE, phone)
                .set(CUSTOMER.WEBSITE, website)
                .set(CUSTOMER.PICTURE, picture)
                .set(CUSTOMER.COMMENT, comment)
                .where(primaryKey(id))
                .execute();
    }

    @Override
    public int delete(int id) {
        return sql.deleteFrom(CUSTOMER)
                .where(primaryKey(id))
                .execute();
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerRepositoryImpl(DSLContext sql, CustomerSerializer customerSerializer) {
        this.sql = sql;
        this.customerSerializer = customerSerializer;
    }

    // PRIVATE

    private final DSLContext sql;
    private final CustomerSerializer customerSerializer;

    private Condition primaryKey(int id) {
        return CUSTOMER.ID.eq(id).and(CUSTOMER.PARTITION_ID.eq(new PartitionAccount().partitionId()));
    }

}
