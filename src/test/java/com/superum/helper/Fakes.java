package com.superum.helper;

import com.google.common.collect.Sets;
import com.superum.api.v2.attendance.ValidLessonAttendanceDTO;
import com.superum.api.v2.customer.ValidCustomerDTO;
import com.superum.api.v2.group.ValidGroupDTO;
import com.superum.api.v2.grouping.ValidGroupingDTO;
import com.superum.api.v2.lesson.ValidLessonDTO;
import com.superum.api.v2.student.ValidStudentDTO;
import com.superum.api.v2.teacher.FullTeacherDTO;
import eu.goodlike.test.Fake;
import org.joda.time.LocalDate;

import java.util.Collections;

public class Fakes {

    public static PartitionAccount partitionAccount() {
        return new PartitionAccount(0, "test");
    }

    public static ValidCustomerDTO customer(int id) {
        return ValidCustomerDTO.stepBuilder()
                .startDate(localDate(id))
                .name(Fake.name(id))
                .phone(Fake.phone(id))
                .website(Fake.website(id))
                .id(id)
                .picture(Fake.picture(id))
                .comment(Fake.comment(id))
                .build();
    }

    public static FullTeacherDTO teacher(int id) {
        return FullTeacherDTO.stepBuilder()
                .paymentDay(Fake.day(id))
                .hourlyWage(Fake.wage(id))
                .academicWage(Fake.wage(id))
                .name(Fake.name(id))
                .surname(Fake.surname(id))
                .phone(Fake.phone(id))
                .city(Fake.city(id))
                .email(Fake.email(id))
                .languages(Fake.language(id))
                .id(id)
                .picture(Fake.picture(id))
                .document(Fake.document(id))
                .comment(Fake.comment(id))
                .build();
    }

    public static ValidGroupDTO group(int id) {
        return group(id, id, id);
    }

    public static ValidGroupDTO group(int id, int teacherId, Integer customerId) {
        return ValidGroupDTO.stepBuilder()
                .teacherId(teacherId)
                .usesHourlyWage(Fake.Boolean(id))
                .languageLevel(Fake.languageLevel(id))
                .name(Fake.name(id))
                .id(id)
                .customerId(customerId)
                .build();
    }

    public static ValidStudentDTO student(int id) {
        return student(id, id);
    }

    public static ValidStudentDTO student(int id, Integer customerId) {
        return ValidStudentDTO.stepBuilder()
                .customerId(customerId)
                .email(Fake.email(id))
                .name(Fake.name(id))
                .id(id)
                .code(Fake.code(id))
                .build();
    }

    public static ValidStudentDTO studentWithDate(int id) {
        return ValidStudentDTO.stepBuilder()
                .noCustomer()
                .startDate(localDate(id))
                .email(Fake.email(id))
                .name(Fake.name(id))
                .id(id)
                .code(Fake.code(id))
                .build();
    }

    public static ValidLessonDTO lesson(long id) {
        return lesson(id, (int) id, (int) id);
    }

    public static ValidLessonDTO lesson(long id, int groupId, int teacherId) {
        return ValidLessonDTO.stepBuilder()
                .groupIdWithTeacher(groupId, teacherId)
                .startTime(Fake.time(id))
                .length(Fake.duration(id))
                .id(id)
                .comment(Fake.comment(id))
                .build();
    }

    public static ValidGroupingDTO grouping(int groupId) {
        return ValidGroupingDTO.jsonInstance(groupId, Collections.singleton(groupId));
    }

    public static ValidGroupingDTO grouping(int groupId, Integer... studentIds) {
        return ValidGroupingDTO.jsonInstance(groupId, Sets.newHashSet(studentIds));
    }

    public static ValidLessonAttendanceDTO lessonAttendance(long lessonId) {
        return ValidLessonAttendanceDTO.jsonInstance(lessonId, Collections.singleton((int) lessonId));
    }

    public static ValidLessonAttendanceDTO lessonAttendance(long lessonId, Integer... studentIds) {
        return ValidLessonAttendanceDTO.jsonInstance(lessonId, Sets.newHashSet(studentIds));
    }

    public static LocalDate localDate(long id) {
        return LocalDate.parse("2015-01-" + Fake.dayString(id));
    }

    // PRIVATE

    private Fakes() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
