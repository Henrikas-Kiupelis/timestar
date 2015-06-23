package com.superum.db.customer.group.contract;

import static com.superum.db.generated.timestar.Tables.GROUP_CONTRACT;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Keys.GROUP_CONTRACT_IBFK_1;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GroupContractQueriesImpl implements GroupContractQueries {

	@Override
	public List<GroupContract> readAllForCustomer(int customerId) {
		return sql.select(GROUP_CONTRACT.fields())
				.from(GROUP_CONTRACT)
				.join(STUDENT_GROUP).onKey(GROUP_CONTRACT_IBFK_1)
				.where(STUDENT_GROUP.CUSTOMER_ID.eq(customerId))
				.groupBy(GROUP_CONTRACT.ID)
				.fetch()
				.map(GroupContract::valueOf);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupContractQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
