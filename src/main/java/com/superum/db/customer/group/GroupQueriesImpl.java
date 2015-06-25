package com.superum.db.customer.group;

import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_1;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GroupQueriesImpl implements GroupQueries {

	@Override
	public List<Group> readAllForCustomer(int customerId) {
		return sql.select(STUDENT_GROUP.fields())
				.from(STUDENT_GROUP)
				.join(STUDENT).onKey(STUDENT_IBFK_1)
				.where(STUDENT.CUSTOMER_ID.eq(customerId))
				.groupBy(STUDENT_GROUP.ID)
				.orderBy(STUDENT_GROUP.ID)
				.fetch()
				.map(Group::valueOf);
	}

	@Override
	public List<Group> readAllForCustomerAndTeacher(int customerId, int teacherId) {
		return sql.select(STUDENT_GROUP.fields())
				.from(STUDENT_GROUP)
				.join(STUDENT).onKey(STUDENT_IBFK_1)
				.where(STUDENT.CUSTOMER_ID.eq(customerId)
						.and(STUDENT_GROUP.TEACHER_ID.eq(teacherId)))
				.groupBy(STUDENT_GROUP.ID)
				.orderBy(STUDENT_GROUP.ID)
				.fetch()
				.map(Group::valueOf);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

}
