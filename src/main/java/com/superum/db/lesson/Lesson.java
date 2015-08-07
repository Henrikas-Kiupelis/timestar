package com.superum.db.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.superum.helper.JodaTimeConverter;
import com.superum.utils.StringUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.LESSON;

@JsonIgnoreProperties(ignoreUnknown = true)
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
		return new Lesson(id, groupId, teacherId, startDate, startHour, startMinute, endDate, endHour, endMinute, length, comment, createdAt, updatedAt);
	}
    @JsonIgnore
    public Lesson without() {
        return new Lesson(0, groupId, teacherId, startDate, startHour, startMinute, endDate, endHour, endMinute, length, comment, createdAt, updatedAt);
    }

	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}

    @JsonProperty("teacherId")
    public Integer getTeacherId() {
        return teacherId;
    }
	
	@JsonProperty("startDate")
    @JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate getStartDate() {
		return startDate;
	}
	
	@JsonProperty("startHour")
	public int getStartHour() {
		return startHour;
	}
	
	@JsonProperty("startMinute")
	public int getStartMinute() {
		return startMinute;
	}
	
	@JsonIgnore
	public long getStartTime() {
        return JodaTimeConverter.from(startDate, startHour, startMinute).toEpochMillis();
	}

    @JsonProperty("endDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getEndDate() {
        return endDate;
    }

	@JsonProperty("endHour")
	public int getEndHour() {
		return endHour;
	}
	
	@JsonProperty("endMinute")
	public int getEndMinute() {
		return endMinute;
	}

    @JsonIgnore
    public long getEndTime() {
        return endDate != null && endHour != null && endMinute != null
                ? JodaTimeConverter.from(endDate, endHour, endMinute).toEpochMillis()
                : JodaTimeConverter.from(startDate, startHour, startMinute + length).toEpochMillis();
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
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "Lesson" + StringUtils.toString(
				"Lesson ID: " + id,
				"Group ID: " + groupId,
                "Teacher ID: " + teacherId,
				"Start date: " + startDate,
				"Start time: " + startHour + ":" + startMinute,
                "End date: " + endDate,
				"End time: " + endHour + ":" + endMinute,
				"Length: " + length,
				"Comment: " + comment,
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Lesson))
			return false;

		Lesson other = (Lesson) o;

		return this.id == other.id
				&& this.groupId == other.groupId
				&& this.startHour == other.startHour
				&& this.startMinute == other.startMinute
                && this.length == other.length
                && Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
        return Objects.hash(id, groupId, startDate, startHour, startMinute, length, comment);
	}

	// CONSTRUCTORS

	public Lesson(@JsonProperty("id") long id,
				@JsonProperty("groupId") int groupId, 
				@JsonProperty("startDate") @JsonDeserialize(using=LocalDateDeserializer.class) LocalDate date,
				@JsonProperty("startHour") int startHour,
				@JsonProperty("startMinute") int startMinute,
				@JsonProperty("length") int length,
				@JsonProperty("comment") String comment) {
		this(id, groupId, null, date, startHour, startMinute, null, null, null, length, comment, null, null);
	}

    public Lesson(long id, int groupId, Integer teacherId, LocalDate startDate, int startHour, int startMinute, LocalDate endDate,
                  Integer endHour, Integer endMinute, int length, String comment, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.startDate = startDate;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endDate = endDate;
        this.endHour = endHour;
        this.endMinute = endMinute;
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

        long startTimestamp = lessonRecord.getValue(LESSON.TIME_OF_START);
        JodaTimeConverter start = JodaTimeConverter.from(startTimestamp);
		LocalDate startDate = start.toOrgJodaTimeLocalDate();
        int startHour = start.toHours();
        int startMinute = start.toMinutes();

        long endTimestamp = lessonRecord.getValue(LESSON.TIME_OF_END);
        JodaTimeConverter end = JodaTimeConverter.from(endTimestamp);
        LocalDate endDate = end.toOrgJodaTimeLocalDate();
        int endHour = end.toHours();
        int endMinute = end.toMinutes();

        int length = lessonRecord.getValue(LESSON.DURATION_IN_MINUTES);
		String comment = lessonRecord.getValue(LESSON.COMMENT);

        long createdTimestamp = lessonRecord.getValue(LESSON.CREATED_AT);
		Instant createdAt = new Instant(createdTimestamp);
        long updatedTimestamp = lessonRecord.getValue(LESSON.UPDATED_AT);
		Instant updatedAt = new Instant(updatedTimestamp);
		return new Lesson(id, groupId, teacherId, startDate, startHour, startMinute, endDate, endHour, endMinute, length, comment, createdAt, updatedAt);
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
	
	@NotNull(message = "The date must be set")
	private final LocalDate startDate;
	
	@Min(value = 0, message = "The hour must be at least 0")
	@Max(value = 23, message = "The hour must be at most 23")
	private final int startHour;
	
	@Min(value = 0, message = "The minute must be at least 0")
	@Max(value = 59, message = "The minute must be at most 59")
	private final int startMinute;

    private final LocalDate endDate;
	private final Integer endHour;
	private final Integer endMinute;
	
	@Min(value = 1, message = "The length of lesson must be set")
	private final int length;
	
	@NotNull(message = "The lesson must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

	private final Instant createdAt;
    private final Instant updatedAt;

    // GENERATED

	public interface GroupIdStep {
        StartDateStep groupIdWithTeacher(int groupId);
        StartDateStep groupIdWithTeacher(int groupId, Integer teacherId);
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
        EndTimeStep length(int length);
	}

    public interface EndTimeStep {
        BuildStep withEndTime();
        BuildStep withoutEndTime();
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


	public static class Builder implements GroupIdStep, StartDateStep, StartHourStep, StartMinuteStep, LengthStep, EndTimeStep, BuildStep {
		private long id;
		private int groupId;
		private Integer teacherId;
		private LocalDate startDate;
		private int startHour;
		private int startMinute;
		private LocalDate endDate;
		private Integer endHour;
		private Integer endMinute;
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
		public Builder groupIdWithTeacher(int groupId) {
			this.groupId = groupId;
			return this;
		}

        @Override
        public Builder groupIdWithTeacher(int groupId, Integer teacherId) {
            this.groupId = groupId;
            this.teacherId = teacherId;
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
        public Builder withEndTime() {
            JodaTimeConverter end = JodaTimeConverter.from(startDate, startHour, startMinute + length);
            endDate = end.toOrgJodaTimeLocalDate();
            endHour = end.toHours();
            endMinute = end.toMinutes();
            return this;
        }

        @Override
        public Builder withoutEndTime() {
			endDate = null;
			endHour = null;
			endMinute = null;
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
			return new Lesson(id, groupId, teacherId, startDate, startHour, startMinute, endDate, endHour,
                    endMinute, length, comment, createdAt, updatedAt);
		}
	}
}
