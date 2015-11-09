package com.superum.api.v3.teacher.impl;

import com.superum.api.v3.teacher.TeacherErrors;
import com.superum.api.v3.teacher.TeacherQueries;
import com.superum.api.v3.teacher.TeacherSerializer;
import com.superum.api.v3.teacher.dto.FetchedTeacher;
import com.superum.helper.PartitionAccount;
import eu.goodlike.libraries.jooq.Queries;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.TeacherRecord;

import java.util.List;

import static timestar_v2.Keys.TEACHER_LANGUAGE_IBFK_1;
import static timestar_v2.Tables.TEACHER;
import static timestar_v2.Tables.TEACHER_LANGUAGE;

@Service
public class TeacherQueriesImpl implements TeacherQueries {

    @Override
    public FetchedTeacher readById(int id) {
        return fullTeachers()
                .where(TEACHER.ID.eq(id).and(correctPartition()))
                .groupBy(TEACHER.ID)
                .fetch().stream().findAny()
                .map(teacherSerializer::toReturnable)
                .orElseThrow(() -> TeacherErrors.teacherIdError(id));
    }

    @Override
    public List<FetchedTeacher> readAll(int page, int amount) {
        return fullTeachers()
                .where(correctPartition())
                .groupBy(TEACHER.ID)
                .orderBy(TEACHER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(teacherSerializer::toReturnable);
    }

    @Override
    public int countAll() {
        return teacherQueries.countAll();
    }

    // CONSTRUCTORS

    @Autowired
    public TeacherQueriesImpl(DSLContext sql, TeacherLanguagesField teacherLanguagesField,
                              Queries<TeacherRecord, Integer> teacherQueries, TeacherSerializer teacherSerializer) {
        this.sql = sql;
        this.teacherLanguagesField = teacherLanguagesField;
        this.teacherQueries = teacherQueries;
        this.teacherSerializer = teacherSerializer;
    }

    // PRIVATE

    private final DSLContext sql;
    private final TeacherLanguagesField teacherLanguagesField;
    private final Queries<TeacherRecord, Integer> teacherQueries;
    private final TeacherSerializer teacherSerializer;

    private SelectJoinStep<Record> fullTeachers() {
        return sql.select(joinedFields())
                .from(TEACHER)
                .join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1);
    }

    private Field<?>[] joinedFields() {
        return teacherLanguagesField.appendField(TEACHER.fields());
    }

    private Condition correctPartition() {
        return TEACHER.PARTITION_ID.eq(new PartitionAccount().partitionId());
    }

}
