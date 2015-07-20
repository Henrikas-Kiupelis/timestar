package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.PaymentData;
import com.superum.db.lesson.table.core.TeacherLessonData;
import com.superum.exception.DatabaseException;
import com.superum.utils.ConditionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.*;
import static com.superum.utils.DateUtils.*;

@Repository
@Transactional
public class LessonTableQueriesImpl implements LessonTableQueries {

	@Override
	public TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, Date start, Date end, int partitionId) {
		Condition condition = LESSON.TEACHER_ID.eq(teacherId)
				.and(STUDENT.CUSTOMER_ID.eq(customerId))
				.and(LESSON.PARTITION_ID.eq(partitionId));
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		if (dateCondition != null)
			condition = condition.and(dateCondition);
		
		Result<Record3<Long,Short,BigDecimal>> result = sql.select(LESSON.ID, LESSON.LENGTH_IN_MINUTES, CUSTOMER.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER.ID))
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
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return new TeacherLessonData(lessonIds, duration, cost);
	}
	
	@Override
	public PaymentData countPriceForTeacher(int teacherId, int partitionId) {
		byte paymentDay = sql.select(TEACHER.PAYMENT_DAY)
				.from(TEACHER)
				.where(TEACHER.ID.eq(teacherId)
						.and(TEACHER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(TEACHER.PAYMENT_DAY))
				.orElseThrow(() -> new DatabaseException("Couldn't find teacher with id " + teacherId));
		
		Calendar init = initialDay(paymentDay);
		Date start = sqlDate(init);
		Date end = sqlDate(finalDay(init, paymentDay));
		
		BigDecimal cost = sql.select(CUSTOMER.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER.ID))
				.where(LESSON.TEACHER_ID.eq(teacherId)
						.and(LESSON.PARTITION_ID.eq(partitionId))
						.and(LESSON.DATE_OF_LESSON.ge(start)
						.and(LESSON.DATE_OF_LESSON.lessThan(end))))
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch()
				.getValues("cost").stream()
				.map(object -> (BigDecimal) object)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return new PaymentData(end, cost);
	}

	@Override
	public PaymentData countPriceForCustomer(int customerId, int partitionId) {
		byte paymentDay = sql.select(CUSTOMER.PAYMENT_DAY)
				.from(CUSTOMER)
				.where(CUSTOMER.ID.eq(customerId)
						.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(CUSTOMER.PAYMENT_DAY))
				.orElseThrow(() -> new DatabaseException("Couldn't find customer with id " + customerId));
		
		Calendar init = initialDay(paymentDay);
		Date start = sqlDate(init);
		Date end = sqlDate(finalDay(init, paymentDay));
		
		BigDecimal cost = sql.select(CUSTOMER.PAYMENT_VALUE.mul(LESSON.LENGTH_IN_MINUTES).div(60).as("Cost"))
				.from(LESSON)
				.join(STUDENT)
				.on(LESSON.GROUP_ID.eq(STUDENT.GROUP_ID))
				.join(CUSTOMER)
				.on(STUDENT.CUSTOMER_ID.eq(CUSTOMER.ID))
				.where(STUDENT.CUSTOMER_ID.eq(customerId)
						.and(LESSON.PARTITION_ID.eq(partitionId))
						.and(LESSON.DATE_OF_LESSON.ge(start)
						.and(LESSON.DATE_OF_LESSON.lessThan(end))))
				.groupBy(LESSON.ID)
				.orderBy(LESSON.ID)
				.fetch()
				.getValues("Cost").stream()
				.map(object -> (BigDecimal) object)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
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
