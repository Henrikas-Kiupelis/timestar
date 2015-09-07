package com.superum.db.lesson.attendance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
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

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("LessonAttendance")
				.add("Lesson id", lessonId)
				.add("Students", studentIds)
				.toString();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonAttendance)) return false;
        LessonAttendance that = (LessonAttendance) o;
        return Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(studentIds, that.studentIds);
    }

    @Override
	public int hashCode() {
		return Objects.hash(lessonId, studentIds);
	}

}
