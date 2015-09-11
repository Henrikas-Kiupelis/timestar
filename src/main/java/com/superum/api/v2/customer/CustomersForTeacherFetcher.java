package com.superum.api.v2.customer;

import org.jooq.DSLContext;
import org.jooq.SelectHavingStep;
import org.jooq.SelectSelectStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static timestar_v2.Keys.*;
import static timestar_v2.Tables.*;

@Repository
public class CustomersForTeacherFetcher {

    /**
     * @return list of customers for given teacherId; only specified amount is returned, with offset
     */
    public List<ValidCustomerDTO> fetch(int teacherId, int page, int amount, int partitionId) {
        return customersForTeacher(sql.select(CUSTOMER.fields()), teacherId, partitionId)
                .orderBy(CUSTOMER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidCustomerDTO::valueOf);
    }

    /**
     * @return count of customers for given teacherId
     */
    public int count(int teacherId, int partitionId) {
        return sql.fetchCount(customersForTeacher(sql.selectOne(), teacherId, partitionId));
    }

    // CONSTRUCTORS

    @Autowired
    public CustomersForTeacherFetcher(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private SelectHavingStep<?> customersForTeacher(SelectSelectStep<?> select, int teacherId, int partitionId) {
        return select.from(CUSTOMER)
                .join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .groupBy(CUSTOMER.ID);
    }

}
