package com.superum.db.customer.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

public class Student {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("customerId")
	public int getCustomerId() {
		return customerId;
	}
	
	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Student ID: " + id,
				"Customer ID: " + customerId,
				"Group ID: " + groupId,
				"Name: " + name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Student))
			return false;

		Student other = (Student) o;

		return this.id == other.id
				&& this.customerId == other.customerId
				&& this.groupId == other.groupId
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + customerId;
		result = (result << 5) - result + groupId;
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Student(@JsonProperty("id") int id, 
					@JsonProperty("id") int customerId, 
					@JsonProperty("id") int groupId, 
					@JsonProperty("id") String name) {
		this.id = id;
		this.customerId = customerId;
		this.groupId = groupId;
		this.name = name;
	}
	
	public static Student valueOf(Record studentRecord) {
		if (studentRecord == null)
			return null;
		
		int id = studentRecord.getValue(STUDENT.ID);
		int customerId = studentRecord.getValue(STUDENT.CUSTOMER_ID);
		int groupId = studentRecord.getValue(STUDENT.GROUP_ID);
		String name = studentRecord.getValue(STUDENT.NAME);
		return new Student(id, customerId, groupId, name);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative student ids not allowed")
	private int id;
	
	@Min(value = 1, message = "The customer id must be set")
	private final int customerId;
	
	@Min(value = 1, message = "The group id must be set")
	private final int groupId;
	
	@NotNull(message = "The student must have a name")
	@Size(max = 60, message = "Name size must not exceed 60 characters")
	private final String name;
	
}
