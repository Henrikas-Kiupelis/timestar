package com.superum.api.v3.teacher.impl;

import com.google.common.collect.ObjectArrays;
import org.jooq.Field;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static eu.goodlike.misc.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static org.jooq.impl.DSL.groupConcat;
import static timestar_v2.Tables.TEACHER_LANGUAGE;

@Component
public class TeacherLanguagesField {

    public Field<String> getField() {
        return field;
    }

    public Field<?>[] appendField(Field<?>... fields) {
        return ObjectArrays.concat(fields, field);
    }

    public List<String> valueFor(Record record) {
        return record == null ? null : Arrays.asList(record.getValue(field).split(MYSQL_GROUP_CONCAT_SEPARATOR));
    }

    // CONSTRUCTORS

    public TeacherLanguagesField() {
        this.field = groupConcat(TEACHER_LANGUAGE.CODE).as(TEACHER_LANGUAGE_FIELD_NAME);
    }

    // PRIVATE

    private final Field<String> field;

    private static final String TEACHER_LANGUAGE_FIELD_NAME = "languages";

}
