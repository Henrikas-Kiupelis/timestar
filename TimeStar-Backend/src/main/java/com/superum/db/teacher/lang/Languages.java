package com.superum.db.teacher.lang;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

public class Languages {

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
		return StringUtils.toString(
				"Teacher ID: " + teacherId,
				"Languages: " + languages);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Languages))
			return false;

		Languages other = (Languages) o;

		return this.teacherId == other.teacherId
				&& Objects.equals(this.languages, other.languages);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + teacherId;
		result = (result << 5) - result + (languages == null ? 0 : languages.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Languages(@JsonProperty("id") int teacherId, 
					@JsonProperty("languages") List<String> languages) {
		this.teacherId = teacherId;
		this.languages = languages != null ? languages : Collections.emptyList();
	}

	// PRIVATE

	@NotNull(message = "The teacher id must be set")
	private final int teacherId;
	
	private final List<String> languages;
	
}
