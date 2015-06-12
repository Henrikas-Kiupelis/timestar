package com.superum.db.contract;

import static com.superum.db.generated.timestar.Tables.CONTRACT;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class ContractDAOImpl implements ContractDAO {

	@Override
	public Contract create(Contract contract) {
		int teacherId = contract.getTeacherId();
		int customerId = contract.getCustomerId();
		byte paymentDay = contract.getPaymentDay();
		BigDecimal paymentValue = contract.getPaymentValue();
		
		return sql.insertInto(CONTRACT)
				.set(CONTRACT.TEACHER_ID, teacherId)
				.set(CONTRACT.CUSTOMER_ID, customerId)
				.set(CONTRACT.PAYMENT_DAY, paymentDay)
				.set(CONTRACT.PAYMENT_VALUE, paymentValue)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(Contract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert contract: " + contract));
	}

	@Override
	public Contract read(Integer id) {
		return sql.selectFrom(CONTRACT)
				.where(CONTRACT.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(Contract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find contract with ID: " + id));
	}

	@Override
	public Contract update(Contract contract) {
		int id = contract.getId();
		int teacherId = contract.getTeacherId();
		int customerId = contract.getCustomerId();
		byte paymentDay = contract.getPaymentDay();
		BigDecimal paymentValue = contract.getPaymentValue();

		Contract old = read(id);
		
		sql.update(CONTRACT)
			.set(CONTRACT.TEACHER_ID, teacherId)
			.set(CONTRACT.CUSTOMER_ID, customerId)
			.set(CONTRACT.PAYMENT_DAY, paymentDay)
			.set(CONTRACT.PAYMENT_VALUE, paymentValue)
			.where(CONTRACT.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public Contract delete(Integer id) {
		Contract old = read(id);
		
		int result = sql.delete(CONTRACT)
				.where(CONTRACT.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete contract with ID: " + id);
		
		return old;
	}

	@Override
	public Contract readForTeacherAndCustomer(int teacherId, int customerId) {
		return sql.selectFrom(CONTRACT)
				.where(CONTRACT.TEACHER_ID.eq(teacherId)
					.and(CONTRACT.CUSTOMER_ID.eq(customerId)))
				.fetch().stream()
				.findFirst()
				.map(Contract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find contract for this teacher/customer ID pair: " + teacherId + "/" + customerId));
	}

	@Override
	public List<Contract> readAllForTeacher(int teacherId) {
		return sql.selectFrom(CONTRACT)
				.where(CONTRACT.TEACHER_ID.eq(teacherId))
				.fetch().stream()
				.map(Contract::valueOf)
				.collect(Collectors.toList());
	}

	@Override
	public List<Contract> readAllForCustomer(int customerId) {
		return sql.selectFrom(CONTRACT)
				.where(CONTRACT.CUSTOMER_ID.eq(customerId))
				.fetch().stream()
				.map(Contract::valueOf)
				.collect(Collectors.toList());
	}

	// CONSTRUCTORS

	@Autowired
	public ContractDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
