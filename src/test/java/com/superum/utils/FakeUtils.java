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

import static com.superum.utils.FakeFieldUtils.*;

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

    public static Teacher makeFakeTeacher(int id) {
        return makeFakeTeacher(id, fakeDay(id), fakeWage(id), fakeWage(id), fakeName(id), fakeSurname(id),
                fakePhone(id), fakeCity(id), fakeEmail(id), fakePicture(id), fakeDocument(id), fakeComment(id));
    }

    public static Customer makeFakeCustomer(int id) {
        return makeFakeCustomer(id, fakeLocalDate(id), fakeName(id), fakePhone(id), fakeWebsite(id), fakePicture(id), fakeComment(id));
    }

    public static Group makeFakeGroup(int id) {
        return makeFakeGroup(id, id, id, fakeBoolean(id), fakeLanguageLevel(id), fakeName(id));
    }

    public static Student makeFakeStudent(int id) {
        return makeFakeStudent(id, id, fakeLocalDate(id), fakeEmail(id), fakeName(id));
    }

    public static Lesson makeFakeLesson(long id) {
        return makeFakeLesson(id, fakeId(id), fakeLocalDate(id), fakeHour(id), fakeMinute(id), fakeId(id), fakeComment(id));
    }

    public static Partition makeFakePartition(int id) {
        return makeFakePartition(-id, fakeName(id));
    }

    public static Account makeFakeAccount(int id, String username, AccountType accountType, String password) {
        return new Account(id, username, accountType.name(), password.toCharArray());
    }

    public static StudentsInGroup makeFakeStudentsInGroup(int groupId, Integer... studentIds) {
        return new StudentsInGroup(groupId, Arrays.asList(studentIds));
    }

    public static LessonAttendance makeFakeLessonAttendance(long lessonId, Integer... studentIds) {
        return new LessonAttendance(lessonId, Arrays.asList(studentIds));
    }

    public static TeacherLanguages makeFakeTeacherLanguages(int teacherId, String... languages) {
        return new TeacherLanguages(teacherId, Arrays.asList(languages));
    }

    // FULL FAKES

    public static Teacher makeFakeTeacher(int id, int paymentDay, BigDecimal hourlyWage,  BigDecimal academicWage,
                                          String name, String surname, String phone, String city, String email,
                                          String pictureName, String documentName, String comment) {
        return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, pictureName, documentName, comment);
    }

    public static Customer makeFakeCustomer(int id, LocalDate startDate, String name, String phone, String website, String picture, String comment) {
        return new Customer(id, startDate, name, phone, website, picture, comment);
    }

    public static Group makeFakeGroup(int id, Integer customerId, int teacherId, boolean usesHourlyWage, String languageLevel, String name) {
        return new Group(id, customerId, teacherId, usesHourlyWage, languageLevel, name);
    }

    public static Student makeFakeStudent(int id, Integer customerId, LocalDate startDate, String email, String name) {
        return new Student(id, customerId, startDate, email, name);
    }

    public static Lesson makeFakeLesson(long id, int groupId, LocalDate date, int startHour, int startMinute, int length, String comment) {
        return new Lesson(id, groupId, date, startHour, startMinute, length, comment);
    }

    public static Partition makeFakePartition(int id, String name) {
        if (id >= 0)
            throw new IllegalArgumentException("While testing, only negative partition ids allowed for fake partitions!");
        return new Partition(id, name);
    }

}
