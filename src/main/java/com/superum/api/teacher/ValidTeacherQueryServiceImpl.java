package com.superum.api.teacher;

import com.google.common.collect.ObjectArrays;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.helper.jooq.DefaultQueries;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.superum.db.generated.timestar.Keys.TEACHER_LANGUAGE_IBFK_1;
import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.db.generated.timestar.Tables.TEACHER_LANGUAGE;
import static org.jooq.impl.DSL.groupConcat;

@Service
public class ValidTeacherQueryServiceImpl implements ValidTeacherQueryService {

    @Override
    public FullTeacherDTO readById(int teacherId, int partitionId) {
        return fullTeachers()
                .where(defaultTeacherQueries.idAndPartition(teacherId, partitionId))
                .groupBy(TEACHER.ID)
                .fetch().stream().findAny()
                .map(FullTeacherDTO::valueOf)
                .orElseThrow(() -> new TeacherNotFoundException("Couldn't find teacher with id " + teacherId));
    }

    @Override
    public List<FullTeacherDTO> readAll(int page, int amount, int partitionId) {
        return fullTeachers()
                .where(defaultTeacherQueries.partitionId(partitionId))
                .groupBy(TEACHER.ID)
                .orderBy(TEACHER.ID)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(FullTeacherDTO::valueOf);
    }

    @Override
    public int countAll(int partitionId) {
        return defaultTeacherQueries.countAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidTeacherQueryServiceImpl(DSLContext sql, DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries) {
        this.sql = sql;
        this.defaultTeacherQueries = defaultTeacherQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;

    private static final Field<?>[] FULL_TEACHER_FIELDS = ObjectArrays.concat(TEACHER.fields(),
            groupConcat(TEACHER_LANGUAGE.CODE).as(FullTeacherDTO.getLanguagesFieldName()));

    private SelectJoinStep<Record> fullTeachers() {
        return sql.select(FULL_TEACHER_FIELDS)
                .from(TEACHER)
                .join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1);
    }

}
