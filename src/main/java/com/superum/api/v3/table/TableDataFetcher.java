package com.superum.api.v3.table;

import com.superum.api.v2.teacher.FullTeacherDTO;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.goodlike.misc.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.sum;
import static timestar_v2.Keys.LESSON_IBFK_1;
import static timestar_v2.Keys.LESSON_IBFK_2;
import static timestar_v2.Tables.*;

@Repository
public class TableDataFetcher {

    /**
     * @return the field data (customerId, teacherId, lesson ids for both of them, the duration of those lessons
     * and cost of those lessons); the cost must consider whether the group uses teacher's hourly or academic wage
     */
    public List<TableField> getFieldData(List<FullTeacherDTO> teachers, long start, long end, int partitionId) {
        return select(fullCondition(teachers, start, end, partitionId))
                .fetch()
                .map(this::toField);
    }

    // CONSTRUCTORS

    @Autowired
    public TableDataFetcher(DSLContext sql, PaddedSumField paddedSumField) {
        this.sql = sql;
        this.paddedSumField = paddedSumField;
    }

    // PRIVATE

    private final DSLContext sql;
    private final PaddedSumField paddedSumField;

    /**
     * @return the SQL records representing a TableField; the condition is usually some form of fullCondition()
     */
    private SelectHavingStep<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> select(Condition condition) {
        return sql.select(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID,
                groupConcat(LESSON.ID).as(LESSON_IDS_FIELD),
                sum(LESSON.DURATION_IN_MINUTES).as(DURATION_FIELD),
                paddedSumField.field())
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID);
    }

    /**
     * @return an SQL condition which gives: lessons in the correct partition; lessons that started between start and
     * end variables; only lessons that are assigned to the given list of teachers
     * @throws AssertionError if teacher's list is empty; this method should NEVER be called if there are no
     * teachers, because in that scenario the program can exit early.
     */
    private Condition fullCondition(List<FullTeacherDTO> teachers, long start, long end, int partitionId) {
        Condition partitionCondition = LESSON.PARTITION_ID.eq(partitionId);
        Condition timeCondition = LESSON.TIME_OF_START.between(start, end);
        Condition teacherCondition = teachers.stream()
                .map(FullTeacherDTO::getId)
                .map(GROUP_OF_STUDENTS.TEACHER_ID::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("If there were no teachers, this method would not " +
                        "be called, because the caller method would exit early."));

        return partitionCondition.and(timeCondition).and(teacherCondition);
    }

    /**
     * @return TableField from a Record returned by select(Condition)
     */
    private TableField toField(Record record) {
        if (record == null)
            return null;

        Integer customerId = record.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID);
        int teacherId = record.getValue(GROUP_OF_STUDENTS.TEACHER_ID);

        String ids = record.getValue(LESSON_IDS_FIELD, String.class);
        List<Long> lessonIds = Stream.of(ids.split(MYSQL_GROUP_CONCAT_SEPARATOR))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        BigDecimal durationSum = record.getValue(DURATION_FIELD, BigDecimal.class);
        int duration = durationSum.intValueExact();

        BigDecimal cost = paddedSumField.valueForRecord(record);

        return new TableField(customerId, teacherId, lessonIds, duration, cost);
    }

    private static final String LESSON_IDS_FIELD = "lessonIds";
    private static final String DURATION_FIELD = "duration";

}
