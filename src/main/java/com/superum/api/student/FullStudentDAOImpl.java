package com.superum.api.student;

import com.superum.db.group.student.Student;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.STUDENT;

@Repository
@Transactional
public class FullStudentDAOImpl implements FullStudentDAO {

    @Override
    public List<Student> read(List<Integer> studentIds, int partitionId) {
        if (studentIds.isEmpty())
            return Collections.emptyList();

        return sql.selectFrom(STUDENT)
                .where(STUDENT.PARTITION_ID.eq(partitionId)
                        .and(studentIds.stream()
                                .map(STUDENT.ID::eq)
                                .reduce(Condition::and)
                                .orElseThrow(() -> new AssertionError("No student ids provided!"))))
                .fetch()
                .map(Student::valueOf);
    }

    @Override
    public List<Student> readAll(int partitionId) {
        return sql.selectFrom(STUDENT)
                .where(STUDENT.PARTITION_ID.eq(partitionId))
                .fetch()
                .map(Student::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public FullStudentDAOImpl(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
