package com.superum.db.customer;

import com.superum.api.customer.CustomerNotFoundException;
import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;

@Repository
@Transactional
public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Customer create(Customer customer, int partitionId) {
        try {
            return sql.insertInto(CUSTOMER)
                    .set(CUSTOMER.PARTITION_ID, partitionId)
                    .set(CUSTOMER.NAME, customer.getName())
                    .set(CUSTOMER.START_DATE, customer.getStartDateSql())
                    .set(CUSTOMER.PHONE, customer.getPhone())
                    .set(CUSTOMER.WEBSITE, customer.getWebsite())
                    .set(CUSTOMER.PICTURE, customer.getPicture())
                    .set(CUSTOMER.COMMENT, customer.getComment())
                    .returning()
                    .fetch().stream()
                    .findFirst()
                    .map(Customer::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't insert customer: " + customer));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when inserting customer " + customer, e);
        }
    }

    @Override
    public Customer read(Integer id, int partitionId) {
        try {
            return sql.selectFrom(CUSTOMER)
                    .where(CUSTOMER.ID.eq(id)
                            .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                    .fetch().stream()
                    .findFirst()
                    .map(Customer::valueOf)
                    .orElseThrow(() -> new CustomerNotFoundException("Couldn't find customer with id: " + id));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read customer with id " + id, e);
        }
    }

    @Override
    public Customer update(Customer customer, int partitionId) {
        try {
            int id = customer.getId();

            Customer old = read(id, partitionId);

            sql.update(CUSTOMER)
                    .set(CUSTOMER.NAME, customer.getName())
                    .set(CUSTOMER.START_DATE, customer.getStartDateSql())
                    .set(CUSTOMER.PHONE, customer.getPhone())
                    .set(CUSTOMER.WEBSITE, customer.getWebsite())
                    .set(CUSTOMER.PICTURE, customer.getPicture())
                    .set(CUSTOMER.COMMENT, customer.getComment())
                    .where(CUSTOMER.ID.eq(id)
                            .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update customer " + customer, e);
        }
    }

    @Override
    public Customer delete(Integer id, int partitionId) {
        try {
            Customer old = read(id, partitionId);

            int result = sql.delete(CUSTOMER)
                    .where(CUSTOMER.ID.eq(id)
                            .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                    .execute();
            if (result == 0)
                throw new DatabaseException("Couldn't delete customer with ID: " + id);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete customer with id " + id, e);
        }
    }

    @Override
    public List<Customer> readAll(int partitionId) {
        try {
            return sql.selectFrom(CUSTOMER)
                    .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                    .orderBy(CUSTOMER.ID)
                    .fetch()
                    .map(Customer::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers", e);
        }
    }

    // CONSTRUCTORS

    @Autowired
    public CustomerDAOImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
