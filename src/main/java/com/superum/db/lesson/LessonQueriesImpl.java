package com.superum.db.lesson;

import com.superum.exception.DatabaseException;
import com.superum.utils.ConditionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Tables.STUDENT;

@Repository
@Transactional
public class LessonQueriesImpl implements LessonQueries {

	@Override
	public List<Lesson> readAllForCustomer(int customerId, Date start, Date end, int partitionId) {
		try {
            Condition condition = STUDENT.CUSTOMER_ID.eq(customerId)
                    .and(LESSON.PARTITION_ID.eq(partitionId));
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
		} catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read lessons for customer with id " + customerId +
                    " between dates " + start + " and " + end, e);
        }
	}

	// CONSTRUCTORS
	
	@Autowired
	public LessonQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;
	
}
