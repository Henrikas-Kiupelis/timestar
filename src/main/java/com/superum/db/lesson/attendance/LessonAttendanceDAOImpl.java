package com.superum.db.lesson.attendance;

import com.superum.db.generated.timestar.tables.records.LessonAttendanceRecord;
import com.superum.exception.DatabaseException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.LESSON_ATTENDANCE;

@Repository
@Transactional
public class LessonAttendanceDAOImpl implements LessonAttendanceDAO {

	@Override
	public LessonAttendance create(LessonAttendance attendance, int partitionId) {
		try {
            Long lessonId = attendance.getLessonId();
            List<Integer> studentIds = attendance.getStudentIds();

            InsertValuesStep3<LessonAttendanceRecord, Integer, Long, Integer> step =
                    sql.insertInto(LESSON_ATTENDANCE, LESSON_ATTENDANCE.PARTITION_ID, LESSON_ATTENDANCE.LESSON_ID, LESSON_ATTENDANCE.STUDENT_ID);
            for (Integer studentId : studentIds)
                step = step.values(partitionId, lessonId, studentId);

            step.execute();
            return attendance;
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert attendance: " + attendance +
                    "; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public LessonAttendance read(Long lessonId, int partitionId) {
        try {
            List<Integer> studentIds =  sql.select(LESSON_ATTENDANCE.STUDENT_ID)
                    .from(LESSON_ATTENDANCE)
                    .where(LESSON_ATTENDANCE.LESSON_ID.eq(lessonId)
                            .and(LESSON_ATTENDANCE.PARTITION_ID.eq(partitionId)))
                    .fetch()
                    .map(record -> record.getValue(LESSON_ATTENDANCE.STUDENT_ID));
            return new LessonAttendance(lessonId, studentIds);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read attendance for id " + lessonId, e);
        }
	}

	@Override
	public LessonAttendance update(LessonAttendance attendance, int partitionId) {
        try {
            long lessonId = attendance.getLessonId();
            LessonAttendance old = delete(lessonId, partitionId);

            create(attendance, partitionId);
            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update attendance " + attendance, e);
        }
	}

	@Override
	public LessonAttendance delete(Long lessonId, int partitionId) {
        try {
            return delete(new LessonAttendance(lessonId, Collections.emptyList()), partitionId);
        } catch (DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete attendance for id " + lessonId, e);
        }
	}

	@Override
	public LessonAttendance delete(LessonAttendance attendance, int partitionId) {
        try {
            Long lessonId = attendance.getLessonId();
            LessonAttendance old = read(lessonId, partitionId);

            List<Integer> studentIds = attendance.getStudentIds();

            Condition condition = LESSON_ATTENDANCE.LESSON_ID.eq(lessonId)
                    .and(LESSON_ATTENDANCE.PARTITION_ID.eq(partitionId));
            for (Integer studentId : studentIds)
                condition = condition.and(LESSON_ATTENDANCE.STUDENT_ID.eq(studentId));

            sql.delete(LESSON_ATTENDANCE)
                    .where(condition)
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete attendance " + attendance, e);
        }
	}
	
	@Override
	public int deleteForStudent(int studentId, int partitionId) {
        try {
            return sql.delete(LESSON_ATTENDANCE)
                    .where(LESSON_ATTENDANCE.STUDENT_ID.eq(studentId)
                            .and(LESSON_ATTENDANCE.PARTITION_ID.eq(partitionId)))
                    .execute();
        } catch (DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete attendance for student with id " + studentId, e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
