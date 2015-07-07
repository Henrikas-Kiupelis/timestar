package com.superum.db.lesson.attendance.code;

import static com.superum.db.generated.timestar.Tables.LESSON_CODE;

import com.superum.db.exception.DatabaseException;
import com.superum.db.generated.timestar.tables.records.LessonCodeRecord;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LessonCodeDAOImpl implements LessonCodeDAO {

	@Override
	public int find(long lessonId, int code) {
		int studentId = sql.select(LESSON_CODE.STUDENT_ID)
				.from(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId)
						.and(LESSON_CODE.CODE.eq(code)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(LESSON_CODE.STUDENT_ID))
				.orElseThrow(() -> new DatabaseException("No valid ID found."));
		
		sql.delete(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId)
						.and(LESSON_CODE.CODE.eq(code)))
				.execute();
		
		return studentId;
	}
	
	@Override
	public List<LessonCode> add(List<LessonCode> lessonCodes) {
		InsertValuesStep3<LessonCodeRecord,Long,Integer,Integer> step = sql.insertInto(LESSON_CODE, LESSON_CODE.LESSON_ID, LESSON_CODE.STUDENT_ID, LESSON_CODE.CODE);
		
		for (LessonCode lessonCode : lessonCodes)
			step = step.values(lessonCode.getLessonId(), lessonCode.getStudentId(), lessonCode.getCode());
		
		step.execute();
		
		return lessonCodes;
	}
	
	@Override
	public int deleteForStudent(int studentId) {
		return sql.delete(LESSON_CODE)
				.where(LESSON_CODE.STUDENT_ID.eq(studentId))
				.execute();
	}
	
	@Override
	public int deleteForLesson(long lessonId) {
		return sql.delete(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId))
				.execute();
	}

	// CONSTRUCTORS

	@Autowired
	public LessonCodeDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
