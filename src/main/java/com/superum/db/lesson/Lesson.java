package com.superum.db.lesson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.helper.TimeConverter;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.jooq.Record;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.LESSON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lesson {

	// PUBLIC API

	@JsonProperty("id")
	public long getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}

	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}
	
	@JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
	public Date getStartDate() {
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
		return TimeConverter.time(startDate, startHour, startMinute);
	}

    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
    public Date getEndDate() {
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

	@JsonProperty("length")
	public int getLength() {
		return length;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Lesson ID: " + id,
				"Group ID: " + groupId,
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
                && ObjectUtils.equalsJavaUtilDate(this.startDate, other.startDate)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, groupId, startDate, startHour, startMinute, length, comment);
	}

	// CONSTRUCTORS

	public Lesson(@JsonProperty("id") long id,
				@JsonProperty("groupId") int groupId, 
				@JsonProperty("date") Date date,
				@JsonProperty("startHour") int startHour,
				@JsonProperty("startMinute") int startMinute,
				@JsonProperty("length") int length,
				@JsonProperty("comment") String comment) {
		this(id, groupId, date, startHour, startMinute, null, null, null, length, comment, null, null);
	}

    public Lesson(long id, int groupId, Date startDate, int startHour, int startMinute, Date endDate,
                  Integer endHour, Integer endMinute, int length, String comment, Date createdAt, Date updatedAt) {
        this.id = id;
        this.groupId = groupId;
        this.startDate = startDate;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endDate = endDate;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.length = length;
        this.comment = comment != null ? comment : "";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Lesson valueOf(Record lessonRecord) {
		if (lessonRecord == null)
			return null;
		
		long id = lessonRecord.getValue(LESSON.ID);
		int groupId = lessonRecord.getValue(LESSON.GROUP_ID);

        long startTimestamp = lessonRecord.getValue(LESSON.TIME_OF_START);
        TimeConverter startConverter = new TimeConverter(startTimestamp);
		Date startDate = startConverter.date();
        int startHour = startConverter.hour();
        int startMinute = startConverter.minute();

        long endTimestamp = lessonRecord.getValue(LESSON.TIME_OF_END);
        TimeConverter endConverter = new TimeConverter(endTimestamp);
        Date endDate = endConverter.date();
        int endHour = endConverter.hour();
        int endMinute = endConverter.minute();

        int length = lessonRecord.getValue(LESSON.DURATION_IN_MINUTES);
		String comment = lessonRecord.getValue(LESSON.COMMENT);

        long createdTimestamp = lessonRecord.getValue(LESSON.CREATED_AT);
        Date createdAt = Date.from(Instant.ofEpochMilli(createdTimestamp));
        long updatedTimestamp = lessonRecord.getValue(LESSON.UPDATED_AT);
        Date updatedAt = Date.from(Instant.ofEpochMilli(updatedTimestamp));
		return new Lesson(id, groupId, startDate, startHour, startMinute, endDate, endHour, endMinute, length, comment, createdAt, updatedAt);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative lesson ids not allowed")
	private final long id;

	@Min(value = 1, message = "The group id must be set")
	private final int groupId;
	
	@NotNull(message = "The date must be set")
	private final Date startDate;
	
	@Min(value = 0, message = "The hour must be at least 0")
	@Max(value = 23, message = "The hour must be at most 23")
	private final int startHour;
	
	@Min(value = 0, message = "The minute must be at least 0")
	@Max(value = 59, message = "The minute must be at most 59")
	private final int startMinute;

    private final Date endDate;
	private final Integer endHour;
	private final Integer endMinute;
	
	@Min(value = 1, message = "The length of lesson must be set")
	private final int length;
	
	@NotNull(message = "The lesson must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

	private final Date createdAt;
    private final Date updatedAt;
	
}
