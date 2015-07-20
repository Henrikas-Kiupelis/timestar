package com.superum.db.customer;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
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
		return sql.insertInto(CUSTOMER)
				.set(CUSTOMER.PARTITION_ID, partitionId)
				.set(CUSTOMER.NAME, customer.getName())
				.set(CUSTOMER.PAYMENT_DAY, customer.getPaymentDay())
				.set(CUSTOMER.START_DATE, customer.getStartDate())
				.set(CUSTOMER.PAYMENT_VALUE, customer.getPaymentValue())
				.set(CUSTOMER.PHONE, customer.getPhone())
				.set(CUSTOMER.WEBSITE, customer.getWebsite())
				.set(CUSTOMER.PICTURE_NAME, customer.getPictureName())
				.set(CUSTOMER.COMMENT_ABOUT, customer.getComment())
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Customer::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert customer: " + customer));
	}

	@Override
	public Customer read(Integer id, int partitionId) {
		return sql.selectFrom(CUSTOMER)
				.where(CUSTOMER.ID.eq(id)
						.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(Customer::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find customer with ID: " + id));
	}

	@Override
	public Customer update(Customer customer, int partitionId) {
		int id = customer.getId();
		
		Customer old = read(id, partitionId);
		
		sql.update(CUSTOMER)
			.set(CUSTOMER.NAME, customer.getName())
			.set(CUSTOMER.PAYMENT_DAY, customer.getPaymentDay())
			.set(CUSTOMER.START_DATE, customer.getStartDate())
			.set(CUSTOMER.PAYMENT_VALUE, customer.getPaymentValue())
			.set(CUSTOMER.PHONE, customer.getPhone())
			.set(CUSTOMER.WEBSITE, customer.getWebsite())
			.set(CUSTOMER.PICTURE_NAME, customer.getPictureName())
			.set(CUSTOMER.COMMENT_ABOUT, customer.getComment())
			.where(CUSTOMER.ID.eq(id)
					.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
			.execute();
		
		return old;
	}

	@Override
	public Customer delete(Integer id, int partitionId) {
		Customer old = read(id, partitionId);
		
		int result = sql.delete(CUSTOMER)
				.where(CUSTOMER.ID.eq(id)
						.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete customer with ID: " + id);
		
		return old;
	}
	
	@Override
	public List<Customer> readAll(int partitionId) {
		return sql.selectFrom(CUSTOMER)
				.where(CUSTOMER.PARTITION_ID.eq(partitionId))
				.orderBy(CUSTOMER.ID)
				.fetch()
				.map(Customer::valueOf);
	}

	// CONSTRUCTORS
	
	@Autowired
	public CustomerDAOImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
