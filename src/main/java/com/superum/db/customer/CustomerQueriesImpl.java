package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_2;
import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_1;

import java.sql.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class CustomerQueriesImpl implements CustomerQueries {

	@Override
	public List<Customer> readAllForTeacher(int teacherId, int partitionId) {
		return sql.select(CUSTOMER.fields())
				.from(CUSTOMER)
				.join(STUDENT).onKey(STUDENT_IBFK_2)
				.join(STUDENT_GROUP).onKey(STUDENT_IBFK_1)
				.where(STUDENT_GROUP.TEACHER_ID.eq(teacherId)
						.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
				.groupBy(CUSTOMER.ID)
				.orderBy(CUSTOMER.ID)
				.fetch()
				.map(Customer::valueOf);
	}
	
	@Override
	public List<Customer> readAllForLessons(Date start, Date end, int partitionId) {
		SelectJoinStep<Record> step = sql.select(CUSTOMER.fields())
				.from(CUSTOMER)
				.join(STUDENT).onKey(STUDENT_IBFK_2)
				.join(LESSON)
				.on(STUDENT.GROUP_ID.eq(LESSON.GROUP_ID));
		
		Condition condition = CUSTOMER.PARTITION_ID.eq(partitionId);
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		return step.where(condition)
				.groupBy(CUSTOMER.ID)
				.orderBy(CUSTOMER.ID)
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
