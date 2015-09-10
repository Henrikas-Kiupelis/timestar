package com.superum.api.v2.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import eu.goodlike.misc.SpecialUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for part of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of TotalLessonDataDTO with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      count               : amount of lessons a certain teacher or a certain customer had, total
 *      duration            : the length of those lessons in minutes, combined
 *      cost                : cost of those lessons, combined
 *
 * Example of JSON to expect:
 * {
 *      "count": 15,
 *      "duration": 675,
 *      "cost": 450.7791
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class TotalLessonDataDTO {

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("duration")
    public int getDuration() {
        return duration;
    }

    @JsonProperty("cost")
    public BigDecimal getCost() {
        return cost;
    }

    // CONSTRUCTORS

    public TotalLessonDataDTO(int count, int duration, BigDecimal cost) {
        this.count = count;
        this.duration = duration;
        this.cost = cost;
    }

    // PRIVATE

    private final int count;
    private final int duration;
    private final BigDecimal cost;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TotalLessonData")
                .add("Count", count)
                .add("Duration", duration)
                .add("Cost", cost)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TotalLessonDataDTO)) return false;
        TotalLessonDataDTO that = (TotalLessonDataDTO) o;
        return Objects.equals(count, that.count) &&
                Objects.equals(duration, that.duration) &&
                SpecialUtils.equalsJavaMathBigDecimal(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, duration, cost == null ? 0 : cost.doubleValue());
    }

}
