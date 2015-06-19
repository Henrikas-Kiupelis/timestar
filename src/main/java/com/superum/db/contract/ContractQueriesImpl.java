package com.superum.db.contract;

import static com.superum.db.generated.timestar.Tables.CONTRACT;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Keys.CONTRACT_IBFK_1;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ContractQueriesImpl implements ContractQueries {

	@Override
	public List<Contract> readAllForCustomer(int customerId) {
		return sql.select(CONTRACT.fields())
				.from(CONTRACT)
				.join(STUDENT_GROUP).onKey(CONTRACT_IBFK_1)
				.where(STUDENT_GROUP.CUSTOMER_ID.eq(customerId))
				.groupBy(CONTRACT.ID)
				.fetch()
				.map(Contract::valueOf);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public ContractQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
