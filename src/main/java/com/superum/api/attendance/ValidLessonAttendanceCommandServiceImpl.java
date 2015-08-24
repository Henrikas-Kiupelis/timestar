package com.superum.api.attendance;

import com.superum.api.lesson.LessonNotFoundException;
import com.superum.api.student.StudentNotFoundException;
import com.superum.db.generated.timestar.tables.records.LessonRecord;
import com.superum.db.generated.timestar.tables.records.StudentRecord;
import com.superum.exception.DatabaseException;
import com.superum.helper.jooq.CommandsForMany;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.QueriesForMany;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Keys.*;
import static com.superum.db.generated.timestar.Tables.*;

@Service
@Transactional
public class ValidLessonAttendanceCommandServiceImpl implements ValidLessonAttendanceCommandService {

    @Override
    public void create(ValidLessonAttendanceDTO validLessonAttendanceDTO, int partitionId) {
        ValidLessonAttendance lessonAttendance = new ValidLessonAttendance(validLessonAttendanceDTO);
        validateIdsInDB(lessonAttendance, partitionId);
        validateStudentIdsAgainstGroup(lessonAttendance, partitionId);

        if (defaultLessonAttendanceQueries.existsPrimary(lessonAttendance.primaryValue(), partitionId))
            throw new DuplicateLessonAttendanceException("Lesson attendance for this lesson already exists; please use POST instead!");

        if (lessonAttendanceCommands.create(lessonAttendance, partitionId) == 0)
            throw new DatabaseException("Couldn't create lesson attendance: " + lessonAttendance);
    }

    @Override
    public void update(ValidLessonAttendanceDTO validLessonAttendanceDTO, int partitionId) {
        ValidLessonAttendance lessonAttendance = new ValidLessonAttendance(validLessonAttendanceDTO);
        validateIdsInDB(lessonAttendance, partitionId);
        validateStudentIdsAgainstGroup(lessonAttendance, partitionId);

        if (!defaultLessonAttendanceQueries.existsPrimary(lessonAttendance.primaryValue(), partitionId))
            throw new LessonAttendanceNotFoundException("Lesson attendance for this lesson doesn't exist yet; please use PUT instead!");

        if (lessonAttendanceCommands.update(lessonAttendance, partitionId) == 0)
            throw new DatabaseException("Couldn't update lesson attendance: " + lessonAttendance);
    }

    @Override
    public void deleteForLesson(long lessonId, int partitionId) {
        if (!defaultLessonQueries.exists(lessonId, partitionId))
            throw new LessonNotFoundException("Couldn't find lesson with id: " + lessonId);

        if (!defaultLessonAttendanceQueries.existsPrimary(lessonId, partitionId))
            throw new LessonAttendanceNotFoundException("Lesson attendance doesn't exist for lesson with id: " + lessonId);

        if (lessonAttendanceCommands.deletePrimary(lessonId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete lesson attendance for lesson with id : " + lessonId);
    }

    @Override
    public void deleteForStudent(int studentId, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new StudentNotFoundException("Couldn't find student with id: " + studentId);

        if (!defaultLessonAttendanceQueries.existsSecondary(studentId, partitionId))
            throw new LessonAttendanceNotFoundException("Lesson attendance doesn't exist for student with id: " + studentId);

        if (lessonAttendanceCommands.deleteSecondary(studentId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete lesson attendance for student with id : " + studentId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidLessonAttendanceCommandServiceImpl(DSLContext sql, CommandsForMany<Long, Integer> lessonAttendanceCommands,
                                                   DefaultQueries<LessonRecord, Long> defaultLessonQueries,
                                                   DefaultQueries<StudentRecord, Integer> defaultStudentQueries,
                                                   QueriesForMany<Long, Integer> defaultLessonAttendanceQueries) {
        this.sql = sql;
        this.lessonAttendanceCommands = lessonAttendanceCommands;
        this.defaultLessonQueries = defaultLessonQueries;
        this.defaultStudentQueries = defaultStudentQueries;
        this.defaultLessonAttendanceQueries = defaultLessonAttendanceQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final CommandsForMany<Long, Integer> lessonAttendanceCommands;
    private final DefaultQueries<LessonRecord, Long> defaultLessonQueries;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;
    private final QueriesForMany<Long, Integer> defaultLessonAttendanceQueries;

    private void validateIdsInDB(ValidLessonAttendance lessonAttendance, int partitionId) {
        if (!defaultLessonQueries.exists(lessonAttendance.primaryValue(), partitionId))
            throw new LessonNotFoundException("Couldn't find lesson with id: " + lessonAttendance.primaryValue());

        Condition condition = lessonAttendance.secondaryValuesEq(STUDENT.ID)
                .and(defaultStudentQueries.partitionId(partitionId));

        if (defaultStudentQueries.countForCondition(condition) != lessonAttendance.secondaryValues().count())
            throw new StudentNotFoundException("Couldn't find some of students with ids: " +
                    lessonAttendance.secondaryValues().join(", "));
    }

    private void validateStudentIdsAgainstGroup(ValidLessonAttendance lessonAttendance, int partitionId) {
        Condition condition = lessonAttendance.secondaryValuesEq(STUDENT.ID)
                .and(defaultStudentQueries.partitionId(partitionId))
                .and(STUDENTS_IN_GROUPS.GROUP_ID.eq(LESSON.GROUP_ID));

        Select<?> select = sql.selectOne()
                .from(LESSON_ATTENDANCE)
                .join(LESSON).onKey(LESSON_ATTENDANCE_IBFK_1)
                .join(STUDENT).onKey(LESSON_ATTENDANCE_IBFK_2)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .where(condition)
                .groupBy(STUDENT.ID);

        if (sql.fetchCount(select) != lessonAttendance.secondaryValues().count())
            throw new InvalidLessonAttendanceException("Some of the students do not belong to the group this lesson was for: " +
                    lessonAttendance.secondaryValues().join(", "));
    }

}
