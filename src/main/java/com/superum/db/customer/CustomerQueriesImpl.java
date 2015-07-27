package com.superum.db.customer;

import com.superum.exception.DatabaseException;
import com.superum.utils.ConditionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_1;
import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_2;
import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class CustomerQueriesImpl implements CustomerQueries {

	@Override
	public List<Customer> readAllForTeacher(int teacherId, int partitionId) {
		try {
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
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers for teacher with id " + teacherId, e);
        }
	}
	
	@Override
	public List<Customer> readAllForLessons(Date start, Date end, int partitionId) {
        try {
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
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers for lessons between dates " + start + " and " + end, e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public CustomerQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
