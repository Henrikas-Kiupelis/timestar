package com.superum.db.lesson.table.core;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerLessonData {

	// PUBLIC API

	@JsonProperty("customerId")
	public int getCustomerId() {
		return customerId;
	}
	
	@JsonProperty("lessonData")
	public List<TeacherLessonData> getLessonData() {
		return lessonData;
	}
	
	@JsonProperty("totalData")
	public TotalLessonData getTotalData() {
		return totalData;
	}
	
	@JsonProperty("paymentData")
	public PaymentData getPaymentData() {
		return paymentData;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Customer ID: " + customerId,
				"Lesson data: " + lessonData,
				"Total data: " + totalData,
				"Payment data: " + paymentData);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof CustomerLessonData))
			return false;

		CustomerLessonData other = (CustomerLessonData) o;

		return this.customerId == other.customerId
				&& Objects.equals(this.lessonData, other.lessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + customerId;
		result = (result << 5) - result + (lessonData == null ? 0 : lessonData.hashCode());
		result = (result << 5) - result + (totalData == null ? 0 : totalData.hashCode());
		result = (result << 5) - result + (paymentData == null ? 0 : paymentData.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public CustomerLessonData(int customerId, 
			List<TeacherLessonData> lessonData,
			TotalLessonData totalData, 
			PaymentData paymentData) {
		this.customerId = customerId;
		this.lessonData = lessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	private final int customerId;
	private final List<TeacherLessonData> lessonData;
	private final TotalLessonData totalData;
	private final PaymentData paymentData;

}
