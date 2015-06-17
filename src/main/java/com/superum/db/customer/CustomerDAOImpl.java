package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public Customer create(Customer customer) {
		String name = customer.getName();
		String phone = customer.getPhone();
		String comment = customer.getComment();

		return sql.insertInto(CUSTOMER)
				.set(CUSTOMER.NAME, name)
				.set(CUSTOMER.PHONE, phone)
				.set(CUSTOMER.COMMENT_ABOUT, comment)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Customer::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert customer: " + customer));
	}

	@Override
	public Customer read(Integer id) {
		return sql.selectFrom(CUSTOMER)
				.where(CUSTOMER.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Customer::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find customer with ID: " + id));
	}

	@Override
	public Customer update(Customer customer) {
		int id = customer.getId();
		String name = customer.getName();
		String phone = customer.getPhone();
		String comment = customer.getComment();
		
		Customer old = read(id);
		
		sql.update(CUSTOMER)
			.set(CUSTOMER.NAME, name)
			.set(CUSTOMER.PHONE, phone)
			.set(CUSTOMER.COMMENT_ABOUT, comment)
			.where(CUSTOMER.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Customer delete(Integer id) {
		Customer old = read(id);
		
		int result = sql.delete(CUSTOMER)
				.where(CUSTOMER.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete customer with ID: " + id);
		
		return old;
	}

	// CONSTRUCTORS
	
	@Autowired
	public CustomerDAOImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
