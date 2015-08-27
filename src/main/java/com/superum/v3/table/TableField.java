package com.superum.v3.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TableField {

    @JsonProperty(CUSTOMER_ID_FIELD)
    public Integer getCustomerId() {
        return customerId;
    }

    @JsonProperty(TEACHER_ID_FIELD)
    public int getTeacherId() {
        return teacherId;
    }

    @JsonProperty(LESSON_IDS_FIELD)
    public List<Long> getLessonIds() {
        return lessonIds;
    }

    @JsonProperty(DURATION_FIELD)
    public int getDuration() {
        return duration;
    }

    @JsonProperty(COST_FIELD)
    public BigDecimal getCost() {
        return cost;
    }

    // CONSTRUCTORS

    public TableField(Integer customerId, int teacherId, List<Long> lessonIds, int duration, BigDecimal cost) {
        this.customerId = customerId;
        this.teacherId = teacherId;
        this.lessonIds = lessonIds;
        this.duration = duration;
        this.cost = cost;
    }

    // PRIVATE

    private final Integer customerId;
    private final int teacherId;
    private final List<Long> lessonIds;
    private final int duration;
    private final BigDecimal cost;

    // FIELD NAMES

    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String LESSON_IDS_FIELD = "lessonIds";
    private static final String DURATION_FIELD = "duration";
    private static final String COST_FIELD = "cost";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TableField")
                .add(CUSTOMER_ID_FIELD, customerId)
                .add(TEACHER_ID_FIELD, teacherId)
                .add(LESSON_IDS_FIELD, lessonIds)
                .add(DURATION_FIELD, duration)
                .add(COST_FIELD, cost)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof TableField && EQUALS.equals(this, (TableField) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, teacherId, lessonIds, duration, cost);
    }

    private static final Equals<TableField> EQUALS = new Equals<>(Arrays.asList(TableField::getCustomerId,
            TableField::getTeacherId, TableField::getLessonIds, TableField::getCost, TableField::getDuration));

}
