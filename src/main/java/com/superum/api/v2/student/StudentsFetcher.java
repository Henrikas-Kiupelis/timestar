package com.superum.api.v2.student;

import org.jooq.DSLContext;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static timestar_v2.Keys.LESSON_ATTENDANCE_IBFK_2;
import static timestar_v2.Keys.STUDENTS_IN_GROUPS_IBFK_1;
import static timestar_v2.Tables.*;

@Repository
public class StudentsFetcher {

    /**
     * @return list of students for given groupId; only specified amount is returned, with offset
     */
    public List<ValidStudentDTO> forGroup(int groupId, int page, int amount, int partitionId) {
        return readFromJoin(STUDENTS_IN_GROUPS, STUDENTS_IN_GROUPS_IBFK_1, STUDENTS_IN_GROUPS.GROUP_ID,
                groupId, page, amount, partitionId);
    }

    /**
     * @return list of students for given lessonId; only specified amount is returned, with offset
     */
    public List<ValidStudentDTO> forLesson(long lessonId, int page, int amount, int partitionId) {
        return readFromJoin(LESSON_ATTENDANCE, LESSON_ATTENDANCE_IBFK_2, LESSON_ATTENDANCE.LESSON_ID,
                lessonId, page, amount, partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public StudentsFetcher(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private <V> List<ValidStudentDTO> readFromJoin(Table<?> table, ForeignKey<?, ?> key,
                                                   TableField<?, V> field, V value,
                                                   int page, int amount, int partitionId) {
        return sql.select(STUDENT.fields())
                .from(STUDENT)
                .join(table).onKey(key)
                .where(STUDENT.PARTITION_ID.eq(partitionId)
                        .and(field.eq(value)))
                .groupBy(STUDENT.ID)
                .orderBy(STUDENT.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidStudentDTO::valueOf);
    }

}
