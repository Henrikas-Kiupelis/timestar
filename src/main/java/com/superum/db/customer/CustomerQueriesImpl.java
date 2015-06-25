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
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class CustomerQueriesImpl implements CustomerQueries {

	@Override
	public List<Customer> readAllForTeacher(int teacherId) {
		return sql.select(CUSTOMER.fields())
				.from(CUSTOMER)
				.join(STUDENT).onKey(STUDENT_IBFK_2)
				.join(STUDENT_GROUP).onKey(STUDENT_IBFK_1)
				.where(STUDENT_GROUP.TEACHER_ID.eq(teacherId))
				.groupBy(CUSTOMER.ID)
				.orderBy(CUSTOMER.ID)
				.fetch()
				.map(Customer::valueOf);
	}
	
	@Override
	public List<Customer> readAllForLessons(Date start, Date end) {
		SelectJoinStep<Record> step1 = sql.select(CUSTOMER.fields())
				.from(CUSTOMER)
				.join(STUDENT).onKey(STUDENT_IBFK_2)
				.join(LESSON)
				.on(STUDENT.GROUP_ID.eq(LESSON.GROUP_ID));
		
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		SelectHavingStep<Record> step2 = dateCondition == null
				? step1.groupBy(CUSTOMER.ID)
				: step1.where(dateCondition).groupBy(CUSTOMER.ID);
		
		return step2.orderBy(CUSTOMER.ID).fetch().map(Customer::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
