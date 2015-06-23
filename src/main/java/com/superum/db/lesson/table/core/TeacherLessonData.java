package com.superum.db.lesson.table.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherLessonData {

	// PUBLIC API

	@JsonProperty("lessonIds")
	public List<Long> getLessonIds() {
		return lessonIds;
	}
	
	@JsonProperty("duration")
	public int getDuration() {
		return duration;
	}
	
	@JsonProperty("cost")
	public BigDecimal getCost() {
		return cost;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Lesson IDs: " + lessonIds,
				"Duration: " + duration,
				"Cost: " + cost);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof TeacherLessonData))
			return false;

		TeacherLessonData other = (TeacherLessonData) o;

		return this.duration == other.duration
				&& Objects.equals(this.lessonIds, other.lessonIds)
				&& Objects.equals(this.cost, other.cost);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (lessonIds == null ? 0 : lessonIds.hashCode());
		result = (result << 5) - result + duration;
		result = (result << 5) - result + (cost == null ? 0 : cost.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public TeacherLessonData(@JsonProperty("lessonIds") List<Long> lessonIds,
							 @JsonProperty("duration") int duration,
							 @JsonProperty("cost") BigDecimal cost) {
		this.lessonIds = lessonIds;
		this.duration = duration;
		this.cost = cost;
	}

	// PRIVATE
	
	private final List<Long> lessonIds;
	private final int duration;
	private final BigDecimal cost;

}
