package com.superum.api.student;

import com.superum.api.customer.CustomerNotFoundException;
import com.superum.api.group.GroupNotFoundException;
import com.superum.api.lesson.LessonNotFoundException;
import com.superum.db.generated.timestar.tables.records.CustomerRecord;
import com.superum.db.generated.timestar.tables.records.GroupOfStudentsRecord;
import com.superum.db.generated.timestar.tables.records.LessonRecord;
import com.superum.db.generated.timestar.tables.records.StudentRecord;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.LESSON_ATTENDANCE_IBFK_2;
import static com.superum.db.generated.timestar.Keys.STUDENTS_IN_GROUPS_IBFK_1;
import static com.superum.db.generated.timestar.Tables.*;

@Service
public class ValidStudentQueryServiceImpl implements ValidStudentQueryService {

    @Override
    public ValidStudentDTO readById(int studentId, int partitionId) {
        return defaultStudentQueries.read(studentId, partitionId, ValidStudentDTO::valueOf)
                .orElseThrow(() -> new StudentNotFoundException("Couldn't find student with id " + studentId));
    }

    @Override
    public List<ValidStudentDTO> readAll(int page, int amount, int partitionId) {
        return defaultStudentQueries.readAll(page, amount, partitionId, ValidStudentDTO::valueOf);
    }

    @Override
    public List<ValidStudentDTO> readForGroup(int groupId, int page, int amount, int partitionId) {
        if (!defaultGroupQueries.exists(groupId, partitionId))
            throw new GroupNotFoundException("Couldn't find group with id " + groupId);

        return readFromJoin(STUDENTS_IN_GROUPS, STUDENTS_IN_GROUPS_IBFK_1,
                STUDENTS_IN_GROUPS.GROUP_ID, groupId,
                page, amount, partitionId);
    }

    @Override
    public List<ValidStudentDTO> readForLesson(long lessonId, int page, int amount, int partitionId) {
        if (!defaultLessonQueries.exists(lessonId, partitionId))
            throw new LessonNotFoundException("Couldn't find lesson with id " + lessonId);

        return readFromJoin(LESSON_ATTENDANCE, LESSON_ATTENDANCE_IBFK_2,
                LESSON_ATTENDANCE.LESSON_ID, lessonId,
                page, amount, partitionId);
    }

    @Override
    public List<ValidStudentDTO> readForCustomer(int customerId, int page, int amount, int partitionId) {
        if (!defaultCustomerQueries.exists(customerId, partitionId))
            throw new CustomerNotFoundException("Couldn't find customer with id " + customerId);

        return defaultStudentQueries.readForForeignKey(page, amount, partitionId,
                STUDENT.CUSTOMER_ID, customerId, ValidStudentDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidStudentQueryServiceImpl(DSLContext sql, DefaultQueries<StudentRecord, Integer> defaultStudentQueries,
                                        DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                        DefaultQueries<LessonRecord, Long> defaultLessonQueries,
                                        DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries) {
        this.sql = sql;
        this.defaultStudentQueries = defaultStudentQueries;
        this.defaultGroupQueries = defaultGroupQueries;
        this.defaultLessonQueries = defaultLessonQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final DefaultQueries<LessonRecord, Long> defaultLessonQueries;
    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;

    private <V> List<ValidStudentDTO> readFromJoin(Table<?> table, ForeignKey<?, ?> key,
                                                   TableField<?, V> field, V value,
                                                   int page, int amount, int partitionId) {
        Condition condition = defaultStudentQueries.partitionId(partitionId)
                .and(field.eq(value));

        return sql.select(STUDENT.fields())
                .from(STUDENT)
                .join(table).onKey(key)
                .where(condition)
                .groupBy(STUDENT.ID)
                .orderBy(STUDENT.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidStudentDTO::valueOf);
    }

}
