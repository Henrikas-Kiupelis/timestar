package com.superum.api.v3.lesson;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v2.student.StudentNotFoundException;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import eu.goodlike.libraries.jooq.Queries;
import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.*;

import java.util.List;

import static timestar_v2.Keys.LESSON_ATTENDANCE_IBFK_1;
import static timestar_v2.Keys.LESSON_IBFK_2;
import static timestar_v2.Tables.*;

@Service
public class LessonQueriesImpl implements LessonQueries {

    @Override
    public FetchedLesson readById(long lessonId) {
        return lessonQueries.read(lessonId, lessonTransformer::from)
                .orElseThrow(() -> new LessonNotFoundException("Couldn't find lesson with id " + lessonId));
    }

    @Override
    public List<FetchedLesson> readAll(int page, int amount, long start, long end) {
        return lessonQueries.readAll(page, amount, lessonTransformer::from);
    }

    @Override
    public List<FetchedLesson> readForGroup(int groupId, int page, int amount, long start, long end) {
        if (!groupQueries.exists(groupId))
            throw new GroupNotFoundException("No group with given id exists: " + groupId);

        return lessonQueries.read(page, amount, condition(LESSON.GROUP_ID, groupId, start, end),
                lessonTransformer::from);
    }

    @Override
    public List<FetchedLesson> readForTeacher(int teacherId, int page, int amount, long start, long end) {
        if (!teacherQueries.exists(teacherId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return lessonQueries.read(page, amount, condition(LESSON.TEACHER_ID, teacherId, start, end),
                lessonTransformer::from);
    }

    @Override
    public List<FetchedLesson> readForCustomer(int customerId, int page, int amount, long start, long end) {
        if (!customerQueries.exists(customerId))
            throw new CustomerNotFoundException("No customer with given id exists: " + customerId);

        return lessonQueries.readJoin(page, amount, condition(GROUP_OF_STUDENTS.CUSTOMER_ID, customerId, start, end),
                lessonTransformer::from, sql -> sql.join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2));
    }

    @Override
    public List<FetchedLesson> readForStudent(int studentId, int page, int amount, long start, long end) {
        if (!studentQueries.exists(studentId))
            throw new StudentNotFoundException("No student with given id exists: " + studentId);

        return lessonQueries.readJoin(page, amount, condition(LESSON_ATTENDANCE.STUDENT_ID, studentId, start, end),
                lessonTransformer::from, sql -> sql.join(LESSON_ATTENDANCE).onKey(LESSON_ATTENDANCE_IBFK_1));
    }

    // CONSTRUCTORS

    @Autowired
    public LessonQueriesImpl(LessonTransformer lessonTransformer, Queries<LessonRecord, Long> lessonQueries,
                             Queries<GroupOfStudentsRecord, Integer> groupQueries,
                             Queries<TeacherRecord, Integer> teacherQueries,
                             Queries<CustomerRecord, Integer> customerQueries,
                             Queries<StudentRecord, Integer> studentQueries) {
        this.lessonTransformer = lessonTransformer;
        this.lessonQueries = lessonQueries;
        this.groupQueries = groupQueries;
        this.teacherQueries = teacherQueries;
        this.customerQueries = customerQueries;
        this.studentQueries = studentQueries;
    }

    // PRIVATE

    private final LessonTransformer lessonTransformer;
    private final Queries<LessonRecord, Long> lessonQueries;
    private final Queries<GroupOfStudentsRecord, Integer> groupQueries;
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final Queries<CustomerRecord, Integer> customerQueries;
    private final Queries<StudentRecord, Integer> studentQueries;

    private <T> Condition condition(Field<T> field, T value, long startTime, long endTime) {
        return field.eq(value).and(LESSON.TIME_OF_START.between(startTime, endTime));
    }

}
