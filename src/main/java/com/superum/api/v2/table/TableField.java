package com.superum.api.v2.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import eu.goodlike.misc.SpecialUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for a field of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of TableField with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      customerId          : id of a customer
 *      teacherId           : id of a teacher
 *      count               : amount of lessons this teacher had for this customer
 *      duration            : the total length of those lessons in minutes
 *      cost                : the total cost of those lessons (hourly/academic wage is considered in calculation)
 *
 * If customerId is null, the lessons are for groups without any customer (student groups)
 *
 * Example of JSON to expect:
 * {
 *      "customerId": 1,
 *      "teacherId": 1,
 *      "count": 5,
 *      "duration": 225,
 *      "cost": 150.2597
 * }
 * </pre>
 */
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

    public TableField(@JsonProperty(CUSTOMER_ID_FIELD) Integer customerId,
                      @JsonProperty(TEACHER_ID_FIELD) int teacherId,
                      @JsonProperty(LESSON_IDS_FIELD) List<Long> lessonIds,
                      @JsonProperty(DURATION_FIELD) int duration,
                      @JsonProperty(COST_FIELD) BigDecimal cost) {
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
        if (this == o) return true;
        if (!(o instanceof TableField)) return false;
        TableField that = (TableField) o;
        return Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(lessonIds, that.lessonIds) &&
                SpecialUtils.equalsJavaMathBigDecimal(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, teacherId, lessonIds, duration, cost == null ? 0 : cost.doubleValue());
    }

}
