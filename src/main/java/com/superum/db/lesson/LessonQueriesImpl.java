package com.superum.db.lesson;

import com.superum.exception.DatabaseException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_2;
import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;
import static com.superum.db.generated.timestar.Tables.LESSON;

@Repository
@Transactional
public class LessonQueriesImpl implements LessonQueries {

	@Override
	public List<Lesson> readAllForCustomer(int customerId, long start, long end, int partitionId) {
		try {
            Condition condition = GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId)
                    .and(LESSON.PARTITION_ID.eq(partitionId))
                    .and(LESSON.TIME_OF_START.between(start, end));

            return sql.select(LESSON.fields())
                    .from(LESSON)
                    .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
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
