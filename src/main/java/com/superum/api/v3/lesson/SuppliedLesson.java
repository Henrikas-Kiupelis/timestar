package com.superum.api.v3.lesson;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * <pre>
 * DTO which primarily focuses on representing incoming lesson data
 *
 * Instances of this class are created directly from JSON in HTTP bodies
 *
 * The following JSON fields are parsed:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      groupId     : id of the group which is having this lesson               1 <= groupId
 *      startTime   : timestamp, representing the time this lesson started      0 <= startTime
 *      startDate   : date when the lesson started                              date String, "yyyy-MM-dd"
 *      startHour   : hour when the lesson started                              0 <= startHour <= 23
 *      startMinute : minute when the lesson started                            0 <= startMinute <= 59
 *      timezone    : time zone for the above 3 values                          time zone String;
 *      length      : duration of the lesson in minutes                         1 <= length
 *      comment     : comment, made by the teacher                              any String, max 500 chars
 *
 * You should only send startTime OR the set of timezone, startDate, startHour and startMinute; if startTime is set,
 * the other 4 will be ignored
 *
 * You can find the list of supported timezones with the method:
 *      /timestar/api/v2/misc/time/zones
 *
 * Example of JSON to send:
 * {
 *      "groupId": 1,
 *      "startDate": "2015-09-23",
 *      "startHour": 13,
 *      "startMinute": 22,
 *      "timezone" : "EET",
 *      "length" : 45,
 *      "comment": "What a lesson"
 * }
 * or
 * {
 *      "groupId": 1,
 *      "startTime": 1443003785160,
 *      "length" : 45,
 *      "comment": "What a lesson"
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class SuppliedLesson {

    @JsonProperty(GROUP_ID_FIELD)
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty(START_TIME_FIELD)
    public Long getStartTime() {
        return startTime;
    }

    @JsonProperty(TIME_ZONE_FIELD)
    public String getTimezoneString() {
        return timezone == null ? null : timezone.toString();
    }
    @JsonIgnore
    public ZoneId getTimezone() {
        return timezone;
    }

    @JsonProperty(START_DATE_FIELD)
    public String getStartDateString() {
        return startDate == null ? null : startDate.toString();
    }
    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonProperty(HOUR_FIELD)
    public Integer getStartHour() {
        return startHour;
    }

    @JsonProperty(MINUTE_FIELD)
    public Integer getStartMinute() {
        return startMinute;
    }

    @JsonProperty(LENGTH_FIELD)
    public Integer getLength() {
        return length;
    }

    @JsonProperty(COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    public SuppliedLessonWithTimezone timezoneOnly() {
        return new SuppliedLessonWithTimezone(groupId, timezone, startDate, startHour, startMinute, length, comment);
    }

    public SuppliedLessonWithTimestamp timestampOnly() {
        return new SuppliedLessonWithTimestamp(groupId, startTime, length, comment);
    }

    // CONSTRUCTORS

    @JsonCreator
    public static SuppliedLesson jsonInstance(@JsonProperty(GROUP_ID_FIELD) Integer groupId,
                                              @JsonProperty(START_TIME_FIELD) Long startTime,
                                              @JsonProperty(TIME_ZONE_FIELD) String timezoneString,
                                              @JsonProperty(START_DATE_FIELD) String startDateString,
                                              @JsonProperty(HOUR_FIELD) Integer startHour,
                                              @JsonProperty(MINUTE_FIELD) Integer startMinute,
                                              @JsonProperty(LENGTH_FIELD) Integer length,
                                              @JsonProperty(COMMENT_FIELD) String comment) {
        ZoneId timezone = timezoneString == null ? null : ZoneId.of(timezoneString);
        LocalDate startDate = startDateString == null ? null : LocalDate.parse(startDateString);
        return new SuppliedLesson(groupId, startTime, timezone, startDate, startHour, startMinute, length, comment);
    }

    public static GroupIdStep stepBuilder() {
        return new Builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public SuppliedLesson(Integer groupId, Long startTime, ZoneId timezone, LocalDate startDate, Integer startHour,
                          Integer startMinute, Integer length, String comment) {
        this.groupId = groupId;
        this.startTime = startTime;
        this.timezone = timezone;
        this.startDate = startDate;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.length = length;
        this.comment = comment;
    }

    // PRIVATE

    private final Integer groupId;
    private final Long startTime;
    private final ZoneId timezone;
    private final LocalDate startDate;
    private final Integer startHour;
    private final Integer startMinute;
    private final Integer length;
    private final String comment;

    private static final String GROUP_ID_FIELD = "groupId";
    private static final String START_TIME_FIELD = "startTime";
    private static final String TIME_ZONE_FIELD = "timezone";
    private static final String START_DATE_FIELD = "startDate";
    private static final String HOUR_FIELD = "startHour";
    private static final String MINUTE_FIELD = "startMinute";
    private static final String LENGTH_FIELD = "length";
    private static final String COMMENT_FIELD = "comment";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("startTime", startTime)
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
        if (!(o instanceof SuppliedLesson)) return false;
        SuppliedLesson that = (SuppliedLesson) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(timezone, that.timezone) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(startHour, that.startHour) &&
                Objects.equals(startMinute, that.startMinute) &&
                Objects.equals(length, that.length) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, startTime, timezone, startDate, startHour, startMinute, length, comment);
    }

    // GENERATED

    public interface GroupIdStep {
        StartTimeStep withGroupId(Integer groupId);
    }

    public interface StartTimeStep {
        TimezoneStep withStartTime(Long startTime);
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
        SuppliedLesson build();
    }


    public static class Builder implements GroupIdStep, StartTimeStep, TimezoneStep, StartDateStep, StartHourStep, StartMinuteStep, LengthStep, CommentStep, BuildStep {
        private Integer groupId;
        private Long startTime;
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
        public Builder withStartTime(Long startTime) {
            this.startTime = startTime;
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
        public SuppliedLesson build() {
            return new SuppliedLesson(
                    this.groupId,
                    this.startTime,
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
