package com.superum.api.v2.attendance;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static timestar_v2.Keys.STUDENTS_IN_GROUPS_IBFK_1;
import static timestar_v2.Tables.*;

@Repository
public class StudentGroupChecker {

    /**
     * @return true if all the students belong to the group this lesson was for, false otherwise
     */
    public boolean checkLessonWithStudentIds(long lessonId, Set<Integer> studentIds, int partitionId) {
        Condition studentIdsCondition = studentIds.stream()
                .map(STUDENT.ID::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("There should be at least one student id!"));

        Condition condition = studentIdsCondition
                .and(STUDENT.PARTITION_ID.eq(partitionId))
                .and(LESSON.ID.eq(lessonId));

        Select<?> select = sql.selectOne()
                .from(STUDENT)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_1)
                .join(LESSON).on(LESSON.GROUP_ID.eq(STUDENTS_IN_GROUPS.GROUP_ID))
                .where(condition)
                .groupBy(STUDENT.ID);

        return sql.fetchCount(select) == studentIds.size();
    }

    // CONSTRUCTORS

    @Autowired
    public StudentGroupChecker(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
