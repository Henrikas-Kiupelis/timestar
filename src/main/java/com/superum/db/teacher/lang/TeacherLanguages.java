package com.superum.db.teacher.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.helper.utils.StringUtils;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
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
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "TeacherLanguages" + StringUtils.toString(
				"Teacher ID: " + teacherId,
				"Languages: " + languages);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof TeacherLanguages))
			return false;

		TeacherLanguages other = (TeacherLanguages) o;

		return this.teacherId == other.teacherId
				&& Objects.equals(this.languages, other.languages);
	}

	@Override
	public int hashCode() {
		return Objects.hash(teacherId, languages);
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
	
}
