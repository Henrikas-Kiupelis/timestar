package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.quick.QuickQueries;
import com.superum.api.teacher.FullTeacherDAO;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.helper.jooq.ConditionBuilder;
import com.superum.helper.jooq.QuickForeignKeyUsageChecker;
import com.superum.helper.jooq.QuickIdExistenceChecker;
import com.superum.helper.jooq.UpdateBuilder;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.*;
import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class FullCustomerQueriesImpl extends QuickQueries<CustomerRecord, Integer> implements FullCustomerQueries {

    @Override
    public int exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasAnyFieldsSet())
            throw new InvalidCustomerException("Provided customer does not have any fields set, so checking if it exists is impossible");

        if (customer.hasOnlyId()) {
            int customerId = customer.getId();
            if (!existsQuick(customerId, partitionId))
                throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

            return customerId;
        }

        return sql.select(CUSTOMER.ID)
                .from(CUSTOMER)
                .where(partialCustomerCondition(customer, partitionId))
                .fetch().stream()
                .findFirst()
                .map(record -> record.getValue(CUSTOMER.ID))
                .orElseThrow(() -> new CustomerNotFoundException("Couldn't find this customer: " + customer));
    }

    @Override
    public int updatePartial(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasId())
            throw new InvalidCustomerException("Provided customer doesn't have its id set; please set it or use /create instead!");

        if (!customer.canUpdateCustomer())
            throw new InvalidCustomerException("Provided customer only has its id set; to update this customer, set additional fields!");

        int customerId = customer.getId();

        return partialCustomerUpdateStatement(customer)
                .where(idAndPartition(customerId, partitionId))
                .execute();
    }

    @Override
    public int safeDelete(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        if (!existsQuick(customerId, partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

        if (usedQuick(customerId, partitionId))
            throw new UnsafeCustomerDeleteException("Cannot delete customer with " + customerId +
                    " while it still has entries in other tables");

        return sql.delete(CUSTOMER)
                .where(idAndPartition(customerId, partitionId))
                .execute();
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        if (!teacherExists(teacherId, partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        return sql.select(CUSTOMER.fields())
                .from(CUSTOMER)
                .join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .groupBy(CUSTOMER.ID)
                .orderBy(CUSTOMER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(FullCustomer::valueOf);
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        return sql.select(CUSTOMER.fields())
                .from(CUSTOMER)
                .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                .groupBy(CUSTOMER.ID)
                .orderBy(CUSTOMER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(FullCustomer::valueOf);
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (!teacherExists(teacherId, partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        return sql.fetchCount(sql.selectOne()
                .from(CUSTOMER)
                .join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .groupBy(CUSTOMER.ID));
    }

    @Override
    public int count(int partitionId) {
        return sql.fetchCount(CUSTOMER, CUSTOMER.PARTITION_ID.eq(partitionId));
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DSLContext sql, FullTeacherDAO fullTeacherDAO) {
        super(new QuickIdExistenceChecker<>(sql, CUSTOMER.ID, CUSTOMER.PARTITION_ID),
                QuickForeignKeyUsageChecker.stepBuilder(Integer.class)
                        .withDSLContext(sql)
                        .add(STUDENT.CUSTOMER_ID, STUDENT.PARTITION_ID)
                        .add(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.PARTITION_ID)
                        .build());

        this.sql = sql;
        this.fullTeacherDAO = fullTeacherDAO;
    }

    // PRIVATE

    private final DSLContext sql;
    private final FullTeacherDAO fullTeacherDAO;

    /**
     * @return Condition which checks for customer id and partition id in CUSTOMER table
     */
    private Condition idAndPartition(int customerId, int partitionId) {
        return CUSTOMER.ID.eq(customerId).and(CUSTOMER.PARTITION_ID.eq(partitionId));
    }

    /**
     * <pre>
     * Tries to use the quick version if it is available (should be the case, but with Spring you never know...)
     * </pre>
     * @return true if a teacher with given teacherId exists in a given partition, false otherwise
     */
    private boolean teacherExists(int teacherId, int partitionId) {
        if (fullTeacherDAO instanceof QuickQueries) {
            @SuppressWarnings("unchecked")
            QuickQueries<TeacherRecord, Integer> teacherQuickDAO = (QuickQueries<TeacherRecord, Integer>) fullTeacherDAO;
            return teacherQuickDAO.existsQuick(teacherId, partitionId);
        }

        return true; // TODO - implement with a full teacher builder
    }

    /**
     * @return Condition which checks for all set Customer fields
     */
    private Condition partialCustomerCondition(FullCustomer customer, int partitionId) {
        return new ConditionBuilder<>(customer, CUSTOMER.PARTITION_ID, partitionId)
                .fieldCheck(FullCustomer::hasId, CUSTOMER.ID, FullCustomer::getId)
                .fieldCheck(FullCustomer::hasStartDate, CUSTOMER.START_DATE, FullCustomer::getStartDateSQL)
                .fieldCheck(FullCustomer::hasName, CUSTOMER.NAME, FullCustomer::getName)
                .fieldCheck(FullCustomer::hasPhone, CUSTOMER.PHONE, FullCustomer::getPhone)
                .fieldCheck(FullCustomer::hasWebsite, CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .fieldCheck(FullCustomer::hasPictureName, CUSTOMER.PICTURE, FullCustomer::getPicture)
                .fieldCheck(FullCustomer::hasComment, CUSTOMER.COMMENT, FullCustomer::getComment)
                .finalCondition();
    }

    /**
     * @return UpdateSetMoreStep which updates all set Customer fields (except id)
     */
    private UpdateSetMoreStep partialCustomerUpdateStatement(FullCustomer customer) {
        return new UpdateBuilder<>(customer, sql, CUSTOMER)
                .setField(FullCustomer::hasStartDate, CUSTOMER.START_DATE, FullCustomer::getStartDateSQL)
                .setField(FullCustomer::hasName, CUSTOMER.NAME, FullCustomer::getName)
                .setField(FullCustomer::hasPhone, CUSTOMER.PHONE, FullCustomer::getPhone)
                .setField(FullCustomer::hasWebsite, CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .setField(FullCustomer::hasPictureName, CUSTOMER.PICTURE, FullCustomer::getPicture)
                .setField(FullCustomer::hasComment, CUSTOMER.COMMENT, FullCustomer::getComment)
                .finalStep();
    }

}
