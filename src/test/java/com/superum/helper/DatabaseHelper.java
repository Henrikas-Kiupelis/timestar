package com.superum.helper;

import com.google.common.collect.ObjectArrays;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;
import com.superum.api.teacher.FullTeacherDTO;
import com.superum.db.account.AccountType;
import com.superum.db.generated.timestar.tables.records.TeacherLanguageRecord;
import com.superum.db.group.Group;
import com.superum.db.group.student.Student;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.TeacherLanguages;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.TEACHER_LANGUAGE_IBFK_1;
import static com.superum.db.generated.timestar.Tables.*;
import static com.superum.utils.FakeFieldUtils.fakePassword;
import static com.superum.utils.FakeUtils.makeFakePartitionAccount;
import static env.IntegrationTestEnvironment.TEST_PARTITION;
import static org.jooq.impl.DSL.groupConcat;

@Repository
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class DatabaseHelper {

    public ValidCustomerDTO insertFullCustomerIntoDb(ValidCustomerDTO validCustomerDTO) {
        ValidCustomerDTO customer = sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, TEST_PARTITION)
                .set(CUSTOMER.NAME, validCustomerDTO.getName())
                .set(CUSTOMER.START_DATE, java.sql.Date.valueOf(validCustomerDTO.getStartDateString()))
                .set(CUSTOMER.PHONE, validCustomerDTO.getPhone())
                .set(CUSTOMER.WEBSITE, validCustomerDTO.getWebsite())
                .set(CUSTOMER.PICTURE, validCustomerDTO.getPicture())
                .set(CUSTOMER.COMMENT, validCustomerDTO.getComment())
                .returning()
                .fetchOne()
                .map(ValidCustomerDTO::valueOf);

        assert customer != null; //if this assert fails, database is broken/offline

        return customer;
    }

    public ValidCustomerDTO readFullCustomerFromDb(int customerId) {
        return sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetch().stream().findAny()
                .map(ValidCustomerDTO::valueOf)
                .orElse(null);
    }

    public void deleteFullCustomerFromDb(int customerId) {
        sql.deleteFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .execute();
    }

    public Teacher insertTeacherIntoDb(Teacher teacher) {
        Teacher insertedTeacher = sql.insertInto(TEACHER)
                .set(TEACHER.PARTITION_ID, TEST_PARTITION)
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
                .returning()
                .fetchOne()
                .map(Teacher::valueOf);

        assert insertedTeacher != null; //if this assert fails, database is broken/offline

        return insertedTeacher;
    }

    public void deleteTeacherFromDb(int teacherId) {
        sql.deleteFrom(TEACHER)
                .where(TEACHER.ID.eq(teacherId))
                .execute();
    }

    public TeacherLanguages insertTeacherLanguagesIntoDb(TeacherLanguages teacherLanguages) {
        Integer teacherId = teacherLanguages.getTeacherId();
        List<String> languageList = teacherLanguages.getLanguages();

        InsertValuesStep3<TeacherLanguageRecord, Integer, Integer, String> step =
                sql.insertInto(TEACHER_LANGUAGE, TEACHER_LANGUAGE.PARTITION_ID, TEACHER_LANGUAGE.TEACHER_ID, TEACHER_LANGUAGE.CODE);
        for (String language : languageList)
            step = step.values(TEST_PARTITION, teacherId, language);

        step.execute();
        return teacherLanguages;
    }

    public FullTeacherDTO insertFullTeacherIntoDb(FullTeacherDTO teacher) {
        Teacher insertedTeacher = sql.insertInto(TEACHER)
                .set(TEACHER.PARTITION_ID, TEST_PARTITION)
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
                .returning()
                .fetchOne()
                .map(Teacher::valueOf);

        assert insertedTeacher != null; //if this assert fails, database is broken/offline

        InsertValuesStep3<TeacherLanguageRecord, Integer, Integer, String> step =
                sql.insertInto(TEACHER_LANGUAGE, TEACHER_LANGUAGE.PARTITION_ID, TEACHER_LANGUAGE.TEACHER_ID, TEACHER_LANGUAGE.CODE);
        for (String language : teacher.getLanguages())
            step = step.values(TEST_PARTITION, insertedTeacher.getId(), language);

        step.execute();

        return readFullTeacherFromDb(insertedTeacher.getId());
    }

    public void insertAccountIntoDb(FullTeacherDTO teacher) {
        PartitionAccount partitionAccount = makeFakePartitionAccount();
        String username = teacher.getEmail();
        String accountUsername = partitionAccount.usernameFor(username);

        sql.insertInto(ACCOUNT)
                .set(ACCOUNT.ID, teacher.getId())
                .set(ACCOUNT.USERNAME, accountUsername)
                .set(ACCOUNT.ACCOUNT_TYPE, AccountType.TEACHER.name())
                .set(ACCOUNT.PASSWORD, fakePassword())
                .execute();
    }

    public FullTeacherDTO readFullTeacherFromDb(int teacherId) {
        Field<?>[] fullTeacherFields = ObjectArrays.concat(TEACHER.fields(),
                groupConcat(TEACHER_LANGUAGE.CODE).as(FullTeacherDTO.getLanguagesFieldName()));
        return sql.select(fullTeacherFields).from(TEACHER)
                .join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1)
                .where(TEACHER.ID.eq(teacherId))
                .groupBy(TEACHER.ID)
                .fetch().stream()
                .findFirst()
                .map(FullTeacherDTO::valueOf)
                .orElse(null);
    }

    public Group insertGroupIntoDb(Group group) {
        Group insertedGroup = sql.insertInto(GROUP_OF_STUDENTS)
                .set(GROUP_OF_STUDENTS.PARTITION_ID, TEST_PARTITION)
                .set(GROUP_OF_STUDENTS.CUSTOMER_ID, group.getCustomerId())
                .set(GROUP_OF_STUDENTS.TEACHER_ID, group.getTeacherId())
                .set(GROUP_OF_STUDENTS.USE_HOURLY_WAGE, group.getUsesHourlyWage())
                .set(GROUP_OF_STUDENTS.LANGUAGE_LEVEL, group.getLanguageLevel())
                .set(GROUP_OF_STUDENTS.NAME, group.getName())
                .returning()
                .fetchOne()
                .map(Group::valueOf);

        assert insertedGroup != null; //if this assert fails, database is broken/offline

        return insertedGroup;
    }

    public Student insertStudentIntoDb(Student student) {
        Student insertedStudent = sql.insertInto(STUDENT)
                .set(STUDENT.PARTITION_ID, TEST_PARTITION)
                .set(STUDENT.CUSTOMER_ID, student.getCustomerId())
                .set(STUDENT.START_DATE, student.getStartDateSql())
                .set(STUDENT.EMAIL, student.getEmail())
                .set(STUDENT.NAME, student.getName())
                .returning()
                .fetchOne()
                .map(Student::valueOf);

        assert insertedStudent != null; //if this assert fails, database is broken/offline

        return insertedStudent;
    }

    public ValidGroupDTO insertValidGroupIntoDb(ValidGroupDTO group) {
        ValidGroupDTO insertedGroup = sql.insertInto(GROUP_OF_STUDENTS)
                .set(GROUP_OF_STUDENTS.PARTITION_ID, TEST_PARTITION)
                .set(GROUP_OF_STUDENTS.CUSTOMER_ID, group.getCustomerId())
                .set(GROUP_OF_STUDENTS.TEACHER_ID, group.getTeacherId())
                .set(GROUP_OF_STUDENTS.USE_HOURLY_WAGE, group.getUsesHourlyWage())
                .set(GROUP_OF_STUDENTS.LANGUAGE_LEVEL, group.getLanguageLevel())
                .set(GROUP_OF_STUDENTS.NAME, group.getName())
                .returning()
                .fetchOne()
                .map(ValidGroupDTO::valueOf);

        assert insertedGroup != null; //if this assert fails, database is broken/offline

        return insertedGroup;
    }

    public ValidGroupDTO readValidGroupFromDb(int groupId) {
        return sql.selectFrom(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.ID.eq(groupId))
                .fetch().stream().findAny()
                .map(ValidGroupDTO::valueOf)
                .orElse(null);
    }

    // CONSTRUCTORS

    @Autowired
    public DatabaseHelper(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
