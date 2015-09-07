package com.superum.db.teacher.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TeacherLanguages {

	// PUBLIC API

	@JsonProperty("id")
	public int getTeacherId() {
		return teacherId;
	}

	@JsonProperty("languages")
	public List<String> getLanguages() {
		return languages;
	}

	// CONSTRUCTORS

	@JsonCreator
	public TeacherLanguages(@JsonProperty("id") int teacherId,
					@JsonProperty("languages") List<String> languages) {
		this.teacherId = teacherId;
		this.languages = languages != null ? languages : Collections.emptyList();
	}

	// PRIVATE

	@Min(value = 1, message = "The teacher id must be set")
	private final int teacherId;

	private final List<String> languages;

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("TeacherLanguages")
				.add("Teacher id", teacherId)
				.add("Languages", languages)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TeacherLanguages)) return false;
		TeacherLanguages that = (TeacherLanguages) o;
		return Objects.equals(teacherId, that.teacherId) &&
				Objects.equals(languages, that.languages);
	}

	@Override
	public int hashCode() {
		return Objects.hash(teacherId, languages);
	}

}
