package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Keys.STUDENT_GROUP_IBFK_1;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomerQueriesImpl implements CustomerQueries {

	@Override
	public List<Customer> readAllForTeacher(int teacherId) {
		return sql.select(CUSTOMER.fields())
				.from(CUSTOMER)
				.join(STUDENT_GROUP).onKey(STUDENT_GROUP_IBFK_1)
				.where(STUDENT_GROUP.TEACHER_ID.eq(teacherId))
				.groupBy(CUSTOMER.ID)
				.fetch()
				.map(Customer::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
