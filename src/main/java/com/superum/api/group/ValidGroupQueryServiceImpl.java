package com.superum.api.group;

import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.helper.jooq.DefaultQueryMaker;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.superum.db.generated.timestar.Tables.*;

@Service
public class ValidGroupQueryServiceImpl implements ValidGroupQueryService {

    @Override
    public Optional<ValidGroupDTO> readById(int groupId, int partitionId) {
        return sql.selectFrom(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.ID.eq(groupId).and(partitionId(partitionId)))
                .fetch().stream().findAny()
                .map(ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readAll(int page, int amount, int partitionId) {
        return readForCondition(page, amount, partitionId(partitionId));
    }

    @Override
    public List<ValidGroupDTO> readForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return readForCondition(page, amount,
                GROUP_OF_STUDENTS.TEACHER_ID.eq(teacherId).and(partitionId(partitionId)));
    }

    @Override
    public List<ValidGroupDTO> readForCustomer(int customerId, int page, int amount, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new TeacherNotFoundException("No customer with given id exists: " + customerId);

        return readForCondition(page, amount,
                GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customerId).and(partitionId(partitionId)));
    }


    // CONSTRUCTORS

    @Autowired
    public ValidGroupQueryServiceImpl(DSLContext sql) {
        this.sql = sql;
        defaultTeacherQueries = DefaultQueryMaker.forTable(sql, TEACHER, TEACHER.ID, TEACHER.PARTITION_ID);
        defaultCustomerQueries = DefaultQueryMaker.forTable(sql, CUSTOMER, CUSTOMER.ID, CUSTOMER.PARTITION_ID);
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueryMaker.Queries<?, Integer> defaultTeacherQueries;
    private final DefaultQueryMaker.Queries<?, Integer> defaultCustomerQueries;

    private List<ValidGroupDTO> readForCondition(int page, int amount, Condition condition) {
        return sql.selectFrom(GROUP_OF_STUDENTS)
                .where(condition)
                .orderBy(GROUP_OF_STUDENTS.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidGroupDTO::valueOf);
    }

    private static Condition partitionId(int partitionId) {
        return GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId);
    }

}
