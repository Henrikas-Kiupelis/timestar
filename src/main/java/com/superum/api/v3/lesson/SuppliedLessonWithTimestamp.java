package com.superum.api.v3.lesson;

import com.google.common.base.MoreObjects;
import com.superum.api.v2.lesson.InvalidLessonException;
import eu.goodlike.v2.validate.Validate;

import java.util.Objects;

import static com.superum.api.core.CommonValidators.MANDATORY_JSON_ID;
import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_ID;

/**
 * SuppliedLesson version which uses timestamp to represent start time of lesson; all SuppliedLessons should eventually
 * be transformed into this
 */
public class SuppliedLessonWithTimestamp {

    public Integer getGroupId() {
        return groupId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Integer getLength() {
        return length;
    }

    public String getComment() {
        return comment;
    }

    public void validateForCreation() {
        MANDATORY_JSON_ID.ifInvalid(groupId).thenThrow(this::groupIdError);
        Validate.longInt().not().isNull().isAtLeast(0).ifInvalid(startTime).thenThrow(this::startTimeError);
        Validate.integer().not().isNull().isAtLeast(1).ifInvalid(length).thenThrow(this::lengthError);
        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT).ifInvalid(comment).thenThrow(this::commentError);
    }

    public void validateForUpdate() {
        OPTIONAL_JSON_ID.ifInvalid(groupId).thenThrow(this::groupIdError);
        Validate.longInt().isNull().or().isAtLeast(0).ifInvalid(startTime).thenThrow(this::startTimeError);
        Validate.integer().isNull().or().isAtLeast(1).ifInvalid(length).thenThrow(this::lengthError);
        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT).ifInvalid(comment).thenThrow(this::commentError);
    }

    // CONSTRUCTORS

    public static GroupIdStep stepBuilder() {
        return new Builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public SuppliedLessonWithTimestamp(Integer groupId, Long startTime, Integer length, String comment) {
        this.groupId = groupId;
        this.startTime = startTime;
        this.length = length;
        this.comment = comment;
    }

    // PRIVATE

    private final Integer groupId;
    private final Long startTime;
    private final Integer length;
    private final String comment;

    private InvalidLessonException groupIdError() {
        return new InvalidLessonException("Group id for lesson must be positive, not: " + groupId);
    }

    private InvalidLessonException startTimeError() {
        return new InvalidLessonException("Lesson start time must be non-negative, not: " + startTime);
    }

    private InvalidLessonException lengthError() {
        return new InvalidLessonException("Lesson length must be positive, not: " + length);
    }

    private InvalidLessonException commentError() {
        return new InvalidLessonException("Lesson comment must not exceed " + COMMENT_SIZE_LIMIT + " chars: " + comment);
    }

    private static final int COMMENT_SIZE_LIMIT = 500;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("startTime", startTime)
                .add("length", length)
                .add("comment", comment)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuppliedLessonWithTimestamp)) return false;
        SuppliedLessonWithTimestamp that = (SuppliedLessonWithTimestamp) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(length, that.length) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, startTime, length, comment);
    }

    // GENERATED

    public interface GroupIdStep {
        StartTimeStep withGroupId(Integer groupId);
    }

    public interface StartTimeStep {
        LengthStep withStartTime(Long startTime);
    }

    public interface LengthStep {
        CommentStep withLength(Integer length);
    }

    public interface CommentStep {
        BuildStep withComment(String comment);
    }

    public interface BuildStep {
        SuppliedLessonWithTimestamp build();
    }

    public static class Builder implements GroupIdStep, StartTimeStep, LengthStep, CommentStep, BuildStep {
        private Integer groupId;
        private Long startTime;
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
        public SuppliedLessonWithTimestamp build() {
            return new SuppliedLessonWithTimestamp(
                    this.groupId,
                    this.startTime,
                    this.length,
                    this.comment
            );
        }
    }
}
