package com.superum.db.lesson.table.core;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonTable {

	// PUBLIC API

	@JsonProperty("teacherIds")
	public List<Integer> getTeacherIds() {
		return teacherIds;
	}
	
	@JsonProperty("lessonData")
	public List<CustomerLessonData> getLessonData() {
		return lessonData;
	}
	
	@JsonProperty("totalData")
	public List<TotalLessonData> getTotalData() {
		return totalData;
	}
	
	@JsonProperty("paymentData")
	public List<PaymentData> getPaymentData() {
		return paymentData;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Teacher IDs: " + teacherIds,
				"Lesson data: " + lessonData,
				"Total data: " + totalData,
				"Payment data: " + paymentData);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof LessonTable))
			return false;

		LessonTable other = (LessonTable) o;

		return Objects.equals(this.teacherIds, other.teacherIds)
				&& Objects.equals(this.lessonData, other.lessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (teacherIds == null ? 0 : teacherIds.hashCode());
		result = (result << 5) - result + (lessonData == null ? 0 : lessonData.hashCode());
		result = (result << 5) - result + (totalData == null ? 0 : totalData.hashCode());
		result = (result << 5) - result + (paymentData == null ? 0 : paymentData.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public LessonTable(@JsonProperty("teacherIds") List<Integer> teacherIds,
					   @JsonProperty("lessonData") List<CustomerLessonData> lessonData,
					   @JsonProperty("totalData") List<TotalLessonData> totalData,
					   @JsonProperty("paymentData") List<PaymentData> paymentData) {
		this.teacherIds = teacherIds;
		this.lessonData = lessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	@NotNull
	private final List<Integer> teacherIds;
	
	@NotNull
	private final List<CustomerLessonData> lessonData;
	
	@NotNull
	private final List<TotalLessonData> totalData;
	
	@NotNull
	private final List<PaymentData> paymentData;

}
