package com.superum.db.lesson.attendance;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

public class Attendance {

	// PUBLIC API
	
	public long getLessonId() {
		return lessonId;
	}
	
	public List<Integer> getStudentIds() {
		return studentIds;
	}

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Lesson ID: " + lessonId,
				"Students: " + studentIds);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Attendance))
			return false;

		Attendance other = (Attendance) o;

		return this.lessonId == other.lessonId
				&& Objects.equals(this.studentIds, other.studentIds);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (int)lessonId;
		result = (result << 5) - result + (studentIds == null ? 0 : studentIds.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Attendance(@JsonProperty("id") long lessonId,
					  @JsonProperty("studentIds") List<Integer> studentIds) {
		this.lessonId = lessonId;
		this.studentIds = studentIds;
	}

	// PRIVATE
	
	@Min(value = 1, message = "The lesson id must be set")
	private final long lessonId;
	
	private final List<Integer> studentIds;

}
