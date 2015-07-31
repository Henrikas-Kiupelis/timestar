package com.superum.db.group.student;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.LESSON_ATTENDANCE_IBFK_2;
import static com.superum.db.generated.timestar.Keys.STUDENTS_IN_GROUPS_IBFK_1;
import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class StudentQueriesImpl implements StudentQueries {
	
	@Override
	public List<Student> readAllForLesson(long lessonId, int partitionId) {
		try {
            return sql.select(STUDENT.fields())
                    .from(STUDENT)
                    .join(LESSON_ATTENDANCE).onKey(LESSON_ATTENDANCE_IBFK_2)
                    .where(LESSON_ATTENDANCE.LESSON_ID.eq(lessonId)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .groupBy(STUDENT.ID)
                    .orderBy(STUDENT.ID)
                    .fetch()
                    .map(Student::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all students for lesson with id " + lessonId, e);
        }
	}

    @Override
    public List<Student> readAllForGroup(int groupId, int partitionId) {
        try {
            return sql.select(STUDENT.fields())
                    .from(STUDENT)
                    .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                    .where(STUDENTS_IN_GROUPS.GROUP_ID.eq(groupId)
                            .and(STUDENT.PARTITION_ID.eq(partitionId)))
                    .orderBy(STUDENT.ID)
                    .fetch()
                    .map(Student::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all students for group with id " + groupId, e);
        }
    }
	
	// CONSTRUCTORS

	@Autowired
	public StudentQueriesImpl(DSLContext sql) {
		this.sql = sql;
	}
	
	// PRIVATE
	
	private final DSLContext sql;

	

}
