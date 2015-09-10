package com.superum.api.v2.lesson;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.student.StudentNotFoundException;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.*;

import java.util.List;

import static timestar_v2.Keys.LESSON_ATTENDANCE_IBFK_1;
import static timestar_v2.Keys.LESSON_IBFK_2;
import static timestar_v2.Tables.*;

@Service
public class ValidLessonQueryServiceImpl implements ValidLessonQueryService {

    @Override
    public ValidLessonDTO readById(long lessonId, int partitionId) {
        return defaultLessonQueries.read(lessonId, partitionId, ValidLessonDTO::valueOf)
                .orElseThrow(() -> new LessonNotFoundException("Couldn't find lesson with id " + lessonId));
    }

    @Override
    public List<ValidLessonDTO> readAll(int page, int amount, long start, long end, int partitionId) {
        Condition condition = defaultLessonQueries.partitionId(partitionId)
                .and(withTime(start, end));

        return defaultLessonQueries.readForCondition(page, amount, condition, ValidLessonDTO::valueOf);
    }

    @Override
    public List<ValidLessonDTO> readForGroup(int groupId, int page, int amount, long start, long end, int partitionId) {
        if (!defaultGroupQueries.exists(groupId, partitionId))
            throw new GroupNotFoundException("No group with given id exists: " + groupId);

        Condition condition = defaultLessonQueries.partitionId(partitionId)
                .and(LESSON.GROUP_ID.eq(groupId))
                .and(withTime(start, end));

        return defaultLessonQueries.readForCondition(page, amount, condition, ValidLessonDTO::valueOf);
    }

    @Override
    public List<ValidLessonDTO> readForTeacher(int teacherId, int page, int amount, long start, long end, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        Condition condition = defaultLessonQueries.partitionId(partitionId)
                .and(LESSON.TEACHER_ID.eq(teacherId))
                .and(withTime(start, end));

        return defaultLessonQueries.readForCondition(page, amount, condition, ValidLessonDTO::valueOf);
    }

    @Override
    public List<ValidLessonDTO> readForCustomer(int customerId, int page, int amount, long start, long end, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new CustomerNotFoundException("No customer with given id exists: " + customerId);

        Condition condition = defaultLessonQueries.partitionId(partitionId)
                .and(GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId))
                .and(withTime(start, end));

        return sql.select(LESSON.fields())
                .from(LESSON)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(LESSON.ID)
                .orderBy(LESSON.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidLessonDTO::valueOf);
    }

    @Override
    public List<ValidLessonDTO> readForStudent(int studentId, int page, int amount, long start, long end, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new StudentNotFoundException("No student with given id exists: " + studentId);

        Condition condition = defaultLessonQueries.partitionId(partitionId)
                .and(LESSON_ATTENDANCE.STUDENT_ID.eq(studentId))
                .and(withTime(start, end));

        return sql.select(LESSON.fields())
                .from(LESSON)
                .join(LESSON_ATTENDANCE).onKey(LESSON_ATTENDANCE_IBFK_1)
                .where(condition)
                .groupBy(LESSON.ID)
                .orderBy(LESSON.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidLessonDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidLessonQueryServiceImpl(DSLContext sql, DefaultQueries<LessonRecord, Long> defaultLessonQueries,
                                       DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                       DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries,
                                       DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                       DefaultQueries<StudentRecord, Integer> defaultStudentQueries) {
        this.sql = sql;
        this.defaultLessonQueries = defaultLessonQueries;
        this.defaultGroupQueries = defaultGroupQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultStudentQueries = defaultStudentQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueries<LessonRecord, Long> defaultLessonQueries;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;

    private Condition withTime(long start, long end) {
        return LESSON.TIME_OF_START.between(start, end);
    }

}
