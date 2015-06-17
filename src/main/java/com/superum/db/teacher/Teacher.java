package com.superum.db.teacher;

import static com.superum.db.generated.timestar.Tables.TEACHER;

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
public class Teacher {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("surname")
	public String getSurname() {
		return surname;
	}
	
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Teacher ID: " + id,
				"Name: " + name,
				"Surname: " + surname,
				"Phone: " + phone,
				"Comment: " + comment);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Teacher))
			return false;

		Teacher other = (Teacher) o;

		return this.id == other.id
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.surname, other.surname)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		result = (result << 5) - result + (surname == null ? 0 : surname.hashCode());
		result = (result << 5) - result + (phone == null ? 0 : phone.hashCode());
		result = (result << 5) - result + (comment == null ? 0 : comment.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Teacher(@JsonProperty("id") int id, 
					@JsonProperty("name") String name, 
					@JsonProperty("surname") String surname, 
					@JsonProperty("phone") String phone, 
					@JsonProperty("comment") String comment) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.comment = comment;
	}
	
	public static Teacher valueOf(Record teacherRecord) {
		if (teacherRecord == null)
			return null;
		
		int id = teacherRecord.getValue(TEACHER.ID);
		String name = teacherRecord.getValue(TEACHER.NAME);
		String surname = teacherRecord.getValue(TEACHER.SURNAME);
		String phone = teacherRecord.getValue(TEACHER.PHONE);
		String comment = teacherRecord.getValue(TEACHER.COMMENT_ABOUT);
		return new Teacher(id, name, surname, phone, comment);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative teacher ids not allowed")
	private final int id;
	
	@NotNull(message = "The teacher must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
	@NotNull(message = "The teacher must have a surname")
	@Size(max = 30, message = "Surname size must not exceed 30 characters")
	private final String surname;
	
	@NotNull(message = "The teacher must have a phone")
	@Size(max = 30, message = "Phone size must not exceed 30 characters")
	private final String phone;
	
	@NotNull(message = "The teacher must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

}
