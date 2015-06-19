package com.superum.db.lesson.attendance;

import static com.superum.db.generated.timestar.Tables.ATTENDANCE;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.AttendanceRecord;

@Repository
@Transactional
public class AttendanceDAOImpl implements AttendanceDAO {

	@Override
	public Attendance create(Attendance attendance) {
		Long lessonId = attendance.getLessonId();
		List<Integer> studentIds = attendance.getStudentIds();
		
		InsertValuesStep2<AttendanceRecord, Long, Integer> step = sql.insertInto(ATTENDANCE, ATTENDANCE.LESSON_ID, ATTENDANCE.STUDENT_ID);
		for (Integer studentId : studentIds)
			step = step.values(lessonId, studentId);
		
		step.execute();
		return attendance;
	}

	@Override
	public Attendance read(Long lessonId) {
		List<Integer> studentIds =  sql.select(ATTENDANCE.STUDENT_ID)
				.from(ATTENDANCE)
				.where(ATTENDANCE.LESSON_ID.eq(lessonId))
				.fetch()
				.map(record -> record.getValue(ATTENDANCE.STUDENT_ID));
		return new Attendance(lessonId, studentIds);
	}

	@Override
	public Attendance update(Attendance attendance) {
		long lessonId = attendance.getLessonId();
		Attendance old = delete(lessonId);
		
		create(attendance);
		return old;
	}

	@Override
	public Attendance delete(Long lessonId) {
		return delete(new Attendance(lessonId, Collections.emptyList()));
	}

	@Override
	public Attendance delete(Attendance attendance) {
		Long lessonId = attendance.getLessonId();
		Attendance old = read(lessonId);
		
		List<Integer> studentIds = attendance.getStudentIds();
		
		Condition condition = ATTENDANCE.LESSON_ID.eq(lessonId);
		for (Integer studentId : studentIds)
			condition = condition.and(ATTENDANCE.STUDENT_ID.eq(studentId));
		
		sql.delete(ATTENDANCE)
			.where(condition)
			.execute();
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public AttendanceDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
