package com.superum.api.v2.attendance;

import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v2.student.StudentNotFoundException;
import com.superum.exception.DatabaseException;
import com.superum.helper.jooq.CommandsForMany;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.QueriesForMany;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.LessonRecord;
import timestar_v2.tables.records.StudentRecord;

import static timestar_v2.Tables.STUDENT;

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
    public ValidLessonAttendanceCommandServiceImpl(StudentGroupChecker studentGroupChecker,
                                                   CommandsForMany<Long, Integer> lessonAttendanceCommands,
                                                   DefaultQueries<LessonRecord, Long> defaultLessonQueries,
                                                   DefaultQueries<StudentRecord, Integer> defaultStudentQueries,
                                                   QueriesForMany<Long, Integer> defaultLessonAttendanceQueries) {
        this.studentGroupChecker = studentGroupChecker;
        this.lessonAttendanceCommands = lessonAttendanceCommands;
        this.defaultLessonQueries = defaultLessonQueries;
        this.defaultStudentQueries = defaultStudentQueries;
        this.defaultLessonAttendanceQueries = defaultLessonAttendanceQueries;
    }

    // PRIVATE

    private final StudentGroupChecker studentGroupChecker;
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
        if (!lessonAttendance.areStudentsFromGroupThisLessonWasFor((lessonId, studentIds) ->
                studentGroupChecker.checkLessonWithStudentIds(lessonId, studentIds, partitionId)))
            throw new InvalidLessonAttendanceException("Some of the students do not belong to the group this lesson was for: " +
                    lessonAttendance.secondaryValues().join(", "));
    }

}
