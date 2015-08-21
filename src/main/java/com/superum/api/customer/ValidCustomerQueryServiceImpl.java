package com.superum.api.customer;

import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.DSLContext;
import org.jooq.SelectHavingStep;
import org.jooq.SelectSelectStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.*;
import static com.superum.db.generated.timestar.Tables.*;

@Service
public class ValidCustomerQueryServiceImpl implements ValidCustomerQueryService {

    @Override
    public ValidCustomerDTO readById(int customerId, int partitionId) {
        return defaultCustomerQueries.read(customerId, partitionId, ValidCustomerDTO::valueOf)
                .orElseThrow(() -> new CustomerNotFoundException("Couldn't find customer with id " + customerId));
    }

    @Override
    public List<ValidCustomerDTO> readForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return teachersForCustomer(sql.select(CUSTOMER.fields()), teacherId, partitionId)
                .orderBy(CUSTOMER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidCustomerDTO::valueOf);
    }

    @Override
    public List<ValidCustomerDTO> readAll(int page, int amount, int partitionId) {
        return defaultCustomerQueries.readAll(page, amount, partitionId, ValidCustomerDTO::valueOf);
    }

    @Override
    public int countForTeacher(int teacherId, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return sql.fetchCount(teachersForCustomer(sql.selectOne(), teacherId, partitionId));
    }

    @Override
    public int countAll(int partitionId) {
        return defaultCustomerQueries.countAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidCustomerQueryServiceImpl(DSLContext sql, DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                         DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries) {
        this.sql = sql;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;

    private SelectHavingStep<?> teachersForCustomer(SelectSelectStep<?> select, int teacherId, int partitionId) {
        return select.from(CUSTOMER)
                .join(STUDENT).onKey(STUDENT_IBFK_1)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .groupBy(CUSTOMER.ID);
    }

}
