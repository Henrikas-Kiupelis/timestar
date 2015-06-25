package com.superum.db.teacher;

import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_1;

import java.sql.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.utils.ConditionUtils;

@Repository
@Transactional
public class TeacherQueriesImpl implements TeacherQueries {

	@Override
	public List<Teacher> readAllforLessons(Date start, Date end) {
		SelectJoinStep<Record> join = sql.select(TEACHER.fields())
				.from(TEACHER)
				.join(LESSON).onKey(LESSON_IBFK_1);
		
		Condition dateCondition = ConditionUtils.betweenDates(LESSON.DATE_OF_LESSON, start, end);
		Result<Record> result = dateCondition == null
				? join.groupBy(TEACHER.ID).fetch()
				: join.where(dateCondition).groupBy(TEACHER.ID).fetch();
		
		return result.map(Teacher::valueOf);
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
