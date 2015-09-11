package com.superum.api.v2.teacher;

import com.google.common.collect.ObjectArrays;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.groupConcat;
import static timestar_v2.Keys.TEACHER_LANGUAGE_IBFK_1;
import static timestar_v2.Tables.TEACHER;
import static timestar_v2.Tables.TEACHER_LANGUAGE;

@Repository
public class TeacherFetcher {

    /**
     * @return teacher with given id, if such exists
     */
    public Optional<FullTeacherDTO> forId(int teacherId, int partitionId) {
        return fullTeachers()
                .where(TEACHER.ID.eq(teacherId)
                        .and(TEACHER.PARTITION_ID.eq(partitionId)))
                .groupBy(TEACHER.ID)
                .fetch().stream().findAny()
                .map(FullTeacherDTO::valueOf);
    }

    /**
     * @return list of all teachers; only specified amount is returned, with offset
     */
    public List<FullTeacherDTO> all(int page, int amount, int partitionId) {
        return fullTeachers()
                .where(TEACHER.PARTITION_ID.eq(partitionId))
                .groupBy(TEACHER.ID)
                .orderBy(TEACHER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(FullTeacherDTO::valueOf);
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherFetcher(DSLContext sql) {
        this.sql = sql;
    }

    // PRIVATE

    private final DSLContext sql;

    private SelectJoinStep<Record> fullTeachers() {
        return sql.select(FULL_TEACHER_FIELDS)
                .from(TEACHER)
                .join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1);
    }

    private static final Field<?>[] FULL_TEACHER_FIELDS = ObjectArrays.concat(TEACHER.fields(),
            groupConcat(TEACHER_LANGUAGE.CODE).as(FullTeacherDTO.getLanguagesFieldName()));

}
