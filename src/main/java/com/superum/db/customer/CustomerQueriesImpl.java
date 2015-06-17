package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_2;

import java.util.List;
import java.util.stream.Collectors;

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
				.join(LESSON).onKey(LESSON_IBFK_2)
				.where(LESSON.TEACHER_ID.eq(teacherId))
				.groupBy(CUSTOMER.ID)
				.fetch().stream()
				.map(Customer::valueOf)
				.collect(Collectors.toList());
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
