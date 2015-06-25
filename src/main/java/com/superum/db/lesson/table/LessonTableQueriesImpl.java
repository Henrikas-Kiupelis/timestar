package com.superum.db.lesson.table;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Tables.GROUP_CONTRACT;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.lesson.table.core.TeacherLessonData;
import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class LessonTableQueriesImpl implements LessonTableQueries {

	@Override
	public TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, Date start, Date end) {
		Condition condition = LESSON.TEACHER_ID.eq(teacherId).and(LESSON.CUSTOMER_ID.eq(customerId));
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		Result<Record3<Long,Short,BigDecimal>> result = sql.select(LESSON.ID, LESSON.TIME_OF_LESSON, GROUP_CONTRACT.PAYMENT_VALUE.mul(LESSON.TIME_OF_LESSON).div(60).as("Cost"))
				.from(LESSON)
				.join(GROUP_CONTRACT)
				.on(LESSON.GROUP_ID.eq(GROUP_CONTRACT.GROUP_ID))
				.where(condition)
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch();
		
		if (result.size() == 0)
			return new TeacherLessonData(Collections.emptyList(), 0, BigDecimal.ZERO);
		
		List<Long> lessonIds = result.getValues(LESSON.ID);
		int duration = result.getValues(LESSON.TIME_OF_LESSON).stream()
				.mapToInt(dur -> (int)dur)
				.reduce(0, (dur1, dur2) -> dur1 + dur2);
		
		BigDecimal cost = result.getValues("Cost").stream()
				.map(cst -> (BigDecimal)cst)
				.reduce(BigDecimal.ZERO, (cost1, cost2) -> cost1.add(cost2));
		
		return new TeacherLessonData(lessonIds, duration, cost);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
