package com.superum.db.customer.contract;

import static com.superum.db.generated.timestar.Tables.CUSTOMER_CONTRACT;

import java.math.BigDecimal;
import java.sql.Date;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class CustomerContractDAOImpl implements CustomerContractDAO {

	@Override
	public CustomerContract create(CustomerContract contract) {
		int id = contract.getId();
		byte paymentDay = contract.getPaymentDay();
		Date startDate = contract.getStartDate();
		String languageLevel = contract.getLanguageLevel();
		BigDecimal paymentValue = contract.getPaymentValue();
		
		int createResult = sql.insertInto(CUSTOMER_CONTRACT)
				.set(CUSTOMER_CONTRACT.CUSTOMER_ID, id)
				.set(CUSTOMER_CONTRACT.PAYMENT_DAY, paymentDay)
				.set(CUSTOMER_CONTRACT.START_DATE, startDate)
				.set(CUSTOMER_CONTRACT.LANGUAGE_LEVEL, languageLevel)
				.set(CUSTOMER_CONTRACT.PAYMENT_VALUE, paymentValue)
				.execute();
		
		if (createResult == 0)
			throw new DatabaseException("Couldn't insert contract: " + contract);
		
		return new CustomerContract(id, paymentDay, startDate, languageLevel, paymentValue);
	}

	@Override
	public CustomerContract read(Integer id) {
		return sql.selectFrom(CUSTOMER_CONTRACT)
				.where(CUSTOMER_CONTRACT.CUSTOMER_ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(CustomerContract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find contract with ID: " + id));
	}

	@Override
	public CustomerContract update(CustomerContract contract) {
		int id = contract.getId();
		byte paymentDay = contract.getPaymentDay();
		Date startDate = contract.getStartDate();
		String languageLevel = contract.getLanguageLevel();
		BigDecimal paymentValue = contract.getPaymentValue();

		CustomerContract old = read(id);
		
		sql.update(CUSTOMER_CONTRACT)
			.set(CUSTOMER_CONTRACT.PAYMENT_DAY, paymentDay)
			.set(CUSTOMER_CONTRACT.START_DATE, startDate)
			.set(CUSTOMER_CONTRACT.LANGUAGE_LEVEL, languageLevel)
			.set(CUSTOMER_CONTRACT.PAYMENT_VALUE, paymentValue)
			.where(CUSTOMER_CONTRACT.CUSTOMER_ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public CustomerContract delete(Integer id) {
		CustomerContract old = read(id);
		
		int result = sql.delete(CUSTOMER_CONTRACT)
				.where(CUSTOMER_CONTRACT.CUSTOMER_ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete contract with ID: " + id);
		
		return old;
	}	

	// CONSTRUCTORS

	@Autowired
	public CustomerContractDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
