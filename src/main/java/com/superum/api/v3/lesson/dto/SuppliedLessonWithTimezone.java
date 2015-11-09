package com.superum.api.v3.lesson.dto;

import com.google.common.base.MoreObjects;
import com.superum.api.v2.lesson.InvalidLessonException;
import eu.goodlike.neat.Null;
import eu.goodlike.v2.validate.Validate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * SuppliedLesson version which uses timezone to represent start time of lesson; should be transformed into
 * SuppliedLessonWithTimestamp when (and if) possible
 */
public class SuppliedLessonWithTimezone {

    public Integer getGroupId() {
        return groupId;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public Integer getLength() {
        return length;
    }

    public String getComment() {
        return comment;
    }

    public void validateForCreation() {
        validateForConversion();
    }

    public void validateForUpdate() {
        if (timezone != null || startDate != null || startHour != null || startMinute != null)
            validateForConversion();
    }

    // CONSTRUCTORS

    public static GroupIdStep stepBuilder() {
        return new Builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public SuppliedLessonWithTimezone(Integer groupId, ZoneId timezone, LocalDate startDate, Integer startHour,
                                      Integer startMinute, Integer length, String comment) {
        this.groupId = groupId;
        this.timezone = timezone;
        this.startDate = startDate;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.length = length;
        this.comment = comment;
    }

    // PRIVATE

    private final Integer groupId;
    private final ZoneId timezone;
    private final LocalDate startDate;
    private final Integer startHour;
    private final Integer startMinute;
    private final Integer length;
    private final String comment;

    private void validateForConversion() {
        Null.check(timezone, startDate).ifAny(this::cannotConvertError);
        Validate.integer().not().isNull().isHourOfDay().ifInvalid(startHour).thenThrow(this::startHourError);
        Validate.integer().not().isNull().isMinuteOfHour().ifInvalid(startMinute).thenThrow(this::startMinuteError);
    }

    private InvalidLessonException cannotConvertError() {
        return new InvalidLessonException("Cannot convert time because timezone or startDate is null: "
                + timezone + ", " + startDate);
    }

    private InvalidLessonException startHourError() {
        return new InvalidLessonException("Lesson start hour must be between 0 and 23, not: " + startHour);
    }

    private InvalidLessonException startMinuteError() {
        return new InvalidLessonException("Lesson start minute must be between 0 and 59, not: " + startMinute);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("timezone", timezone)
                .add("startDate", startDate)
                .add("startHour", startHour)
                .add("startMinute", startMinute)
                .add("length", length)
                .add("comment", comment)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuppliedLessonWithTimezone)) return false;
        SuppliedLessonWithTimezone that = (SuppliedLessonWithTimezone) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(timezone, that.timezone) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(startHour, that.startHour) &&
                Objects.equals(startMinute, that.startMinute) &&
                Objects.equals(length, that.length) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, timezone, startDate, startHour, startMinute, length, comment);
    }

    // GENERATED

    public interface GroupIdStep {
        TimezoneStep withGroupId(Integer groupId);
    }

    public interface TimezoneStep {
        StartDateStep withTimezone(ZoneId timezone);
    }

    public interface StartDateStep {
        StartHourStep withStartDate(LocalDate startDate);
    }

    public interface StartHourStep {
        StartMinuteStep withStartHour(Integer startHour);
    }

    public interface StartMinuteStep {
        LengthStep withStartMinute(Integer startMinute);
    }

    public interface LengthStep {
        CommentStep withLength(Integer length);
    }

    public interface CommentStep {
        BuildStep withComment(String comment);
    }

    public interface BuildStep {
        SuppliedLessonWithTimezone build();
    }

    public static class Builder implements GroupIdStep, TimezoneStep, StartDateStep, StartHourStep, StartMinuteStep, LengthStep, CommentStep, BuildStep {
        private Integer groupId;
        private ZoneId timezone;
        private LocalDate startDate;
        private Integer startHour;
        private Integer startMinute;
        private Integer length;
        private String comment;

        private Builder() {}

        @Override
        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        @Override
        public Builder withTimezone(ZoneId timezone) {
            this.timezone = timezone;
            return this;
        }

        @Override
        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder withStartHour(Integer startHour) {
            this.startHour = startHour;
            return this;
        }

        @Override
        public Builder withStartMinute(Integer startMinute) {
            this.startMinute = startMinute;
            return this;
        }

        @Override
        public Builder withLength(Integer length) {
            this.length = length;
            return this;
        }

        @Override
        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public SuppliedLessonWithTimezone build() {
            return new SuppliedLessonWithTimezone(
                    this.groupId,
                    this.timezone,
                    this.startDate,
                    this.startHour,
                    this.startMinute,
                    this.length,
                    this.comment
            );
        }
    }
}
