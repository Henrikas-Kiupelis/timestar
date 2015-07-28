package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.quick.QuickQueries;
import com.superum.api.teacher.FullTeacherDAO;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.db.customer.lang.CustomerLanguagesDAO;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.exception.DatabaseException;
import com.superum.helper.ConditionBuilder;
import com.superum.helper.QuickForeignKeyUsageChecker;
import com.superum.helper.QuickIdExistenceChecker;
import com.superum.helper.UpdateBuilder;
import com.superum.utils.ArrayUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.UpdateSetMoreStep;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.*;
import static com.superum.db.generated.timestar.Tables.*;
import static org.jooq.impl.DSL.groupConcat;

@Repository
@Transactional
public class FullCustomerQueriesImpl extends QuickQueries<CustomerRecord, Integer> implements FullCustomerQueries {

    @Override
    public int exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasAnyCustomerFieldsSet())
            throw new InvalidCustomerException("Provided customer does not have any customer fields set, so checking if it exists is impossible");

        try {
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
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to check if this customer exists: " + customer, e);
        }
    }

    @Override
    public void updatePartial(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasId())
            throw new InvalidCustomerException("Provided customer doesn't have its id set; please set it or use /create instead!");

        boolean canUpdateCustomerLanguages = customer.canUpdateCustomerLanguages();
        boolean canUpdateCustomer = customer.canUpdateCustomer();
        if (!canUpdateCustomerLanguages && !canUpdateCustomer)
            throw new InvalidCustomerException("Provided customer only has its id set; to update this customer, set additional fields!");

        try {
            Condition condition = idAndPartition(customer.getId(), partitionId);

            if (canUpdateCustomer) {
                int updateResult = partialCustomerUpdateStatement(customer)
                        .where(condition)
                        .execute();
                if (updateResult == 0)
                    throw new DatabaseException("Couldn't update customer: " + customer);
            }

            if (canUpdateCustomerLanguages) {
                sql.delete(CUSTOMER_LANG)
                        .where(condition)
                        .execute();

                customerLanguagesDAO.create(customer.toCustomerLanguages(), partitionId);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update customer: " + customer, e);
        }
    }

    @Override
    public void safeDelete(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        if (!existsQuick(customerId, partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

        if (usedQuick(customerId, partitionId))
            throw new UnsafeCustomerDeleteException("Cannot delete customer with " + customerId +
                    " while it still has entries in other tables");

        try {
            sql.delete(CUSTOMER_LANG)
                    .where(idAndPartition(customerId, partitionId))
                    .execute();

            sql.delete(CUSTOMER)
                    .where(idAndPartition(customerId, partitionId))
                    .execute();
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete customer with id " + customerId, e);
        }
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

        try {
            return sql.select(FULL_CUSTOMER_FIELDS)
                    .from(CUSTOMER)
                    .join(CUSTOMER_LANG).onKey(CUSTOMER_LANG_IBFK_1)
                    .join(STUDENT).onKey(STUDENT_IBFK_2)
                    .join(STUDENT_GROUP).onKey(STUDENT_IBFK_1)
                    .where(STUDENT_GROUP.TEACHER_ID.eq(teacherId)
                            .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                    .groupBy(CUSTOMER.ID)
                    .orderBy(CUSTOMER.ID)
                    .limit(amount)
                    .offset(page * amount)
                    .fetch()
                    .map(FullCustomer::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers for teacher with id " + teacherId, e);
        }
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        try {
            return sql.select(FULL_CUSTOMER_FIELDS)
                    .from(CUSTOMER)
                    .join(CUSTOMER_LANG).onKey(CUSTOMER_LANG_IBFK_1)
                    .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                    .groupBy(CUSTOMER.ID)
                    .orderBy(CUSTOMER.ID)
                    .limit(amount)
                    .offset(page * amount)
                    .fetch()
                    .map(FullCustomer::valueOf);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all customers", e);
        }
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (!teacherExists(teacherId, partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        try {
            return sql.fetchCount(
                    sql.selectOne()
                        .from(CUSTOMER)
                        .join(STUDENT).onKey(STUDENT_IBFK_2)
                        .join(STUDENT_GROUP).onKey(STUDENT_IBFK_1)
                        .where(STUDENT_GROUP.TEACHER_ID.eq(teacherId)
                                .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                        .groupBy(CUSTOMER.ID));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to count all customers for teacher with id " + teacherId, e);
        }
    }

    @Override
    public int count(int partitionId) {
        try {
            return sql.fetchCount(CUSTOMER, CUSTOMER.PARTITION_ID.eq(partitionId));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to count all customers", e);
        }
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DSLContext sql, CustomerLanguagesDAO customerLanguagesDAO, FullTeacherDAO fullTeacherDAO) {
        super(new QuickIdExistenceChecker<>(sql, CUSTOMER.ID, CUSTOMER.PARTITION_ID),
                QuickForeignKeyUsageChecker.newRequiredBuilder(Integer.class)
                        .withDSLContext(sql)
                        .add(STUDENT.CUSTOMER_ID, STUDENT.PARTITION_ID)
                        .build());

        this.sql = sql;
        this.customerLanguagesDAO = customerLanguagesDAO;
        this.fullTeacherDAO = fullTeacherDAO;
    }

    // PRIVATE

    private final DSLContext sql;
    private final CustomerLanguagesDAO customerLanguagesDAO;
    private final FullTeacherDAO fullTeacherDAO;

    /**
     * @return Condition which checks for customer id and partition id
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

        return false; // TODO - implement with a full teacher builder
    }

    /**
     * @return Condition which checks for all set Customer fields
     */
    private Condition partialCustomerCondition(FullCustomer customer, int partitionId) {
        return new ConditionBuilder<>(customer, CUSTOMER.PARTITION_ID, partitionId)
                .fieldCheck(FullCustomer::hasId, CUSTOMER.ID, FullCustomer::getId)
                .fieldCheck(FullCustomer::hasPaymentDay, CUSTOMER.PAYMENT_DAY, FullCustomer::getPaymentDay)
                .fieldCheck(FullCustomer::hasStartDate, CUSTOMER.START_DATE, FullCustomer::getStartDate)
                .fieldCheck(FullCustomer::hasPaymentValue, CUSTOMER.PAYMENT_VALUE, FullCustomer::getPaymentValue)
                .fieldCheck(FullCustomer::hasName, CUSTOMER.NAME, FullCustomer::getName)
                .fieldCheck(FullCustomer::hasPhone, CUSTOMER.PHONE, FullCustomer::getPhone)
                .fieldCheck(FullCustomer::hasWebsite, CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .fieldCheck(FullCustomer::hasPictureName, CUSTOMER.PICTURE_NAME, FullCustomer::getPictureName)
                .fieldCheck(FullCustomer::hasComment, CUSTOMER.COMMENT_ABOUT, FullCustomer::getComment)
                .finalCondition();
    }

    /**
     * @return UpdateSetMoreStep which updates all set Customer fields (except id)
     */
    private UpdateSetMoreStep partialCustomerUpdateStatement(FullCustomer customer) {
        return new UpdateBuilder<>(customer, sql, CUSTOMER)
                .setField(FullCustomer::hasPaymentDay, CUSTOMER.PAYMENT_DAY, FullCustomer::getPaymentDay)
                .setField(FullCustomer::hasStartDate, CUSTOMER.START_DATE, FullCustomer::getStartDate)
                .setField(FullCustomer::hasPaymentValue, CUSTOMER.PAYMENT_VALUE, FullCustomer::getPaymentValue)
                .setField(FullCustomer::hasName, CUSTOMER.NAME, FullCustomer::getName)
                .setField(FullCustomer::hasPhone, CUSTOMER.PHONE, FullCustomer::getPhone)
                .setField(FullCustomer::hasWebsite, CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .setField(FullCustomer::hasPictureName, CUSTOMER.PICTURE_NAME, FullCustomer::getPictureName)
                .setField(FullCustomer::hasComment, CUSTOMER.COMMENT_ABOUT, FullCustomer::getComment)
                .finalStep();
    }

    private static final Field<?>[] FULL_CUSTOMER_FIELDS = ArrayUtils.join(CUSTOMER.fields(), groupConcat(CUSTOMER_LANG.LANGUAGE_LEVEL));

}
