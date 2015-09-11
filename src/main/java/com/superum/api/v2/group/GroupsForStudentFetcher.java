package com.superum.api.v2.group;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static timestar_v2.Keys.STUDENTS_IN_GROUPS_IBFK_2;
import static timestar_v2.Tables.GROUP_OF_STUDENTS;
import static timestar_v2.Tables.STUDENTS_IN_GROUPS;

@Repository
public class GroupsForStudentFetcher {

    /**
     * @return list of groups for given studentId; only specified amount is returned, with offset
     */
    public List<ValidGroupDTO> fetch(int studentId, int page, int amount, int partitionId) {
        return sql.select(GROUP_OF_STUDENTS.fields())
                .from(GROUP_OF_STUDENTS)
                .join(STUDENTS_IN_GROUPS).onKey(STUDENTS_IN_GROUPS_IBFK_2)
                .where(STUDENTS_IN_GROUPS.PARTITION_ID.eq(partitionId)
                        .and(STUDENTS_IN_GROUPS.STUDENT_ID.eq(studentId)))
                .groupBy(GROUP_OF_STUDENTS.ID)
                .orderBy(GROUP_OF_STUDENTS.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(ValidGroupDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public GroupsForStudentFetcher(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

}
