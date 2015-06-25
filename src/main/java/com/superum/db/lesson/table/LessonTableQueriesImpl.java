package com.superum.db.lesson.table;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.TEACHER_CONTRACT;
import static com.superum.db.generated.timestar.Tables.CUSTOMER_CONTRACT;
import static com.superum.utils.DateUtils.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;
import com.superum.db.lesson.table.core.PaymentData;
import com.superum.db.lesson.table.core.TeacherLessonData;
import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class LessonTableQueriesImpl implements LessonTableQueries {

	@Override
	public TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, Date start, Date end) {
		Condition condition = LESSON.TEACHER_ID.eq(teacherId).and(STUDENT.CUSTOMER_ID.eq(customerId));
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		Result<Record3<Long,Short,BigDecimal>> result = sql.select(LESSON.ID, LESSON.LENGTH_IN_MINUTES, CUSTOMER_CONTRACT.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER_CONTRACT)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER_CONTRACT.CUSTOMER_ID))
				.where(condition)
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch();
		
		if (result.size() == 0)
			return new TeacherLessonData(Collections.emptyList(), 0, BigDecimal.ZERO);
		
		List<Long> lessonIds = result.getValues(LESSON.ID);
		int duration = result.getValues(LESSON.LENGTH_IN_MINUTES).stream()
				.mapToInt(dur -> (int)dur)
				.reduce(0, (dur1, dur2) -> dur1 + dur2);
		
		BigDecimal cost = result.getValues("cost").stream()
				.map(cst -> (BigDecimal)cst)
				.reduce(BigDecimal.ZERO, (cost1, cost2) -> cost1.add(cost2));
		
		return new TeacherLessonData(lessonIds, duration, cost);
	}
	
	@Override
	public PaymentData countPriceForTeacher(int teacherId) {
		byte paymentDay = sql.select(TEACHER_CONTRACT.PAYMENT_DAY)
				.from(TEACHER_CONTRACT)
				.where(TEACHER_CONTRACT.TEACHER_ID.eq(teacherId))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(TEACHER_CONTRACT.PAYMENT_DAY))
				.orElseThrow(() -> new DatabaseException("Couldn't find teacher with id " + teacherId));
		
		Calendar init = initialDay(paymentDay);
		Date start = sqlDate(init);
		Date end = sqlDate(finalDay(init, paymentDay));
		
		BigDecimal cost = sql.select(CUSTOMER_CONTRACT.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER_CONTRACT)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER_CONTRACT.CUSTOMER_ID))
				.where(LESSON.TEACHER_ID.eq(teacherId)
						.and(LESSON.DATE_OF_LESSON.ge(start)
						.and(LESSON.DATE_OF_LESSON.lessThan(end))))
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch()
				.getValues("cost").stream()
				.map(object -> (BigDecimal) object)
				.reduce(BigDecimal.ZERO, (cost1, cost2) -> cost1.add(cost2));
		
		return new PaymentData(end, cost);
	}

	@Override
	public PaymentData countPriceForCustomer(int customerId) {
		byte paymentDay = sql.select(CUSTOMER_CONTRACT.PAYMENT_DAY)
				.from(CUSTOMER_CONTRACT)
				.where(CUSTOMER_CONTRACT.CUSTOMER_ID.eq(customerId))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(CUSTOMER_CONTRACT.PAYMENT_DAY))
				.orElseThrow(() -> new DatabaseException("Couldn't find customer with id " + customerId));
		
		Calendar init = initialDay(paymentDay);
		Date start = sqlDate(init);
		Date end = sqlDate(finalDay(init, paymentDay));
		
		BigDecimal cost = sql.select(CUSTOMER_CONTRACT.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("Cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER_CONTRACT)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER_CONTRACT.CUSTOMER_ID))
				.where(STUDENT.CUSTOMER_ID.eq(customerId)
						.and(LESSON.DATE_OF_LESSON.ge(start)
						.and(LESSON.DATE_OF_LESSON.lessThan(end))))
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch()
				.getValues("Cost").stream()
				.map(object -> (BigDecimal) object)
				.reduce(BigDecimal.ZERO, (cost1, cost2) -> cost1.add(cost2));
		
		return new PaymentData(end, cost);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
