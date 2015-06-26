package com.superum.db.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.ATTENDANCE;
import static com.superum.db.generated.timestar.Keys.ATTENDANCE_IBFK_2;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StudentQueriesImpl implements StudentQueries {
	
	@Override
	public List<Student> readAllForLesson(long lessonId) {
		return sql.select(STUDENT.fields())
				.from(STUDENT)
				.join(ATTENDANCE).onKey(ATTENDANCE_IBFK_2)
				.where(ATTENDANCE.LESSON_ID.eq(lessonId))
				.groupBy(STUDENT.ID)
				.orderBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}
	
	// CONSTRUCTORS

	@Autowired
	public StudentQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

	

}
