package com.superum.api.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for part of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of TeacherLessonDataDTO with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      count               : amount of lessons a certain teacher had for a certain customer
 *      duration            : the length of those lessons in minutes, combined
 *      cost                : cost of those lessons, combined
 *
 * Example of JSON to expect:
 * {
 *      "count": 5,
 *      "duration": 225,
 *      "cost": 150.2597
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class TeacherLessonDataDTO {

    @JsonProperty("lessonIds")
    public List<Long> getLessonIds() {
        return lessonIds;
    }

    @JsonProperty("duration")
    public int getDuration() {
        return duration;
    }

    @JsonProperty("cost")
    public BigDecimal getCost() {
        return cost;
    }

    @JsonIgnore
    public Integer getCustomerId() {
        return customerId;
    }

    @JsonIgnore
    public int getTeacherId() {
        return teacherId;
    }

    // CONSTRUCTORS

    public TeacherLessonDataDTO(Integer customerId, int teacherId, List<Long> lessonIds, int duration, BigDecimal cost) {
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

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TeacherLessonData")
                .add("Lesson ids", lessonIds)
                .add("Duration", duration)
                .add("Cost", cost)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof TeacherLessonDataDTO && EQUALS.equals(this, (TeacherLessonDataDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonIds, duration, cost);
    }

    private static final Equals<TeacherLessonDataDTO> EQUALS = new Equals<>(Arrays.asList(
            TeacherLessonDataDTO::getLessonIds, TeacherLessonDataDTO::getCost,
            TeacherLessonDataDTO::getDuration));


}
