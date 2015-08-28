package com.superum.utils;

import com.superum.api.attendance.ValidLessonAttendanceDTO;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;
import com.superum.api.grouping.ValidGroupingDTO;
import com.superum.api.lesson.ValidLessonDTO;
import com.superum.api.student.ValidStudentDTO;
import com.superum.api.teacher.FullTeacherDTO;
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
import com.superum.helper.PartitionAccount;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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

    // API V2

    public static PartitionAccount makeFakePartitionAccount() {
        return new PartitionAccount(0, "test");
    }

    public static ValidCustomerDTO makeFakeValidCustomer(Integer id) {
        return ValidCustomerDTO.jsonInstance(id, fakeLocalDate(id).toString(), fakeName(id), fakePhone(id), fakeWebsite(id), fakePicture(id), fakeComment(id));
    }

    public static FullTeacherDTO makeFakeFullTeacher(Integer id) {
        return FullTeacherDTO.jsonInstance(id, fakeDay(id), fakeWage(id), fakeWage(id), fakeName(id), fakeSurname(id),
                fakePhone(id), fakeCity(id), fakeEmail(id), fakePicture(id), fakeDocument(id), fakeComment(id),
                Collections.singletonList(fakeLanguage(id)));
    }

    public static ValidGroupDTO makeFakeValidGroup(Integer id) {
        return ValidGroupDTO.jsonInstance(id, id, id, fakeBoolean(id), fakeLanguageLevel(id), fakeName(id));
    }

    public static ValidGroupDTO makeFakeValidGroup(Integer id, Integer customerId, Integer teacherId) {
        return makeFakeValidGroup(id, customerId, teacherId, fakeBoolean(id), fakeLanguageLevel(id), fakeName(id));
    }

    public static ValidStudentDTO makeFakeValidStudent(Integer id) {
        return ValidStudentDTO.jsonInstance(id, id, fakeLocalDate(id).toString(), fakeEmail(id), fakeName(id));
    }

    public static ValidGroupingDTO makeFakeValidGrouping(Integer id) {
        return ValidGroupingDTO.jsonInstance(id, Collections.singleton(id));
    }

    public static ValidLessonDTO makeFakeValidLesson(Long id) {
        return ValidLessonDTO.jsonInstance(id, id.intValue(), System.currentTimeMillis(), null, null, null, null, fakeDuration(id), fakeComment(id));
    }

    public static ValidLessonAttendanceDTO makeFakeValidAttendance(Long id) {
        return ValidLessonAttendanceDTO.jsonInstance(id, Collections.singleton(id.intValue()));
    }

    // FULL FAKES

    public static ValidCustomerDTO makeFakeValidCustomer(Integer id, LocalDate startDate, String name, String phone, String website, String picture, String comment) {
        return ValidCustomerDTO.jsonInstance(id, startDate == null ? null : startDate.toString(), name, phone, website, picture, comment);
    }


    public static FullTeacherDTO makeFakeFullTeacher(Integer id, Integer paymentDay, BigDecimal hourlyWage,
                                                  BigDecimal academicWage, String name, String surname, String phone,
                                                  String city, String email, String picture, String document,
                                                  String comment, String... languages) {
        return FullTeacherDTO.jsonInstance(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, Arrays.asList(languages));
    }

    public static FullTeacherDTO makeFakeFullTeacher(Integer id, Integer paymentDay, BigDecimal hourlyWage,
                                                  BigDecimal academicWage, String name, String surname, String phone,
                                                  String city, String email, String picture, String document,
                                                  String comment, List<String> languages) {
        return FullTeacherDTO.jsonInstance(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages);
    }

    public static ValidGroupDTO makeFakeValidGroup(Integer id, Integer customerId, Integer teacherId, Boolean usesHourlyWage, String languageLevel, String name) {
        return ValidGroupDTO.jsonInstance(id, customerId, teacherId, usesHourlyWage, languageLevel, name);
    }

    // API V1

    public static Teacher makeFakeTeacher(Integer id) {
        return makeFakeTeacher(id, fakeDay(id), fakeWage(id), fakeWage(id), fakeName(id), fakeSurname(id),
                fakePhone(id), fakeCity(id), fakeEmail(id), fakePicture(id), fakeDocument(id), fakeComment(id));
    }

    public static Customer makeFakeCustomer(Integer id) {
        return makeFakeCustomer(id, fakeLocalDate(id), fakeName(id), fakePhone(id), fakeWebsite(id), fakePicture(id), fakeComment(id));
    }

    public static Group makeFakeGroup(Integer id) {
        return makeFakeGroup(id, id, id, fakeBoolean(id), fakeLanguageLevel(id), fakeName(id));
    }

    public static Student makeFakeStudent(Integer id) {
        return makeFakeStudent(id, id, fakeLocalDate(id), fakeEmail(id), fakeName(id));
    }

    public static Lesson makeFakeLesson(Long id) {
        return makeFakeLesson(id, fakeId(id), fakeLocalDate(id), fakeHour(id), fakeMinute(id), fakeId(id), fakeComment(id));
    }

    public static Partition makeFakePartition(Integer id) {
        return makeFakePartition(-id, fakeName(id));
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
