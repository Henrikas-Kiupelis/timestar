package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.customer.lang.CustomerLanguagesDAO;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.exception.DatabaseException;
import com.superum.helper.ConditionBuilder;
import com.superum.helper.QuickForeignKeyUsageChecker;
import com.superum.helper.QuickIdExistenceChecker;
import com.superum.helper.UpdateBuilder;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateSetMoreStep;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class FullCustomerQueriesImpl implements FullCustomerQueries {

    @Override
    public Optional<Integer> exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasAnyCustomerFieldsSet())
            throw new InvalidCustomerException("Provided customer does not have any customer fields set, so checking if it exists is impossible");

        try {
            if (customer.hasOnlyId()) {
                int id = customer.getId();
                return existsQuick(id, partitionId) ? Optional.of(id) : Optional.empty();
            }

            return sql.select(CUSTOMER.ID)
                    .from(CUSTOMER)
                    .where(partialCustomerCondition(customer, partitionId))
                    .fetch().stream()
                    .findFirst()
                    .map(record -> record.getValue(CUSTOMER.ID));
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


    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);


        return null;
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);


        return null;
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        return 0;
    }

    @Override
    public int count(int partitionId) {


        return 0;
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DSLContext sql, CustomerLanguagesDAO customerLanguagesDAO) {
        this.sql = sql;
        this.customerLanguagesDAO = customerLanguagesDAO;

        existenceChecker = new QuickIdExistenceChecker<>(sql, CUSTOMER.ID, CUSTOMER.PARTITION_ID);
        foreignKeyUsageChecker = QuickForeignKeyUsageChecker.newRequiredBuilder(Integer.class)
                .withDSLContext(sql)
                .add(CUSTOMER_LANG.CUSTOMER_ID, CUSTOMER_LANG.PARTITION_ID)
                .add(STUDENT.CUSTOMER_ID, STUDENT.PARTITION_ID)
                .build();
    }

    // PRIVATE

    private final DSLContext sql;
    private final CustomerLanguagesDAO customerLanguagesDAO;

    private final QuickIdExistenceChecker<CustomerRecord, Integer> existenceChecker;
    private final QuickForeignKeyUsageChecker<Integer> foreignKeyUsageChecker;

    /**
     * @return true if a record with customerId exists in given partition; false otherwise
     */
    private boolean existsQuick(int customerId, int partitionId) {
        return existenceChecker.check(customerId, partitionId);
    }

    /**
     * @return true if a record with customerId as foreign key exists in given partition; false otherwise
     */
    private boolean usedQuick(int customerId, int partitionId) {
        return foreignKeyUsageChecker.check(customerId, partitionId);
    }

    /**
     * @return Condition which checks for customer id and partition id
     */
    private Condition idAndPartition(int customerId, int partitionId) {
        return CUSTOMER.ID.eq(customerId).and(CUSTOMER.PARTITION_ID.eq(partitionId));
    }

    /**
     * @return Condition which checks for all set Customer fields
     */
    private Condition partialCustomerCondition(FullCustomer customer, int partitionId) {
        return new ConditionBuilder<>(customer, CUSTOMER.PARTITION_ID, partitionId)
                .fieldCheck(FullCustomer::hasId,            CUSTOMER.ID, FullCustomer::getId)
                .fieldCheck(FullCustomer::hasPaymentDay,    CUSTOMER.PAYMENT_DAY,FullCustomer::getPaymentDay)
                .fieldCheck(FullCustomer::hasStartDate,     CUSTOMER.START_DATE, FullCustomer::getStartDate)
                .fieldCheck(FullCustomer::hasPaymentValue,  CUSTOMER.PAYMENT_VALUE, FullCustomer::getPaymentValue)
                .fieldCheck(FullCustomer::hasName,          CUSTOMER.NAME, FullCustomer::getName)
                .fieldCheck(FullCustomer::hasPhone,         CUSTOMER.PHONE, FullCustomer::getPhone)
                .fieldCheck(FullCustomer::hasWebsite,       CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .fieldCheck(FullCustomer::hasPictureName,   CUSTOMER.PICTURE_NAME, FullCustomer::getPictureName)
                .fieldCheck(FullCustomer::hasComment,       CUSTOMER.COMMENT_ABOUT, FullCustomer::getComment)
                .finalCondition();
    }

    /**
     * @return UpdateSetMoreStep which updates all set Customer fields (except id)
     */
    private UpdateSetMoreStep partialCustomerUpdateStatement(FullCustomer customer) {
        return new UpdateBuilder<>(customer, sql, CUSTOMER)
                .setField(FullCustomer::hasPaymentDay,      CUSTOMER.PAYMENT_DAY, FullCustomer::getPaymentDay)
                .setField(FullCustomer::hasStartDate,       CUSTOMER.START_DATE, FullCustomer::getStartDate)
                .setField(FullCustomer::hasPaymentValue,    CUSTOMER.PAYMENT_VALUE, FullCustomer::getPaymentValue)
                .setField(FullCustomer::hasName,            CUSTOMER.NAME, FullCustomer::getName)
                .setField(FullCustomer::hasPhone,           CUSTOMER.PHONE, FullCustomer::getPhone)
                .setField(FullCustomer::hasWebsite,         CUSTOMER.WEBSITE, FullCustomer::getWebsite)
                .setField(FullCustomer::hasPictureName,     CUSTOMER.PICTURE_NAME, FullCustomer::getPictureName)
                .setField(FullCustomer::hasComment,         CUSTOMER.COMMENT_ABOUT, FullCustomer::getComment)
                .finalStep();
    }

}
