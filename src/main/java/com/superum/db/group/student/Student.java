package com.superum.db.group.student;

import static com.superum.db.generated.timestar.Tables.STUDENT;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}
	
	@JsonProperty("customerId")
	public int getCustomerId() {
		return customerId;
	}
	
	@JsonProperty("email")
	public String getEmail() {
		return email;
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
				"Group ID: " + groupId,
				"Customer ID: " + customerId, 
				"Email: " + email,
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
				&& this.groupId == other.groupId
				&& this.customerId == other.customerId
				&& Objects.equals(this.email, other.email)
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + groupId;
		result = (result << 5) - result + customerId;
		result = (result << 5) - result + (email == null ? 0 : email.hashCode());
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Student(@JsonProperty("id") int id, 
					@JsonProperty("groupId") int groupId, 
					@JsonProperty("customerId") int customerId, 
					@JsonProperty("email") String email, 
					@JsonProperty("name") String name) {
		this.id = id;
		this.groupId = groupId;
		this.customerId = customerId;
		this.email = email;
		this.name = name;
	}
	
	public static Student valueOf(Record studentRecord) {
		if (studentRecord == null)
			return null;
		
		int id = studentRecord.getValue(STUDENT.ID);
		int groupId = studentRecord.getValue(STUDENT.GROUP_ID);
		int customerId = studentRecord.getValue(STUDENT.CUSTOMER_ID);
		String email = studentRecord.getValue(STUDENT.EMAIL);
		String name = studentRecord.getValue(STUDENT.NAME);
		return new Student(id, groupId, customerId, email, name);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative student ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The group id must be set")
	private final int groupId;
	
	@Min(value = 1, message = "The customer id must be set")
	private final int customerId;
	
	@NotNull(message = "The student must have an email")
	@Size(max = 60, message = "Email size must not exceed 60 characters")
	@Email
	private final String email;
	
	@NotNull(message = "The student must have a name")
	@Size(max = 60, message = "Name size must not exceed 60 characters")
	private final String name;
	
}
