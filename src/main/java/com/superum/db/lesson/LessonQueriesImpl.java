package com.superum.db.lesson;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Tables.STUDENT;

import java.sql.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class LessonQueriesImpl implements LessonQueries {

	@Override
	public List<Lesson> readAllForCustomer(int customerId, Date start, Date end) {
		Condition condition = STUDENT.CUSTOMER_ID.eq(customerId);
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		return sql.select(LESSON.fields())
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.where(condition)
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch()
				.map(Lesson::valueOf);
	}

	// CONSTRUCTORS
	
	@Autowired
	public LessonQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;
	
}
