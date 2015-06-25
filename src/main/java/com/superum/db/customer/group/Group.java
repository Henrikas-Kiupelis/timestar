package com.superum.db.customer.group;

import static com.superum.db.generated.timestar.Tables.STUDENT_GROUP;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("teacherId")
	public Integer getTeacherId() {
		return teacherId;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Group ID: " + id,
				"Teacher ID: " + teacherId,
				"Name: " + name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Group))
			return false;

		Group other = (Group) o;

		return this.id == other.id
				&& Objects.equals(this.teacherId, other.teacherId)
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + (teacherId == null ? 0 : teacherId);
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Group(@JsonProperty("id") int id,
				@JsonProperty("teacherId") Integer teacherId,
				@JsonProperty("name") String name) {
		this.id = id;
		this.teacherId = teacherId;
		this.name = name;
	}
	
	public static Group valueOf(Record groupRecord) {
		if (groupRecord == null)
			return null;
		
		int id = groupRecord.getValue(STUDENT_GROUP.ID);
		Integer teacherId = groupRecord.getValue(STUDENT_GROUP.TEACHER_ID);
		String name = groupRecord.getValue(STUDENT_GROUP.NAME);
		return new Group(id, teacherId, name);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative group ids not allowed")
	private final int id;
	
	private final Integer teacherId;
	
	@NotNull(message = "The group must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
}
