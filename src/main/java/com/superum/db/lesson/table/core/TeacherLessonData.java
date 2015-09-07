package com.superum.db.lesson.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import eu.goodlike.misc.SpecialUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
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
	
	@NotNull
	private final List<Long> lessonIds;
	
	private final int duration;
	
	@NotNull
	private final BigDecimal cost;

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("TeacherLessonData")
				.add("Lesson ids", lessonIds)
				.add("Duration", duration)
				.add("Cost", cost)
				.toString();
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
				&& SpecialUtils.equalsJavaMathBigDecimal(this.cost, other.cost);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lessonIds, duration, cost == null ? 0 : cost.doubleValue());
	}

}
