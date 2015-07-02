package com.superum.db.lesson.attendance.code;

import static com.superum.db.generated.timestar.Tables.LESSON_CODE;

import com.superum.db.exception.DatabaseException;
import com.superum.db.generated.timestar.tables.records.LessonCodeRecord;
import com.superum.db.lesson.attendance.Attendance;
import com.superum.utils.RandomUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CodeDAOImpl implements CodeDAO {

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
	public Map<Integer, Integer> add(Attendance attendance) {
		long lessonId = attendance.getLessonId();
		List<Integer> studentIds = attendance.getStudentIds();

		//The following code ensures that all the codes are unique for this lesson
		Set<Integer> codes = new HashSet<Integer>();
		while (codes.size() < studentIds.size())
			codes.add(RandomUtils.randomNumber(900000) + 100000);
		
		InsertValuesStep3<LessonCodeRecord,Long,Integer,Integer> step = sql.insertInto(LESSON_CODE, LESSON_CODE.LESSON_ID, LESSON_CODE.STUDENT_ID, LESSON_CODE.CODE);
		
		Map<Integer, Integer> codesForStudents = new HashMap<>();
		int i = 0;
		for (int code : codes) {
			int studentId = studentIds.get(i++);
			codesForStudents.put(studentId, code);
			step = step.values(lessonId, studentId, code);
		}
		return codesForStudents;
	}

	// CONSTRUCTORS

	@Autowired
	public CodeDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
