package com.superum.db.lesson.attendance;

import static com.superum.db.generated.timestar.Tables.LESSON_ATTENDANCE;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.LessonAttendanceRecord;

@Repository
@Transactional
public class LessonAttendanceDAOImpl implements LessonAttendanceDAO {

	@Override
	public LessonAttendance create(LessonAttendance attendance) {
		Long lessonId = attendance.getLessonId();
		List<Integer> studentIds = attendance.getStudentIds();
		
		InsertValuesStep2<LessonAttendanceRecord, Long, Integer> step = sql.insertInto(LESSON_ATTENDANCE, LESSON_ATTENDANCE.LESSON_ID, LESSON_ATTENDANCE.STUDENT_ID);
		for (Integer studentId : studentIds)
			step = step.values(lessonId, studentId);
		
		step.execute();
		return attendance;
	}

	@Override
	public LessonAttendance read(Long lessonId) {
		List<Integer> studentIds =  sql.select(LESSON_ATTENDANCE.STUDENT_ID)
				.from(LESSON_ATTENDANCE)
				.where(LESSON_ATTENDANCE.LESSON_ID.eq(lessonId))
				.fetch()
				.map(record -> record.getValue(LESSON_ATTENDANCE.STUDENT_ID));
		return new LessonAttendance(lessonId, studentIds);
	}

	@Override
	public LessonAttendance update(LessonAttendance attendance) {
		long lessonId = attendance.getLessonId();
		LessonAttendance old = delete(lessonId);
		
		create(attendance);
		return old;
	}

	@Override
	public LessonAttendance delete(Long lessonId) {
		return delete(new LessonAttendance(lessonId, Collections.emptyList()));
	}

	@Override
	public LessonAttendance delete(LessonAttendance attendance) {
		Long lessonId = attendance.getLessonId();
		LessonAttendance old = read(lessonId);
		
		List<Integer> studentIds = attendance.getStudentIds();
		
		Condition condition = LESSON_ATTENDANCE.LESSON_ID.eq(lessonId);
		for (Integer studentId : studentIds)
			condition = condition.and(LESSON_ATTENDANCE.STUDENT_ID.eq(studentId));
		
		sql.delete(LESSON_ATTENDANCE)
			.where(condition)
			.execute();
		
		return old;
	}
	
	@Override
	public int deleteForStudent(int studentId) {
		return sql.delete(LESSON_ATTENDANCE)
				.where(LESSON_ATTENDANCE.STUDENT_ID.eq(studentId))
				.execute();
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
