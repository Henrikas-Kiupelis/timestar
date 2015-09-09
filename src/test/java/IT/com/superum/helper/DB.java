package IT.com.superum.helper;

import com.google.common.collect.ObjectArrays;
import com.superum.api.attendance.ValidLessonAttendanceDTO;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;
import com.superum.api.grouping.ValidGroupingDTO;
import com.superum.api.lesson.ValidLesson;
import com.superum.api.lesson.ValidLessonDTO;
import com.superum.api.student.ValidStudentDTO;
import com.superum.api.teacher.FullTeacherDTO;
import com.superum.helper.Fakes;
import eu.goodlike.libraries.jodatime.Time;
import eu.goodlike.test.Fake;
import org.joda.time.LocalDate;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.lambda.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static IT.com.superum.helper.TestConstants.TEST_PARTITION;
import static com.superum.db.generated.timestar.Keys.TEACHER_LANGUAGE_IBFK_1;
import static com.superum.db.generated.timestar.Tables.*;
import static org.jooq.impl.DSL.groupConcat;

@Component
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class DB {

    // INIT

    public void init() {
        insertCustomers();
        insertTeachers();
        insertGroups();
        insertStudents();
        insertGrouping();
        insertLessons();
        insertAttendance();
    }

    /**
     * <pre>
     * Occasionally some straggler accounts remain in the DB despite transaction rollback... needs investigation!
     * EDIT: Seems to have been a concurrency issue, looks to be solved
     * </pre>
     */
    public void clean() {
        cleanAccounts();
    }

    // CREATE

    public FullTeacherDTO insertFullTeacher(FullTeacherDTO teacher) {
        sql.insertInto(TEACHER)
                .set(TEACHER.PARTITION_ID, TEST_PARTITION)
                .set(TEACHER.ID, teacher.getId())
                .set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
                .set(TEACHER.HOURLY_WAGE, teacher.getHourlyWage())
                .set(TEACHER.ACADEMIC_WAGE, teacher.getAcademicWage())
                .set(TEACHER.NAME, teacher.getName())
                .set(TEACHER.SURNAME, teacher.getSurname())
                .set(TEACHER.PHONE, teacher.getPhone())
                .set(TEACHER.CITY, teacher.getCity())
                .set(TEACHER.EMAIL, teacher.getEmail())
                .set(TEACHER.PICTURE, teacher.getPicture())
                .set(TEACHER.DOCUMENT, teacher.getDocument())
                .set(TEACHER.COMMENT, teacher.getComment())
                .execute();

        Seq.seq(teacher.getLanguages())
                .foldLeft(sql.insertInto(TEACHER_LANGUAGE, TEACHER_LANGUAGE.PARTITION_ID, TEACHER_LANGUAGE.TEACHER_ID, TEACHER_LANGUAGE.CODE),
                        (step, language) -> step.values(TEST_PARTITION, teacher.getId(), language))
                .execute();

        return readFullTeacher(teacher.getId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert teacer"));
    }

    public ValidCustomerDTO insertValidCustomer(ValidCustomerDTO customer) {
        sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, TEST_PARTITION)
                .set(CUSTOMER.ID, customer.getId())
                .set(CUSTOMER.NAME, customer.getName())
                .set(CUSTOMER.START_DATE, java.sql.Date.valueOf(customer.getStartDateString()))
                .set(CUSTOMER.PHONE, customer.getPhone())
                .set(CUSTOMER.WEBSITE, customer.getWebsite())
                .set(CUSTOMER.PICTURE, customer.getPicture())
                .set(CUSTOMER.COMMENT, customer.getComment())
                .execute();

        return readValidCustomer(customer.getId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert customer"));
    }

    public ValidGroupDTO insertValidGroup(ValidGroupDTO group) {
        sql.insertInto(GROUP_OF_STUDENTS)
                .set(GROUP_OF_STUDENTS.PARTITION_ID, TEST_PARTITION)
                .set(GROUP_OF_STUDENTS.ID, group.getId())
                .set(GROUP_OF_STUDENTS.CUSTOMER_ID, group.getCustomerId())
                .set(GROUP_OF_STUDENTS.TEACHER_ID, group.getTeacherId())
                .set(GROUP_OF_STUDENTS.USE_HOURLY_WAGE, group.getUsesHourlyWage())
                .set(GROUP_OF_STUDENTS.LANGUAGE_LEVEL, group.getLanguageLevel())
                .set(GROUP_OF_STUDENTS.NAME, group.getName())
                .execute();

        return readValidGroup(group.getId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert group"));
    }

    public ValidStudentDTO insertValidStudent(ValidStudentDTO student) {
        sql.insertInto(STUDENT)
                .set(STUDENT.PARTITION_ID, TEST_PARTITION)
                .set(STUDENT.ID, student.getId())
                .set(STUDENT.CUSTOMER_ID, student.getCustomerId())
                .set(STUDENT.CODE, student.getCode())
                .set(STUDENT.START_DATE, toSql(student.getStartDate()))
                .set(STUDENT.EMAIL, student.getEmail())
                .set(STUDENT.NAME, student.getName())
                .execute();

        return readValidStudent(student.getId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert student"));
    }

    public ValidLessonDTO insertValidLesson(ValidLessonDTO lesson) {
        sql.insertInto(LESSON)
                .set(LESSON.PARTITION_ID, TEST_PARTITION)
                .set(LESSON.ID, lesson.getId())
                .set(LESSON.GROUP_ID, lesson.getGroupId())
                .set(LESSON.TIME_OF_START, lesson.getStartTime())
                .set(LESSON.TIME_OF_END, ValidLesson.calculateEndTime(lesson.getStartTime(), lesson.getLength()))
                .set(LESSON.DURATION_IN_MINUTES, lesson.getLength())
                .set(LESSON.COMMENT, lesson.getComment())
                .execute();

        return readValidLesson(lesson.getId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert lesson"));
    }

    public ValidGroupingDTO insertValidGrouping(ValidGroupingDTO grouping) {
        Seq.seq(grouping.getStudentIds())
                .foldLeft(sql.insertInto(STUDENTS_IN_GROUPS,
                                STUDENTS_IN_GROUPS.PARTITION_ID, STUDENTS_IN_GROUPS.GROUP_ID, STUDENTS_IN_GROUPS.STUDENT_ID),
                        (step, studentId) -> step.values(TEST_PARTITION, grouping.getGroupId(), studentId))
                .execute();

        return readValidGrouping(grouping.getGroupId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert grouping"));
    }

    public ValidLessonAttendanceDTO insertValidLessonAttendance(ValidLessonAttendanceDTO attendance) {
        Seq.seq(attendance.getStudentIds())
                .foldLeft(sql.insertInto(LESSON_ATTENDANCE,
                                LESSON_ATTENDANCE.PARTITION_ID, LESSON_ATTENDANCE.LESSON_ID, LESSON_ATTENDANCE.STUDENT_ID),
                        (step, studentId) -> step.values(TEST_PARTITION, attendance.getLessonId(), studentId))
                .execute();

        return readValidLessonAttendance(attendance.getLessonId())
                .orElseThrow(() -> new RuntimeException("Couldn't insert attendance"));
    }

    public void insertValidAccount(FullTeacherDTO teacher) {
        sql.insertInto(ACCOUNT)
                .set(ACCOUNT.USERNAME, TEST_PARTITION + "." + teacher.getEmail())
                .set(ACCOUNT.ID, teacher.getId())
                .set(ACCOUNT.PASSWORD, FILLER_PASSWORD)
                .set(ACCOUNT.ACCOUNT_TYPE, TEACHER_TYPE)
                .execute();
    }

    // READ

    private static final Field<?>[] FULL_TEACHER_FIELDS = ObjectArrays.concat(TEACHER.fields(),
            groupConcat(TEACHER_LANGUAGE.CODE).as(FullTeacherDTO.getLanguagesFieldName()));
    public Optional<FullTeacherDTO> readFullTeacher(int teacherId) {
        return sql.select(FULL_TEACHER_FIELDS).from(TEACHER)
                .join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1)
                .where(TEACHER.ID.eq(teacherId))
                .groupBy(TEACHER.ID)
                .fetch().stream()
                .findFirst()
                .map(FullTeacherDTO::valueOf);
    }

    public Optional<ValidCustomerDTO> readValidCustomer(int customerId) {
        return sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetch().stream().findAny()
                .map(ValidCustomerDTO::valueOf);
    }

    public Optional<ValidGroupDTO> readValidGroup(int groupId) {
        return sql.selectFrom(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.ID.eq(groupId))
                .fetch().stream().findAny()
                .map(ValidGroupDTO::valueOf);
    }

    public Optional<ValidStudentDTO> readValidStudent(int studentId) {
        return sql.selectFrom(STUDENT)
                .where(STUDENT.ID.eq(studentId))
                .fetch().stream().findAny()
                .map(ValidStudentDTO::valueOf);
    }

    public Optional<ValidLessonDTO> readValidLesson(long lessonId) {
        return sql.selectFrom(LESSON)
                .where(LESSON.ID.eq(lessonId))
                .fetch().stream().findAny()
                .map(ValidLessonDTO::valueOf);
    }

    public Optional<ValidGroupingDTO> readValidGrouping(int groupId) {
        Set<Integer> studentIds = sql.select(STUDENTS_IN_GROUPS.STUDENT_ID)
                .from(STUDENTS_IN_GROUPS)
                .where(STUDENTS_IN_GROUPS.GROUP_ID.eq(groupId))
                .fetch().stream()
                .map(record -> record.getValue(STUDENTS_IN_GROUPS.STUDENT_ID))
                .collect(Collectors.toSet());

        return studentIds.isEmpty() ? Optional.empty() : Optional.of(new ValidGroupingDTO(groupId, studentIds));
    }

    public boolean existsGroupingForStudentId(int studentId) {
        return sql.fetchExists(STUDENTS_IN_GROUPS, STUDENTS_IN_GROUPS.STUDENT_ID.eq(studentId));
    }

    public Optional<ValidLessonAttendanceDTO> readValidLessonAttendance(long lessonId) {
        Set<Integer> studentIds = sql.select(LESSON_ATTENDANCE.STUDENT_ID)
                .from(LESSON_ATTENDANCE)
                .where(LESSON_ATTENDANCE.LESSON_ID.eq(lessonId))
                .fetch().stream()
                .map(record -> record.getValue(LESSON_ATTENDANCE.STUDENT_ID))
                .collect(Collectors.toSet());

        return studentIds.isEmpty() ? Optional.empty() : Optional.of(new ValidLessonAttendanceDTO(lessonId, studentIds));
    }

    public boolean existsLessonAttendanceForStudentId(int studentId) {
        return sql.fetchExists(LESSON_ATTENDANCE, LESSON_ATTENDANCE.STUDENT_ID.eq(studentId));
    }

    // CONSTRUCTORS

    @Autowired
    public DB(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private void cleanAccounts() {
        sql.deleteFrom(ACCOUNT)
                .where(ACCOUNT.ID.isNotNull())
                .execute();
    }

    private java.sql.Date toSql(LocalDate startDate) {
        return startDate == null ? null : Time.convert(startDate).toSqlDate();
    }

    private void insertCustomers() {
        Fake.someOf(Fakes::customer, 2)
                .forEach(this::insertValidCustomer);
    }

    private void insertTeachers() {
        Fake.someOf(Fakes::teacher, 2)
                .forEach(teacher -> {
                    insertFullTeacher(teacher);
                    insertValidAccount(teacher);
                });
    }

    private void insertGroups() {
        Fake.someOf(Fakes::group, 2)
                .forEach(this::insertValidGroup);
    }

    private void insertStudents() {
        Fake.someOf(Fakes::student, 2)
                .forEach(this::insertValidStudent);
    }

    private void insertGrouping() {
        Fake.someOf(Fakes::grouping, 2)
                .forEach(this::insertValidGrouping);
    }

    private void insertLessons() {
        Fake.someOfLong(Fakes::lesson, 2)
                .forEach(this::insertValidLesson);
    }

    private void insertAttendance() {
        Fake.someOfLong(Fakes::lessonAttendance, 2)
                .forEach(this::insertValidLessonAttendance);
    }

    private static final String FILLER_PASSWORD = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private static final String TEACHER_TYPE = "TEACHER";

}
