package com.superum.api.lesson;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import com.superum.api.core.DTOWithTimestamps;
import com.superum.api.exception.InvalidRequestException;
import com.superum.helper.Equals;
import com.superum.helper.NullChecker;
import com.superum.helper.time.JodaTimeZoneHandler;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import java.util.Arrays;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.LESSON;

/**
 * <pre>
 * Data Transport Object for lessons
 *
 * This object is used to de-serialize and serialize JSON that is coming in and out of the back end;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When creating an instance of ValidLessonDTO with JSON, these fields are required:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      groupId     : id of the group which is having this lesson               1 <= groupId
 *      length      : duration of the lesson in minutes                         1 <= length
 * You can choose from one set of these fields:
 *      startTime   : timestamp, representing the time this lesson started      0 <= startTime
 * or:
 *      startDate   : date when the lesson started                              date String, "yyyy-MM-dd"
 *      startHour   : hour when the lesson started                              0 <= startHour <= 23
 *      startMinute : minute when the lesson started                            0 <= startMinute <= 59
 *      timeZone    : time zone for the above 3 values                          time zone String;
 *                    check /misc/time/zones (MiscController) for valid values
 * These fields are optional:
 *      comment     : comment, made by the app client                           any String, max 500 chars
 * These fields should only be specified if they are known:
 *      id          : number representation of this lesson in the system        1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will assume default value (null for objects, 0/false for primitives),
 * all of which are assumed to be allowed unless stated otherwise (check FIELD_CONSTRAINTS)
 *
 * Example of JSON to send:
 * {
 *      "id": 1,
 *      "groupId": 1
 *      "startDate": "2015-08-21",
 *      "startHour": 15,
 *      "startMinute": 46,
 *      "timeZone" : "EET",
 *      "duration" : 45,
 *      "comment": "What a lesson"
 * }
 * or
 * {
 *      "id": 1,
 *      "groupId": 1
 *      "startTime": 1440161160000,
 *      "duration" : 45,
 *      "comment": "What a lesson"
 * }
 *
 * When returning an instance of ValidLessonDTO with JSON, these additional fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      teacherId   : id of the teacher which is responsible for the group that is having this lesson
 *      endTime     : timestamp, representing the time this lesson ended
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "groupId": 1
 *      "teacherId": 1
 *      "startTime": 1440161160000,
 *      "endTime": 1440163860000,
 *      "duration" : 45,
 *      "comment": "What a lesson"
 *      "createdAt":1440104400000,
 *      "updatedAt":1440161945223
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidLessonDTO extends DTOWithTimestamps {

    @JsonProperty(ID_FIELD)
    public Long getId() {
        return id;
    }
    @JsonIgnore
    public ValidLessonDTO withId(Long id) {
        return new ValidLessonDTO(id, groupId, teacherId, startTime, endTime, length, comment, getCreatedAt(), getUpdatedAt());
    }
    @JsonIgnore
    public ValidLessonDTO without() {
        return new ValidLessonDTO(null, groupId, teacherId, startTime, endTime, length, comment, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(GROUP_ID_FIELD)
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty(TEACHER_ID_FIELD)
    public Integer getTeacherId() {
        return teacherId;
    }

    @JsonProperty(START_TIME_FIELD)
    public Long getStartTime() {
        return startTime;
    }

    @JsonProperty(END_TIME_FIELD)
    public Long getEndTime() {
        return endTime;
    }

    @JsonProperty(LENGTH_FIELD)
    public Integer getLength() {
        return length;
    }

    @JsonProperty(COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidLessonDTO jsonInstance(@JsonProperty(ID_FIELD) Long id,
                                              @JsonProperty(GROUP_ID_FIELD) Integer groupId,
                                              @JsonProperty(START_TIME_FIELD) Long startTime,
                                              @JsonProperty("timeZone") String timeZone,
                                              @JsonProperty("startDate") String startDate,
                                              @JsonProperty("startHour") Integer startHour,
                                              @JsonProperty("startMinute") Integer startMinute,
                                              @JsonProperty(LENGTH_FIELD) Integer length,
                                              @JsonProperty(COMMENT_FIELD) String comment) {
        if (startTime == null) {
            NullChecker.check(timeZone, startDate, startHour, startMinute)
                    .notNull(() -> new InvalidRequestException("Must provide either startTime or timeZone, " +
                            "startDate, startHour and startMinute"));

            return fromTimeZone(id, groupId, null, DateTimeZone.forID(timeZone), LocalDate.parse(startDate),
                    startHour, startMinute, length, comment, null, null);
        }
        return fromStartTime(id, groupId, null, startTime, length, comment, null, null);
    }

    public static ValidLessonDTO fromStartTime(Long id, Integer groupId, Integer teacherId, Long startTime, Integer length,
                                       String comment, Instant createdAt, Instant updatedAt) {
        Long endTime = new Instant(startTime).plus(Duration.standardMinutes(length)).getMillis();
        return new ValidLessonDTO(id, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
    }

    public static ValidLessonDTO fromTimeZone(Long id, Integer groupId, Integer teacherId, DateTimeZone timeZone,
                                      LocalDate startDate, Integer startHour, Integer startMinute, Integer length,
                                      String comment, Instant createdAt, Instant updatedAt) {
        Long startTime = JodaTimeZoneHandler.forTimeZone(timeZone).from(startDate, startHour, startMinute).toEpochMillis();
        return fromStartTime(id, groupId, teacherId, startTime, length, comment, createdAt, updatedAt);
    }

    public ValidLessonDTO(Long id, Integer groupId, Integer teacherId, Long startTime, Long endTime, Integer length, String comment,
                  Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.length = length;
        this.comment = comment;
    }

    public static ValidLessonDTO valueOf(Record lessonRecord) {
        if (lessonRecord == null)
            return null;

        return stepBuilder()
                .groupIdWithTeacher(lessonRecord.getValue(LESSON.GROUP_ID),
                        lessonRecord.getValue(LESSON.TEACHER_ID))
                .startTime(lessonRecord.getValue(LESSON.TIME_OF_START))
                .length(lessonRecord.getValue(LESSON.DURATION_IN_MINUTES))
                .id(lessonRecord.getValue(LESSON.ID))
                .createdAt(lessonRecord.getValue(LESSON.CREATED_AT))
                .comment(lessonRecord.getValue(LESSON.COMMENT))
                .updatedAt(lessonRecord.getValue(LESSON.UPDATED_AT))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static GroupIdStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

    private final Long id;
    private final Integer groupId;
    private final Integer teacherId;
    private final Long startTime;
    private final Long endTime;
    private final Integer length;
    private final String comment;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String GROUP_ID_FIELD = "groupId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String START_TIME_FIELD = "startTime";
    private static final String END_TIME_FIELD = "endTime";
    private static final String LENGTH_FIELD = "length";
    private static final String COMMENT_FIELD = "comment";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Lesson")
                .add(ID_FIELD, id)
                .add(GROUP_ID_FIELD, groupId)
                .add(TEACHER_ID_FIELD, teacherId)
                .add(START_TIME_FIELD, startTime)
                .add(END_TIME_FIELD, endTime)
                .add(LENGTH_FIELD, length)
                .add(COMMENT_FIELD, comment)
                .add(CREATED_AT_FIELD, getCreatedAt())
                .add(UPDATED_AT_FIELD, getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidLessonDTO && EQUALS.equals(this, (ValidLessonDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, startTime, length, comment);
    }

    private static final Equals<ValidLessonDTO> EQUALS = new Equals<>(Arrays.asList(ValidLessonDTO::getId,
            ValidLessonDTO::getGroupId, ValidLessonDTO::getStartTime, ValidLessonDTO::getLength,
            ValidLessonDTO::getComment));

    // GENERATED

    public interface GroupIdStep {
        TimeStep groupIdWithTeacher(Integer groupId, Integer teacherId);
    }

    public interface TimeStep {
        StartDateStep timeZone(DateTimeZone timeZone);
        StartDateStep timeZone(String timeZone);
        LengthStep startTime(Long startTime);
    }

    public interface StartDateStep {
        StartHourStep startDate(LocalDate startDate);
        StartHourStep startDate(String startDate);
    }

    public interface StartHourStep {
        StartMinuteStep startHour(int startHour);
    }

    public interface StartMinuteStep {
        LengthStep startMinute(int startMinute);
    }

    public interface LengthStep {
        BuildStep length(Integer length);
    }

    public interface BuildStep {
        BuildStep id(Long id);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        ValidLessonDTO build();
    }

    public static class Builder implements GroupIdStep, TimeStep, StartDateStep, StartHourStep, StartMinuteStep, LengthStep, BuildStep {
        private Long id;
        private Integer groupId;
        private Integer teacherId;
        private Long startTime;
        private DateTimeZone timeZone;
        private LocalDate startDate;
        private Integer startHour;
        private Integer startMinute;
        private Integer length;
        private String comment;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder groupIdWithTeacher(Integer groupId, Integer teacherId) {
            this.groupId = groupId;
            this.teacherId = teacherId;
            return this;
        }

        @Override
        public Builder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        @Override
        public Builder timeZone(DateTimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @Override
        public Builder timeZone(String timeZone) {
            this.timeZone = DateTimeZone.forID(timeZone);
            return this;
        }

        @Override
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder startDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        @Override
        public Builder startHour(int startHour) {
            this.startHour = startHour;
            return this;
        }

        @Override
        public Builder startMinute(int startMinute) {
            this.startMinute = startMinute;
            return this;
        }

        @Override
        public Builder length(Integer length) {
            this.length = length;
            return this;
        }

        @Override
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public Builder createdAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        @Override
        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        @Override
        public ValidLessonDTO build() {
            if (startTime == null)
                return ValidLessonDTO.fromTimeZone(id, groupId, teacherId, timeZone, startDate, startHour,
                        startMinute, length, comment, createdAt, updatedAt);

            return ValidLessonDTO.fromStartTime(id, groupId, teacherId, startTime, length, comment, createdAt, updatedAt);
        }

    }

}
