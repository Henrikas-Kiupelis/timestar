package com.superum.db.lesson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.google.common.base.MoreObjects;
import com.superum.api.exception.InvalidRequestException;
import eu.goodlike.libraries.jodatime.Time;
import eu.goodlike.neat.Null;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.LESSON;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Lesson {

	// PUBLIC API

	@JsonProperty("id")
	public long getId() {
		return id;
	}
    @JsonIgnore
	public boolean hasId() {
		return id > 0;
	}
	@JsonIgnore
	public Lesson withId(long id) {
		return new Lesson(id, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
	}
    @JsonIgnore
    public Lesson without() {
        return new Lesson(0, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
    }

	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}

    @JsonProperty("teacherId")
    public Integer getTeacherId() {
        return teacherId;
    }
	
    @JsonProperty("startTime")
	public long getStartTime() {
        return startTime;
	}

    @JsonProperty("endTime")
    public long getEndTime() {
        return endTime;
    }

	@JsonProperty("length")
	public int getLength() {
		return length;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

    @JsonProperty("createdAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }

	// CONSTRUCTORS

    @JsonCreator
	public static Lesson jsonInstance(@JsonProperty("id") long id,
                                      @JsonProperty("groupId") int groupId,
                                      @JsonProperty("startTime") Long startTime,
                                      @JsonProperty("timeZone") String timeZone,
                                      @JsonProperty("startDate") String startDate,
                                      @JsonProperty("startHour") Integer startHour,
                                      @JsonProperty("startMinute") Integer startMinute,
                                      @JsonProperty("length") int length,
                                      @JsonProperty("comment") String comment) {
        if (startTime == null) {
            Null.check(timeZone, startDate, startHour, startMinute)
                    .ifAny(() -> new InvalidRequestException("Must provide either startTime or timeZone, " +
                            "startDate, startHour and startMinute"));

            return fromTimeZone(id, groupId, null, DateTimeZone.forID(timeZone), LocalDate.parse(startDate),
                    startHour, startMinute, length, comment, null, null);
        }
        return fromStartTime(id, groupId, null, startTime, length, comment, null, null);
	}

    public static Lesson fromStartTime(long id, int groupId, Integer teacherId, long startTime, int length,
                                       String comment, Instant createdAt, Instant updatedAt) {
        long endTime = new Instant(startTime).plus(Duration.standardMinutes(length)).getMillis();
        return new Lesson(id, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
    }

    public static Lesson fromTimeZone(long id, int groupId, Integer teacherId, DateTimeZone timeZone,
                                      LocalDate startDate, int startHour, int startMinute, int length,
                                      String comment, Instant createdAt, Instant updatedAt) {
        long startTime = Time.forZone(timeZone).from(startDate, startHour, startMinute).toEpochMillis();
        return fromStartTime(id, groupId, teacherId, startTime, length, comment, createdAt, updatedAt);
    }

    public Lesson(long id, int groupId, Integer teacherId, long startTime, long endTime, int length, String comment,
                  Instant createdAt, Instant updatedAt) {
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
	
	public static Lesson valueOf(Record lessonRecord) {
		if (lessonRecord == null)
			return null;
		
		long id = lessonRecord.getValue(LESSON.ID);
		int groupId = lessonRecord.getValue(LESSON.GROUP_ID);
        int teacherId = lessonRecord.getValue(LESSON.TEACHER_ID);
        long startTime = lessonRecord.getValue(LESSON.TIME_OF_START);
        long endTime = lessonRecord.getValue(LESSON.TIME_OF_END);
        int length = lessonRecord.getValue(LESSON.DURATION_IN_MINUTES);
		String comment = lessonRecord.getValue(LESSON.COMMENT);

        long createdTimestamp = lessonRecord.getValue(LESSON.CREATED_AT);
		Instant createdAt = new Instant(createdTimestamp);
        long updatedTimestamp = lessonRecord.getValue(LESSON.UPDATED_AT);
		Instant updatedAt = new Instant(updatedTimestamp);
		return new Lesson(id, groupId, teacherId, startTime, endTime, length, comment, createdAt, updatedAt);
	}

    public static Builder builder() {
        return new Builder();
    }

    public static GroupIdStep stepBuilder() {
        return new Builder();
    }

	// PRIVATE

	@Min(value = 0, message = "Negative lesson ids not allowed")
	private final long id;

	@Min(value = 1, message = "The group id must be set")
	private final int groupId;

    private final Integer teacherId;

    private final long startTime;
    private final long endTime;
	
	@Min(value = 1, message = "The length of lesson must be set")
	private final int length;
	
	@NotNull(message = "The lesson must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

	private final Instant createdAt;
    private final Instant updatedAt;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Lesson")
                .add("Lesson id", id)
                .add("Group id", groupId)
                .add("Teacher id", teacherId)
                .add("Start time", startTime)
                .add("End time", endTime)
                .add("Length", length)
                .add("Comment", comment)
                .add("Created at", createdAt)
                .add("Updated at", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) &&
                Objects.equals(groupId, lesson.groupId) &&
                Objects.equals(startTime, lesson.startTime) &&
                Objects.equals(length, lesson.length) &&
                Objects.equals(comment, lesson.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, startTime, length, comment);
    }

    // GENERATED

	public interface GroupIdStep {
        TimeStep groupIdWithTeacher(int groupId, Integer teacherId);
	}

	public interface TimeStep {
        StartDateStep timeZone(DateTimeZone timeZone);
        StartDateStep timeZone(String timeZone);
        LengthStep startTime(long startTime);
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
        BuildStep length(int length);
	}

	public interface BuildStep {
        BuildStep id(long id);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
		Lesson build();
	}

	public static class Builder implements GroupIdStep, TimeStep, StartDateStep, StartHourStep, StartMinuteStep, LengthStep, BuildStep {
		private long id;
		private int groupId;
		private Integer teacherId;
        private Long startTime;
        private DateTimeZone timeZone;
		private LocalDate startDate;
		private int startHour;
		private int startMinute;
		private int length;
		private String comment;
		private Instant createdAt;
		private Instant updatedAt;

		private Builder() {}

		@Override
		public Builder id(long id) {
			this.id = id;
			return this;
		}

        @Override
        public Builder groupIdWithTeacher(int groupId, Integer teacherId) {
            this.groupId = groupId;
            this.teacherId = teacherId;
            return this;
        }

        @Override
        public Builder startTime(long startTime) {
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
        public Builder length(int length) {
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
		public Lesson build() {
            if (startTime == null)
			    return Lesson.fromTimeZone(id, groupId, teacherId, timeZone, startDate, startHour,
                        startMinute, length, comment, createdAt, updatedAt);

            return Lesson.fromStartTime(id, groupId, teacherId, startTime, length, comment, createdAt, updatedAt);
		}

    }
}
