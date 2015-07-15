package com.superum.db.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.LESSON_ATTENDANCE;
import static com.superum.db.generated.timestar.Keys.LESSON_ATTENDANCE_IBFK_2;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StudentQueriesImpl implements StudentQueries {
	
	@Override
	public List<Student> readAllForLesson(long lessonId, int partitionId) {
		return sql.select(STUDENT.fields())
				.from(STUDENT)
				.join(LESSON_ATTENDANCE).onKey(LESSON_ATTENDANCE_IBFK_2)
				.where(LESSON_ATTENDANCE.LESSON_ID.eq(lessonId)
						.and(STUDENT.PARTITION_ID.eq(partitionId)))
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
