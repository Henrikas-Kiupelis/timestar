package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.helper.jooq.DefaultQueryMaker;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.*;
import static com.superum.db.generated.timestar.Tables.*;

@Repository
@Transactional
public class FullCustomerQueriesImpl implements FullCustomerQueries {

    @Override
    public int exists(FullCustomer customer, int partitionId) {
        if (customer == null)
            throw new InvalidRequestException("Customer cannot have null value");

        if (!customer.hasAnyFieldsSet())
            throw new InvalidCustomerException("Provided customer does not have any fields set, so checking if it exists is impossible");

        if (customer.hasOnlyId()) {
            int customerId = customer.getId();
            if (!defaultQueries.exists(customerId, partitionId))
                throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

            return customerId;
        }

        return defaultQueries.exists(customer, partitionId)
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

        return defaultQueries.update(customer, partitionId);
    }

    @Override
    public int safeDelete(int customerId, int partitionId) {
        if (customerId <= 0)
            throw new InvalidRequestException("Customers can only have positive ids, not " + customerId);

        if (!defaultQueries.exists(customerId, partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

        if (foreignQueries.isUsed(customerId))
            throw new UnsafeCustomerDeleteException("Cannot delete customer with " + customerId +
                    " while it still has entries in other tables");

        return defaultQueries.delete(customerId, partitionId);
    }

    @Override
    public List<FullCustomer> readCustomersForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        if (!defaultQueriesForTeacher.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        return defaultQueries.readAllCustom(page, amount, FullCustomer::valueOf, select -> teachers(select, teacherId, partitionId));
    }

    @Override
    public List<FullCustomer> readCustomersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        return defaultQueries.readAll(partitionId, page, amount, FullCustomer::valueOf);
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        if (!defaultQueriesForTeacher.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        return defaultQueries.countCustom(select -> teachers(select, teacherId, partitionId));
    }

    @Override
    public int count(int partitionId) {
        return defaultQueries.countAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public FullCustomerQueriesImpl(DefaultQueryMaker defaultQueryMaker) {
        this.defaultQueries = defaultQueryMaker.forTable(CUSTOMER, CUSTOMER.ID, CUSTOMER.PARTITION_ID);
        this.defaultQueriesForTeacher = defaultQueryMaker.forTable(TEACHER, TEACHER.ID, TEACHER.PARTITION_ID);
        this.foreignQueries = defaultQueryMaker.forOtherTables(STUDENT.CUSTOMER_ID, GROUP_OF_STUDENTS.CUSTOMER_ID);
    }

    // PRIVATE

    private final DefaultQueryMaker.Queries<?, Integer> defaultQueries;
    private final DefaultQueryMaker.Queries<?, Integer> defaultQueriesForTeacher;
    private final DefaultQueryMaker.ForeignQueries<Integer> foreignQueries;

    private <R extends Record> SelectConditionStep<R> teachers(SelectJoinStep<R> select, int teacherId, int partitionId) {
        return select
                .join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)));
    }

}
