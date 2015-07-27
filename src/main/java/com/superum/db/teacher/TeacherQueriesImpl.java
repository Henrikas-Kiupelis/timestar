package com.superum.db.teacher;

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

import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_1;
import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Tables.TEACHER;

@Repository
@Transactional
public class TeacherQueriesImpl implements TeacherQueries {

	@Override
	public List<Teacher> readAllForLessons(Date start, Date end, int partitionId) {
		try {
            SelectJoinStep<Record> step = sql.select(TEACHER.fields())
                    .from(TEACHER)
                    .join(LESSON).onKey(LESSON_IBFK_1);

            Condition condition = TEACHER.PARTITION_ID.eq(partitionId);
            Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
            if (dateCondition != null)
                condition = condition.and(dateCondition);

            return step
                    .where(condition)
                    .groupBy(TEACHER.ID)
                    .orderBy(TEACHER.ID)
                    .fetch()
                    .map(Teacher::valueOf);
		} catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read teachers for lessons between dates " + start + " and " + end , e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
