package com.superum.api.v3.customer.sql;

import com.superum.api.v3.customer.CustomerSerializer;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import com.superum.helper.PartitionAccount;
import eu.goodlike.libraries.jooq.Queries;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import timestar_v2.tables.records.CustomerRecord;

import java.util.List;

import static timestar_v2.Keys.*;
import static timestar_v2.Tables.*;

@Repository
public class CustomersForTeacher {

    /**
     * @return list of customers for given teacherId; only specified amount is returned, with offset
     */
    public List<FetchedCustomer> fetch(int teacherId, int page, int amount) {
        return customerQueries.readJoin(page, amount, teacherId(teacherId),
                customerSerializer::toReturnable, this::customerJoin);
    }

    /**
     * @return count of customers for given teacherId
     */
    public int count(int teacherId) {
        return sql.fetchCount(customersForTeacher(teacherId));
    }

    // CONSTRUCTORS

    @Autowired
    public CustomersForTeacher(DSLContext sql, Queries<CustomerRecord, Integer> customerQueries,
                               CustomerSerializer customerSerializer) {
        this.sql = sql;
        this.customerQueries = customerQueries;
        this.customerSerializer = customerSerializer;
    }

    // PRIVATE

    private final DSLContext sql;
    private final Queries<CustomerRecord, Integer> customerQueries;
    private final CustomerSerializer customerSerializer;

    private Condition teacherId(int teacherId) {
        return GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                .and(CUSTOMER.PARTITION_ID.eq(new PartitionAccount().partitionId()));
    }

    private SelectWhereStep<Record> customerJoin(SelectJoinStep<Record> customers) {
        return customers.join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2);
    }

    private SelectHavingStep<?> customersForTeacher(int teacherId) {
        return customerJoin(sql.select(CUSTOMER.fields()).from(CUSTOMER))
                .where(teacherId(teacherId))
                .groupBy(CUSTOMER.ID);
    }

}
