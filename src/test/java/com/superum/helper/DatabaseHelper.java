package com.superum.helper;

import com.superum.api.customer.FullCustomer;
import com.superum.db.group.Group;
import com.superum.db.group.student.Student;
import com.superum.db.teacher.Teacher;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Tables.*;
import static env.IntegrationTestEnvironment.TEST_PARTITION;

@Repository
@Transactional
public class DatabaseHelper {

    public FullCustomer insertCustomerIntoDb(FullCustomer fullCustomer) {
        FullCustomer customer = sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, TEST_PARTITION)
                .set(CUSTOMER.NAME, fullCustomer.getName())
                .set(CUSTOMER.START_DATE, fullCustomer.getStartDateSQL())
                .set(CUSTOMER.PHONE, fullCustomer.getPhone())
                .set(CUSTOMER.WEBSITE, fullCustomer.getWebsite())
                .set(CUSTOMER.PICTURE, fullCustomer.getPicture())
                .set(CUSTOMER.COMMENT, fullCustomer.getComment())
                .returning()
                .fetchOne()
                .map(FullCustomer::valueOf);

        assert customer != null; //if this assert fails, database is broken/offline

        return customer;
    }

    public FullCustomer readCustomerFromDb(int customerId) {
        return sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetch().stream()
                .findFirst()
                .map(FullCustomer::valueOf)
                .orElse(null);
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

    // CONSTRUCTORS

    @Autowired
    public DatabaseHelper(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
