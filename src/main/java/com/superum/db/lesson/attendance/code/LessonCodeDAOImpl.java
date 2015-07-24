package com.superum.db.lesson.attendance.code;

import com.superum.db.generated.timestar.tables.records.LessonCodeRecord;
import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.LESSON_CODE;

@Repository
@Transactional
public class LessonCodeDAOImpl implements LessonCodeDAO {

	@Override
	public int find(long lessonId, int code, int partitionId) {
		int studentId = sql.select(LESSON_CODE.STUDENT_ID)
				.from(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId)
						.and(LESSON_CODE.CODE.eq(code))
						.and(LESSON_CODE.PARTITION_ID.eq(partitionId)))
				.fetch().stream()
				.findFirst()
				.map(record -> record.getValue(LESSON_CODE.STUDENT_ID))
				.orElseThrow(() -> new DatabaseException("No valid ID found."));
		
		sql.delete(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId)
						.and(LESSON_CODE.CODE.eq(code))
						.and(LESSON_CODE.PARTITION_ID.eq(partitionId)))
				.execute();
		
		return studentId;
	}
	
	@Override
	public List<LessonCode> add(List<LessonCode> lessonCodes, int partitionId) {
		InsertValuesStep4<LessonCodeRecord,Integer,Long,Integer,Integer> step = sql.insertInto(LESSON_CODE, LESSON_CODE.PARTITION_ID, LESSON_CODE.LESSON_ID, LESSON_CODE.STUDENT_ID, LESSON_CODE.CODE);
		
		for (LessonCode lessonCode : lessonCodes)
			step = step.values(partitionId, lessonCode.getLessonId(), lessonCode.getStudentId(), lessonCode.getCode());
		
		step.execute();
		
		return lessonCodes;
	}
	
	@Override
	public int deleteForStudent(int studentId, int partitionId) {
		return sql.delete(LESSON_CODE)
				.where(LESSON_CODE.STUDENT_ID.eq(studentId)
						.and(LESSON_CODE.PARTITION_ID.eq(partitionId)))
				.execute();
	}
	
	@Override
	public int deleteForLesson(long lessonId, int partitionId) {
		return sql.delete(LESSON_CODE)
				.where(LESSON_CODE.LESSON_ID.eq(lessonId)
						.and(LESSON_CODE.PARTITION_ID.eq(partitionId)))
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
