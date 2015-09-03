package com.superum.helper;

import com.superum.api.attendance.ValidLessonAttendanceDTO;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;
import com.superum.api.grouping.ValidGroupingDTO;
import com.superum.api.lesson.ValidLessonDTO;
import com.superum.api.student.ValidStudentDTO;
import com.superum.api.teacher.FullTeacherDTO;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Fake {

    // OBJECTS

    public static <T> List<T> someOf(int amount, IntFunction<T> fakeCreationMethod) {
        return IntStream.rangeClosed(1, amount)
                .mapToObj(fakeCreationMethod)
                .collect(Collectors.toList());
    }

    public static <T> List<T> someOfLong(long amount, LongFunction<T> fakeCreationMethodForObjectsWithLongId) {
        return LongStream.rangeClosed(1, amount)
                .mapToObj(fakeCreationMethodForObjectsWithLongId)
                .collect(Collectors.toList());
    }

    public static PartitionAccount partitionAccount() {
        return new PartitionAccount(0, "test");
    }

    public static ValidCustomerDTO customer(int id) {
        return ValidCustomerDTO.stepBuilder()
                .startDate(localDate(id))
                .name(name(id))
                .phone(phone(id))
                .website(website(id))
                .id(id)
                .picture(picture(id))
                .comment(comment(id))
                .build();
    }

    public static FullTeacherDTO teacher(int id) {
        return FullTeacherDTO.stepBuilder()
                .paymentDay(day(id))
                .hourlyWage(wage(id))
                .academicWage(wage(id))
                .name(name(id))
                .surname(surname(id))
                .phone(phone(id))
                .city(city(id))
                .email(email(id))
                .languages(language(id))
                .id(id)
                .picture(picture(id))
                .document(document(id))
                .comment(comment(id))
                .build();
    }

    public static ValidGroupDTO group(int id) {
        return group(id, id, id);
    }

    public static ValidGroupDTO group(int id, int teacherId, Integer customerId) {
        return ValidGroupDTO.stepBuilder()
                .teacherId(teacherId)
                .usesHourlyWage(Boolean(id))
                .languageLevel(languageLevel(id))
                .name(name(id))
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
                .email(email(id))
                .name(name(id))
                .id(id)
                .code(code(id))
                .build();
    }

    public static ValidStudentDTO studentWithDate(int id) {
        return ValidStudentDTO.stepBuilder()
                .noCustomer()
                .startDate(localDate(id))
                .email(email(id))
                .name(name(id))
                .id(id)
                .code(code(id))
                .build();
    }

    public static ValidGroupingDTO grouping(int id) {
        return ValidGroupingDTO.jsonInstance(id, Collections.singleton(id));
    }

    public static ValidLessonDTO lesson(long id) {
        return lesson(id, (int)id, (int)id);
    }

    public static ValidLessonDTO lesson(long id, int groupId, int teacherId) {
        return ValidLessonDTO.stepBuilder()
                .groupIdWithTeacher(groupId, teacherId)
                .startTime(time(id))
                .length(duration(id))
                .id(id)
                .comment(comment(id))
                .build();
    }

    public static ValidLessonAttendanceDTO lessonAttendance(long id) {
        return ValidLessonAttendanceDTO.jsonInstance(id, Collections.singleton((int)id));
    }

    // FIELDS

    public static String name(long id) {
        return "Name" + id;
    }

    public static String surname(long id) {
        return "Surname" + id;
    }

    public static String email(long id) {
        return "fake" + id + "@fake.lt";
    }

    public static String city(long id) {
        return "City" + id;
    }

    public static String phone(long id) {
        return "860000000" + id;
    }

    public static String picture(long id) {
        return "Picture" + id + ".jpg";
    }

    public static String document(long id) {
        return "Document" + id;
    }

    public static String comment(long id) {
        return "Comment" + id;
    }

    public static BigDecimal wage(long id) {
        return BigDecimal.valueOf(id);
    }

    public static int day(long id) {
        if (id == 0) return 31;
        if (id < 0) id = -id;

        return (int)((id - 1) % 31) + 1;
    }

    /**
     * @deprecated Date fields should not be used any longer, use Instant/LocalDate instead
     */
    @Deprecated
    public static Date date(long id) {
        return Date.from(Instant.parse("2015-01-" + dayString(id) + "T00:00:00.00Z"));
    }

    public static String website(long id) {
        return "http://website" + id + ".eu/";
    }

    public static boolean Boolean(long id) {
        return (id & 1) == 1;
    }

    public static String languageLevel(long id) {
        return "English: C" + id;
    }

    public static int hour(long id) {
        return (int)(id % 24);
    }

    public static int minute(long id) {
        return (int)(id % 60);
    }

    public static int id(long id) {
        return (int)id;
    }

    public static LocalDate localDate(long id) {
        return LocalDate.parse("2015-01-" + dayString(id));
    }

    public static String dayString(long id) {
        String dayString = String.valueOf(day(id));
        if (dayString.length() == 1)
            dayString = 0 + dayString;
        return dayString;
    }

    public static String language(long id) {
        if (id < 0) id = -id;
        return "L" + (id % 100);
    }

    public static String password() {
        return "canYouGuessMe?nah";
    }

    public static int duration(long id) {
        return (int)(45 * id);
    }

    public static int code(long id) {
        long code = 3;
        while (code < 900000)
            code *= id + code;

        return (int)(id % 900000) + 100000;
    }

    public static long time(long id) {
        return id * 1000000;
    }

    // PRIVATE

    private Fake() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
