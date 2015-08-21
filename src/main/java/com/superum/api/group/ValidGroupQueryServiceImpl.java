package com.superum.api.group;

import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.GroupOfStudentsRecord;
import com.superum.db.generated.timestar.tables.records.StudentRecord;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.STUDENTS_IN_GROUPS_IBFK_2;
import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;
import static com.superum.db.generated.timestar.Tables.STUDENTS_IN_GROUPS;

@Service
public class ValidGroupQueryServiceImpl implements ValidGroupQueryService {

    @Override
    public ValidGroupDTO readById(int groupId, int partitionId) {
        return defaultGroupQueries.read(groupId, partitionId, ValidGroupDTO::valueOf)
                .orElseThrow(() -> new GroupNotFoundException("Couldn't find group with id " + groupId));
    }

    @Override
    public List<ValidGroupDTO> readAll(int page, int amount, int partitionId) {
        return defaultGroupQueries.readAll(page, amount, partitionId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForTeacher(int teacherId, int page, int amount, int partitionId) {
        if (!defaultTeacherQueries.exists(teacherId, partitionId))
            throw new TeacherNotFoundException("No teacher with given id exists: " + teacherId);

        return defaultGroupQueries.readForForeignKey(page, amount, partitionId,
                GROUP_OF_STUDENTS.TEACHER_ID, teacherId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForCustomer(int customerId, int page, int amount, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new TeacherNotFoundException("No customer with given id exists: " + customerId);

        return defaultGroupQueries.readForForeignKey(page, amount, partitionId,
                GROUP_OF_STUDENTS.CUSTOMER_ID, customerId, ValidGroupDTO::valueOf);
    }

    @Override
    public List<ValidGroupDTO> readForStudent(int studentId, int page, int amount, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new TeacherNotFoundException("No student with given id exists: " + studentId);

        Condition condition = defaultGroupQueries.partitionId(partitionId)
                .and(STUDENTS_IN_GROUPS.STUDENT_ID.eq(studentId));

        return sql.select(GROUP_OF_STUDENTS.fields())
                .from(GROUP_OF_STUDENTS)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(condition)
                .groupBy(GROUP_OF_STUDENTS.ID)
                .orderBy(GROUP_OF_STUDENTS.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidGroupDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupQueryServiceImpl(DSLContext sql, DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                      DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries,
                                      DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                      DefaultQueries<StudentRecord, Integer> defaultStudentQueries) {
        this.sql = sql;
        this.defaultGroupQueries = defaultGroupQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultStudentQueries = defaultStudentQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;

}
