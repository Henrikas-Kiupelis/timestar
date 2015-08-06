package com.superum.db.lesson.attendance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.helper.utils.StringUtils;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonAttendance {

	// PUBLIC API

    @JsonProperty("id")
	public long getLessonId() {
		return lessonId;
	}

    @JsonProperty("studentIds")
	public List<Integer> getStudentIds() {
		return studentIds;
	}

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "ExtendedLessonAttendance" + StringUtils.toString(
				"Lesson ID: " + lessonId,
				"Students: " + studentIds);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof LessonAttendance))
			return false;

		LessonAttendance other = (LessonAttendance) o;

		return this.lessonId == other.lessonId
				&& Objects.equals(this.studentIds, other.studentIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lessonId, studentIds);
	}

	// CONSTRUCTORS

	@JsonCreator
	public LessonAttendance(@JsonProperty("id") long lessonId,
                            @JsonProperty("studentIds") List<Integer> studentIds) {
		this.lessonId = lessonId;
		this.studentIds = studentIds;
	}

	// PRIVATE
	
	@Min(value = 1, message = "The lesson id must be set")
	private final long lessonId;
	
	private final List<Integer> studentIds;

}
