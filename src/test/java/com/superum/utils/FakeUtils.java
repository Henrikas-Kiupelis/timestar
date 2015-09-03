package com.superum.utils;

import com.superum.db.account.Account;
import com.superum.db.account.AccountType;
import com.superum.db.customer.Customer;
import com.superum.db.group.Group;
import com.superum.db.group.student.Student;
import com.superum.db.group.studentsgroup.StudentsInGroup;
import com.superum.db.lesson.Lesson;
import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.partition.Partition;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.TeacherLanguages;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.superum.helper.Fake.Boolean;
import static com.superum.helper.Fake.*;

public class FakeUtils {

    public static <T> List<T> makeSomeFakes(int amount, IntFunction<T> fakeCreationMethod) {
        return IntStream.rangeClosed(1, amount)
                .mapToObj(fakeCreationMethod)
                .collect(Collectors.toList());
    }

    public static <T> List<T> makeSomeFakesLong(long amount, LongFunction<T> fakeCreationMethodForObjectsWithLongId) {
        return LongStream.rangeClosed(1, amount)
                .mapToObj(fakeCreationMethodForObjectsWithLongId)
                .collect(Collectors.toList());
    }

    // API V1

    public static Teacher makeFakeTeacher(Integer id) {
        return makeFakeTeacher(id, day(id), wage(id), wage(id), name(id), surname(id),
                phone(id), city(id), email(id), picture(id), document(id), comment(id));
    }

    public static Customer makeFakeCustomer(Integer id) {
        return makeFakeCustomer(id, localDate(id), name(id), phone(id), website(id), picture(id), comment(id));
    }

    public static Group makeFakeGroup(Integer id) {
        return makeFakeGroup(id, id, id, Boolean(id), languageLevel(id), name(id));
    }

    public static Student makeFakeStudent(Integer id) {
        return makeFakeStudent(id, id, localDate(id), email(id), name(id));
    }

    public static Lesson makeFakeLesson(Long id) {
        return makeFakeLesson(id, id(id), localDate(id), hour(id), minute(id), id(id), comment(id));
    }

    public static Partition makeFakePartition(Integer id) {
        return makeFakePartition(-id, name(id));
    }

    public static Account makeFakeAccount(Integer id, String username, AccountType accountType, String password) {
        return new Account(id, username, accountType.name(), password.toCharArray());
    }

    public static StudentsInGroup makeFakeStudentsInGroup(Integer groupId, Integer... studentIds) {
        return new StudentsInGroup(groupId, Arrays.asList(studentIds));
    }

    public static LessonAttendance makeFakeLessonAttendance(Long lessonId, Integer... studentIds) {
        return new LessonAttendance(lessonId, Arrays.asList(studentIds));
    }

    public static TeacherLanguages makeFakeTeacherLanguages(Integer teacherId, String... languages) {
        return new TeacherLanguages(teacherId, Arrays.asList(languages));
    }

    // FULL FAKES

    public static Teacher makeFakeTeacher(Integer id, Integer paymentDay, BigDecimal hourlyWage,  BigDecimal academicWage,
                                          String name, String surname, String phone, String city, String email,
                                          String picture, String document, String comment) {
        return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment);
    }

    public static Customer makeFakeCustomer(Integer id, LocalDate startDate, String name, String phone, String website, String picture, String comment) {
        return new Customer(id, startDate == null ? null : startDate.toString(), name, phone, website, picture, comment);
    }

    public static Group makeFakeGroup(Integer id, Integer customerId, Integer teacherId, boolean usesHourlyWage, String languageLevel, String name) {
        return new Group(id, customerId, teacherId, usesHourlyWage, languageLevel, name);
    }

    public static Student makeFakeStudent(Integer id, Integer customerId, LocalDate startDate, String email, String name) {
        return new Student(id, customerId, startDate == null ? null : startDate.toString(), email, name);
    }

    public static Lesson makeFakeLesson(Long id, Integer groupId, LocalDate startDate, Integer startHour, Integer startMinute, Integer length, String comment) {
        return Lesson.jsonInstance(id, groupId, null, "UTC", startDate == null ? null : startDate.toString(), startHour, startMinute, length, comment);
    }

    public static Partition makeFakePartition(Integer id, String name) {
        if (id >= 0)
            throw new IllegalArgumentException("While testing, only negative partition ids allowed for fake partitions!");
        return new Partition(id, name);
    }

    // PRIVATE

    private FakeUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
