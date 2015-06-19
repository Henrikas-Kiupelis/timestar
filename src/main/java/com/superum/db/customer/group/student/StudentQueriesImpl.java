package com.superum.db.customer.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;
import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;
import static com.superum.db.generated.timestar.Tables.ATTENDANCE;
import static com.superum.db.generated.timestar.Keys.STUDENT_IBFK_1;
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
	public List<Student> readAllForCustomer(int customerId) {
		return sql.select(STUDENT.fields())
				.from(STUDENT)
				.join(STUDENT_GROUP).onKey(STUDENT_IBFK_1)
				.where(STUDENT_GROUP.CUSTOMER_ID.eq(customerId))
				.groupBy(STUDENT.ID)
				.fetch()
				.map(Student::valueOf);
	}
	
	@Override
	public List<Student> readAllForLesson(long lessonId) {
		return sql.select(STUDENT.fields())
				.from(STUDENT)
				.join(ATTENDANCE).onKey(ATTENDANCE_IBFK_2)
				.where(ATTENDANCE.LESSON_ID.eq(lessonId))
				.groupBy(STUDENT.ID)
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
