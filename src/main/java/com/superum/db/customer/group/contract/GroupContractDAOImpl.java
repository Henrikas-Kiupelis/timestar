package com.superum.db.customer.group.contract;

import static com.superum.db.generated.timestar.Tables.GROUP_CONTRACT;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class GroupContractDAOImpl implements GroupContractDAO {

	@Override
	public GroupContract create(GroupContract contract) {
		int groupId = contract.getGroupId();
		Date startDate = contract.getStartDate();
		String languageLevel = contract.getLanguageLevel();
		BigDecimal paymentValue = contract.getPaymentValue();
		
		return sql.insertInto(GROUP_CONTRACT)
				.set(GROUP_CONTRACT.GROUP_ID, groupId)
				.set(GROUP_CONTRACT.START_DATE, startDate)
				.set(GROUP_CONTRACT.LANGUAGE_LEVEL, languageLevel)
				.set(GROUP_CONTRACT.PAYMENT_VALUE, paymentValue)
				.returning()
				.fetch().stream()
				.findFirst()
				.map(GroupContract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't insert contract: " + contract));
	}

	@Override
	public GroupContract read(Integer id) {
		return sql.selectFrom(GROUP_CONTRACT)
				.where(GROUP_CONTRACT.ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(GroupContract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find contract with ID: " + id));
	}

	@Override
	public GroupContract update(GroupContract contract) {
		int id = contract.getId();
		int groupId = contract.getGroupId();
		Date startDate = contract.getStartDate();
		String languageLevel = contract.getLanguageLevel();
		BigDecimal paymentValue = contract.getPaymentValue();

		GroupContract old = read(id);
		
		sql.update(GROUP_CONTRACT)
			.set(GROUP_CONTRACT.GROUP_ID, groupId)
			.set(GROUP_CONTRACT.START_DATE, startDate)
			.set(GROUP_CONTRACT.LANGUAGE_LEVEL, languageLevel)
			.set(GROUP_CONTRACT.PAYMENT_VALUE, paymentValue)
			.where(GROUP_CONTRACT.ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public GroupContract delete(Integer id) {
		GroupContract old = read(id);
		
		int result = sql.delete(GROUP_CONTRACT)
				.where(GROUP_CONTRACT.ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete contract with ID: " + id);
		
		return old;
	}

	@Override
	public List<GroupContract> readAllForGroup(int groupId) {
		return sql.selectFrom(GROUP_CONTRACT)
				.where(GROUP_CONTRACT.GROUP_ID.eq(groupId))
				.fetch()
				.map(GroupContract::valueOf);
	}

	

	// CONSTRUCTORS

	@Autowired
	public GroupContractDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
