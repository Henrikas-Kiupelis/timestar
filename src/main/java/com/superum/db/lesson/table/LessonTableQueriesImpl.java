package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.CustomerLanguages;
import com.superum.db.lesson.table.core.PaymentData;
import com.superum.db.lesson.table.core.TeacherLessonData;
import com.superum.exception.DatabaseException;
import com.superum.helper.time.TimeResolver;
import eu.goodlike.time.JodaTimeZoneHandler;
import org.joda.time.LocalDate;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_1;
import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_2;
import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class LessonTableQueriesImpl implements LessonTableQueries {

	@Override
	public TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, long start, long end, int partitionId) {
		Condition condition = LESSON.TEACHER_ID.eq(teacherId)
				.and(GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId))
				.and(LESSON.PARTITION_ID.eq(partitionId))
				.and(LESSON.TIME_OF_START.between(start, end));
		
		Result<Record3<Long,Integer,BigDecimal>> result = sql.select(LESSON.ID, LESSON.DURATION_IN_MINUTES, TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).as("cost"))
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(LESSON.ID)
                .orderBy(LESSON.ID)
                .fetch();
		if (result.size() == 0)
			return new TeacherLessonData(Collections.emptyList(), 0, BigDecimal.ZERO);
		
		List<Long> lessonIds = result.getValues(LESSON.ID);

		int duration = result.getValues(LESSON.DURATION_IN_MINUTES).stream()
                .mapToInt(Integer::intValue)
				.reduce(0, Integer::sum);
		
		BigDecimal cost = result.getValues("cost").stream()
				.map(cst -> (BigDecimal)cst)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return new TeacherLessonData(lessonIds, duration, cost.divide(BigDecimal.valueOf(60), 4, BigDecimal.ROUND_HALF_EVEN));
	}
	
	@Override
	public PaymentData countPriceForTeacher(int teacherId, int partitionId) {
		int paymentDay = sql.select(TEACHER.PAYMENT_DAY)
				.from(TEACHER)
				.where(TEACHER.ID.eq(teacherId)
						.and(TEACHER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(TEACHER.PAYMENT_DAY))
				.orElseThrow(() -> new DatabaseException("Couldn't find teacher with id " + teacherId));

        TimeResolver timeResolver = TimeResolver.from(paymentDay);
        Condition condition = LESSON.TEACHER_ID.eq(teacherId)
                .and(LESSON.PARTITION_ID.eq(partitionId))
                .and(timeResolver.isBetween(LESSON.TIME_OF_START));
		
		BigDecimal cost = sql.select(TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).as("cost"))
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .where(condition)
                .groupBy(LESSON.ID)
                .orderBy(LESSON.ID)
                .fetch()
                .getValues("cost").stream()
                .map(object -> (BigDecimal) object)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate endDate = JodaTimeZoneHandler.getDefault().from(timeResolver.getEndTime()).toOrgJodaTimeLocalDate().minusDays(1);
		return new PaymentData(endDate, cost.divide(BigDecimal.valueOf(60), 4, BigDecimal.ROUND_HALF_EVEN));
	}

	@Override
	public PaymentData countPriceForCustomer(int customerId, int partitionId) {
		Date contractStartDate = sql.select(CUSTOMER.START_DATE)
				.from(CUSTOMER)
				.where(CUSTOMER.ID.eq(customerId)
						.and(CUSTOMER.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(CUSTOMER.START_DATE))
				.orElseThrow(() -> new DatabaseException("Couldn't find customer with id " + customerId));

        LocalDate contractDate = JodaTimeZoneHandler.getDefault().from(contractStartDate).toOrgJodaTimeLocalDate().minusDays(1);
        TimeResolver timeResolver = TimeResolver.from(contractDate);
        Condition condition = GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId)
                .and(LESSON.PARTITION_ID.eq(partitionId))
                .and(timeResolver.isBetween(LESSON.TIME_OF_START));

		BigDecimal cost = sql.select(TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).as("Cost"))
				.from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(LESSON.ID)
                .orderBy(LESSON.ID)
                .fetch()
                .getValues("Cost").stream()
                .map(object -> (BigDecimal) object)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate endDate = JodaTimeZoneHandler.getDefault().from(timeResolver.getEndTime()).toOrgJodaTimeLocalDate();
		return new PaymentData(endDate, cost.divide(BigDecimal.valueOf(60), 4, BigDecimal.ROUND_HALF_EVEN));
	}

	@Override
	public CustomerLanguages getCustomerLanguages(int id, int partitionId) {
        List<String> languages = sql.select(GROUP_OF_STUDENTS.LANGUAGE_LEVEL)
                .from(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.CUSTOMER_ID.eq(id))
                .fetch()
                .map(record -> record.getValue(GROUP_OF_STUDENTS.LANGUAGE_LEVEL));
		return new CustomerLanguages(id, languages);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
