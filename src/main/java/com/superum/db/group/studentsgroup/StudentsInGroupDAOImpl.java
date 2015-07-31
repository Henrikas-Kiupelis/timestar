package com.superum.db.group.studentsgroup;

import com.superum.db.generated.timestar.tables.records.StudentsInGroupsRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.superum.db.generated.timestar.Tables.STUDENTS_IN_GROUPS;

@Repository
@Transactional
public class StudentsInGroupDAOImpl implements StudentsInGroupDAO {

    @Override
    public int create(Integer groupId, List<Integer> studentIds, int partitionId) {
        InsertValuesStep3<StudentsInGroupsRecord, Integer, Integer, Integer> step =
                sql.insertInto(STUDENTS_IN_GROUPS, STUDENTS_IN_GROUPS.PARTITION_ID, STUDENTS_IN_GROUPS.GROUP_ID, STUDENTS_IN_GROUPS.STUDENT_ID);

        for (Integer studentId : studentIds)
            step = step.values(partitionId, groupId, studentId);

        return step.execute();
    }

    @Override
    public List<Integer> read(Integer groupId, int partitionId) {
        return sql.select(STUDENTS_IN_GROUPS.STUDENT_ID)
                .from(STUDENTS_IN_GROUPS)
                .where(STUDENTS_IN_GROUPS.GROUP_ID.eq(groupId)
                    .and(STUDENTS_IN_GROUPS.PARTITION_ID.eq(partitionId)))
                .fetch()
                .map(record -> record.getValue(STUDENTS_IN_GROUPS.STUDENT_ID));
    }

    @Override
    public int delete(Integer groupId, List<Integer> studentIds, int partitionId) {
        Condition condition = STUDENTS_IN_GROUPS.GROUP_ID.eq(groupId)
                .and(STUDENTS_IN_GROUPS.PARTITION_ID.eq(partitionId));

        for (Integer studentId : studentIds)
            condition = condition.and(STUDENTS_IN_GROUPS.STUDENT_ID.eq(studentId));

        return sql.delete(STUDENTS_IN_GROUPS)
                .where(condition)
                .execute();
    }

    @Override
    public int deleteForStudent(int studentId, int partitionId) {
        return sql.delete(STUDENTS_IN_GROUPS)
                .where(STUDENTS_IN_GROUPS.STUDENT_ID.eq(studentId)
                        .and(STUDENTS_IN_GROUPS.PARTITION_ID.eq(partitionId)))
                .execute();
    }

    // CONSTRUCTORS

    @Autowired
    public StudentsInGroupDAOImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;


}
