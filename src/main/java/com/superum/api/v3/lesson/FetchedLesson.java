package com.superum.api.v3.lesson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * <pre>
 * DTO which primarily focuses on representing outgoing lesson data
 *
 * Instances of this class are created directly from a Record from the database
 *
 * Expect the following fields in JSON:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      id          : id of this lesson
 *      groupId     : id of the group that is having this lesson
 *      teacherId   : id of the teacher which is responsible for the group that is having this lesson
 *      startTime   : timestamp, representing the time this lesson started
 *      endTime     : timestamp, representing the time this lesson ended
 *      length      : duration of the lesson in minutes
 *      comment     : (can be null) comment teacher can make about this lesson
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "groupId": 1,
 *      "teacherId": 1,
 *      "startTime": 1442997507129,
 *      "endTime": 1443000207129,
 *      "length" : 45,
 *      "comment": "What a lesson",
 *      "createdAt":1442955600000,
 *      "updatedAt":1442997541588
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class FetchedLesson {

    @JsonProperty(ID_FIELD)
    public long getId() {
        return id;
    }

    @JsonProperty(GROUP_ID_FIELD)
    public int getGroupId() {
        return groupId;
    }

    @JsonProperty(TEACHER_ID_FIELD)
    public int getTeacherId() {
        return teacherId;
    }

    @JsonProperty(START_TIME_FIELD)
    public long getStartTime() {
        return startTime;
    }

    @JsonProperty(END_TIME_FIELD)
    public long getEndTime() {
        return endTime;
    }

    @JsonProperty(LENGTH_FIELD)
    public int getLength() {
        return length;
    }

    @JsonProperty(COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    @JsonProperty(CREATED_AT_FIELD)
    public long getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(UPDATED_AT_FIELD)
    public long getUpdatedAt() {
        return updatedAt;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static FetchedLesson jsonInstance(@JsonProperty(ID_FIELD) long id,
                                             @JsonProperty(GROUP_ID_FIELD) int groupId,
                                             @JsonProperty(TEACHER_ID_FIELD) int teacherId,
                                             @JsonProperty(START_TIME_FIELD) long startTime,
                                             @JsonProperty(END_TIME_FIELD) long endTime,
                                             @JsonProperty(LENGTH_FIELD) int length,
                                             @JsonProperty(COMMENT_FIELD) String comment,
                                             @JsonProperty(CREATED_AT_FIELD) long createdAt,
                                             @JsonProperty(UPDATED_AT_FIELD) long updatedAt) {
        return new FetchedLesson(id, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
    }

    public static IdStep stepBuilder() {
        return new Builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public FetchedLesson(long id, int groupId, int teacherId, long startTime, long endTime, int length,
                         String comment, long createdAt, long updatedAt) {
        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.length = length;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PRIVATE

    private final long id;
    private final int groupId;
    private final int teacherId;
    private final long startTime;
    private final long endTime;
    private final int length;
    private final String comment;
    private final long createdAt;
    private final long updatedAt;

    private static final String ID_FIELD = "id";
    private static final String GROUP_ID_FIELD = "groupId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String START_TIME_FIELD = "startTime";
    private static final String END_TIME_FIELD = "endTime";
    private static final String LENGTH_FIELD = "length";
    private static final String COMMENT_FIELD = "comment";
    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("groupId", groupId)
                .add("teacherId", teacherId)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("length", length)
                .add("comment", comment)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedLesson)) return false;
        FetchedLesson that = (FetchedLesson) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(length, that.length) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, teacherId, startTime, endTime, length, comment);
    }

    // GENERATED

    public interface IdStep {
        GroupIdStep withId(long id);
    }

    public interface GroupIdStep {
        TeacherIdStep withGroupId(int groupId);
    }

    public interface TeacherIdStep {
        StartTimeStep withTeacherId(int teacherId);
    }

    public interface StartTimeStep {
        EndTimeStep withStartTime(long startTime);
    }

    public interface EndTimeStep {
        LengthStep withEndTime(long endTime);
    }

    public interface LengthStep {
        CommentStep withLength(int length);
    }

    public interface CommentStep {
        CreatedAtStep withComment(String comment);
    }

    public interface CreatedAtStep {
        UpdatedAtStep withCreatedAt(long createdAt);
    }

    public interface UpdatedAtStep {
        BuildStep withUpdatedAt(long updatedAt);
    }

    public interface BuildStep {
        FetchedLesson build();
    }

    public static class Builder implements IdStep, GroupIdStep, TeacherIdStep, StartTimeStep, EndTimeStep, LengthStep, CommentStep, CreatedAtStep, UpdatedAtStep, BuildStep {
        private long id;
        private int groupId;
        private int teacherId;
        private long startTime;
        private long endTime;
        private int length;
        private String comment;
        private long createdAt;
        private long updatedAt;

        private Builder() {
        }

        @Override
        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder withGroupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        @Override
        public Builder withTeacherId(int teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        @Override
        public Builder withStartTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        @Override
        public LengthStep withEndTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        @Override
        public Builder withLength(int length) {
            this.length = length;
            return this;
        }

        @Override
        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public Builder withCreatedAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public Builder withUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public FetchedLesson build() {
            return new FetchedLesson(
                    this.id,
                    this.groupId,
                    this.teacherId,
                    this.startTime,
                    this.endTime,
                    this.length,
                    this.comment,
                    this.createdAt,
                    this.updatedAt
            );
        }

    }
}
