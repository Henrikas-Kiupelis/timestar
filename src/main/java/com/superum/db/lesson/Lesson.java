package com.superum.db.lesson;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.db.lesson.TimeConverter.*;

import java.sql.Date;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

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
	
	@JsonProperty("teacherId")
	public int getTeacherId() {
		return teacherId;
	}
	
	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}
	
	@JsonProperty("date")
	public Date getDate() {
		return date;
	}
	
	@JsonProperty("hour")
	public byte getHour() {
		return hour;
	}
	
	@JsonProperty("minute")
	public byte getMinute() {
		return minute;
	}
	
	@JsonIgnore
	public short getTime() {
		return time(hour, minute);
	}
	
	@JsonProperty("length")
	public short getLength() {
		return length;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Lesson ID: " + id,
				"Teacher ID: " + teacherId,
				"Group ID: " + groupId,
				"Date: " + date,
				"Time: " + hour + ":" + minute,
				"Length: " + length,
				"Comment: " + comment);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Lesson))
			return false;

		Lesson other = (Lesson) o;

		return this.id == other.id
				&& this.teacherId == other.teacherId
				&& this.groupId == other.groupId
				&& this.hour == other.hour
				&& this.minute == other.minute
				&& this.length == other.length
				&& Objects.equals(this.date, other.date)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (int)id;
		result = (result << 5) - result + teacherId;
		result = (result << 5) - result + groupId;
		result = (result << 5) - result + hour;
		result = (result << 5) - result + minute;
		result = (result << 5) - result + length;
		result = (result << 5) - result + (date == null ? 0 : date.hashCode());
		result = (result << 5) - result + comment.hashCode();
		return result;
	}

	// CONSTRUCTORS

	public Lesson(@JsonProperty("id") long id, 
				@JsonProperty("teacherId") int teacherId, 
				@JsonProperty("groupId") int groupId, 
				@JsonProperty("date") Date date, 
				@JsonProperty("hour") byte hour, 
				@JsonProperty("minute") byte minute, 
				@JsonProperty("length") short length, 
				@JsonProperty("comment") String comment) {
		this.id = id;
		this.teacherId = teacherId;
		this.groupId = groupId;
		this.date = date;
		this.hour = hour;
		this.minute = minute;
		this.length = length;
		this.comment = comment != null ? comment : "";
	}
	
	public static Lesson valueOf(Record lessonRecord) {
		if (lessonRecord == null)
			return null;
		
		long id = lessonRecord.getValue(LESSON.ID);
		int teacherId = lessonRecord.getValue(LESSON.TEACHER_ID);
		int groupId = lessonRecord.getValue(LESSON.GROUP_ID);
		Date date = lessonRecord.getValue(LESSON.DATE_OF_LESSON);
		short time = lessonRecord.getValue(LESSON.TIME_OF_LESSON);
		byte hour = hour(time);
		byte minute = minute(time);
		short length = lessonRecord.getValue(LESSON.LENGTH_IN_MINUTES);
		String comment = lessonRecord.getValue(LESSON.COMMENT_ABOUT);
		return new Lesson(id, teacherId, groupId, date, hour, minute, length, comment);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative lesson ids not allowed")
	private final long id;
	
	@Min(value = 1, message = "The teacher id must be set")
	private final int teacherId;
	
	@Min(value = 1, message = "The group id must be set")
	private final int groupId;
	
	@NotNull(message = "The date must be set")
	private final Date date;
	
	@Min(value = 0, message = "The hour must be at least 0")
	@Max(value = 23, message = "The hour must be at most 23")
	private final byte hour;
	
	@Min(value = 0, message = "The minute must be at least 0")
	@Max(value = 59, message = "The minute must be at most 59")
	private final byte minute;
	
	@Min(value = 1, message = "The length of lesson must be set")
	private final short length;
	
	@NotNull(message = "The lesson must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;
	
}
