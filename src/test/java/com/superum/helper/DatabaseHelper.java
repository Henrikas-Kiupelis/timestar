package com.superum.helper;

import com.superum.api.customer.FullCustomer;
import com.superum.db.customer.Customer;
import com.superum.db.generated.timestar.tables.records.CustomerLangRecord;
import com.superum.db.group.Group;
import com.superum.db.group.student.Student;
import com.superum.db.teacher.Teacher;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.*;
import static env.IntegrationTestEnvironment.TEST_PARTITION;

@Repository
@Transactional
public class DatabaseHelper {

    public FullCustomer insertCustomerIntoDb(FullCustomer fullCustomer) {
        Customer customer = sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, TEST_PARTITION)
                .set(CUSTOMER.NAME, fullCustomer.getName())
                .set(CUSTOMER.PAYMENT_DAY, fullCustomer.getPaymentDay())
                .set(CUSTOMER.START_DATE, fullCustomer.getStartDate())
                .set(CUSTOMER.PAYMENT_VALUE, fullCustomer.getPaymentValue())
                .set(CUSTOMER.PHONE, fullCustomer.getPhone())
                .set(CUSTOMER.WEBSITE, fullCustomer.getWebsite())
                .set(CUSTOMER.PICTURE_NAME, fullCustomer.getPictureName())
                .set(CUSTOMER.COMMENT_ABOUT, fullCustomer.getComment())
                .returning(CUSTOMER.fields())
                .fetchOne()
                .map(Customer::valueOf);

        assert customer != null; //if this assert fails, database is broken/offline

        int id = customer.getId();

        InsertValuesStep3<CustomerLangRecord, Integer, Integer, String> step = sql.insertInto(CUSTOMER_LANG, CUSTOMER_LANG.PARTITION_ID, CUSTOMER_LANG.CUSTOMER_ID, CUSTOMER_LANG.LANGUAGE_LEVEL);
        for (String language : fullCustomer.getLanguages())
            step = step.values(TEST_PARTITION, id, language);

        step.execute();

        return fullCustomer.withId(id);
    }

    public FullCustomer readCustomerFromDb(int customerId) {
        Customer customer = sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetch().stream()
                .findFirst()
                .map(Customer::valueOf)
                .orElse(null);

        if (customer == null)
            return null;

        List<String> languages = sql.select(CUSTOMER_LANG.LANGUAGE_LEVEL)
                .from(CUSTOMER_LANG)
                .where(CUSTOMER_LANG.CUSTOMER_ID.eq(customerId))
                .fetch()
                .map(record -> record.getValue(CUSTOMER_LANG.LANGUAGE_LEVEL));

        return new FullCustomer(customer, languages);
    }

    public Teacher insertTeacherIntoDb(Teacher teacher) {
        Teacher insertedTeacher = sql.insertInto(TEACHER)
                .set(TEACHER.PARTITION_ID, TEST_PARTITION)
                .set(TEACHER.PAYMENT_DAY, teacher.getPaymentDay())
                .set(TEACHER.NAME, teacher.getName())
                .set(TEACHER.SURNAME, teacher.getSurname())
                .set(TEACHER.PHONE, teacher.getPhone())
                .set(TEACHER.CITY, teacher.getCity())
                .set(TEACHER.EMAIL, teacher.getEmail())
                .set(TEACHER.PICTURE_NAME, teacher.getPictureName())
                .set(TEACHER.DOCUMENT_NAME, teacher.getDocumentName())
                .set(TEACHER.COMMENT_ABOUT, teacher.getComment())
                .returning()
                .fetchOne()
                .map(Teacher::valueOf);

        assert insertedTeacher != null; //if this assert fails, database is broken/offline

        return insertedTeacher;
    }

    public Group insertGroupIntoDb(Group group) {
        Group insertedGroup = sql.insertInto(STUDENT_GROUP)
                .set(STUDENT_GROUP.PARTITION_ID, TEST_PARTITION)
                .set(STUDENT_GROUP.TEACHER_ID, group.getTeacherId())
                .set(STUDENT_GROUP.NAME, group.getName())
                .returning()
                .fetchOne()
                .map(Group::valueOf);

        assert insertedGroup != null; //if this assert fails, database is broken/offline

        return insertedGroup;
    }

    public Student insertStudentIntoDb(Student student) {
        Student insertedStudent = sql.insertInto(STUDENT)
                .set(STUDENT.PARTITION_ID, TEST_PARTITION)
                .set(STUDENT.GROUP_ID, student.getGroupId())
                .set(STUDENT.CUSTOMER_ID, student.getCustomerId())
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
