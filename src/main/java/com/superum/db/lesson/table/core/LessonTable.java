package com.superum.db.lesson.table.core;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.Languages;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonTable {

	// PUBLIC API

	@JsonProperty("teachers")
	public List<Teacher> getTeachers() {
		return teachers;
	}
	
	@JsonProperty("languages")
	public List<Languages> getLanguages() {
		return languages;
	}
	
	@JsonProperty("customerLessonData")
	public List<CustomerLessonData> getCustomerLessonData() {
		return customerLessonData;
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
				"Teachers: " + teachers,
				"Languages: " + languages,
				"Lesson data: " + customerLessonData,
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

		return Objects.equals(this.teachers, other.teachers)
				&& Objects.equals(this.languages, other.languages)
				&& Objects.equals(this.customerLessonData, other.customerLessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (teachers == null ? 0 : teachers.hashCode());
		result = (result << 5) - result + (languages == null ? 0 : languages.hashCode());
		result = (result << 5) - result + (customerLessonData == null ? 0 : customerLessonData.hashCode());
		result = (result << 5) - result + (totalData == null ? 0 : totalData.hashCode());
		result = (result << 5) - result + (paymentData == null ? 0 : paymentData.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public LessonTable(@JsonProperty("teachers") List<Teacher> teachers,
					   @JsonProperty("languages") List<Languages> languages,
					   @JsonProperty("customerLessonData") List<CustomerLessonData> customerLessonData,
					   @JsonProperty("totalData") List<TotalLessonData> totalData,
					   @JsonProperty("paymentData") List<PaymentData> paymentData) {
		this.teachers = teachers;
		this.languages = languages;
		this.customerLessonData = customerLessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	@NotNull
	private final List<Teacher> teachers;
	
	@NotNull
	private final List<Languages> languages;
	
	@NotNull
	private final List<CustomerLessonData> customerLessonData;
	
	@NotNull
	private final List<TotalLessonData> totalData;
	
	@NotNull
	private final List<PaymentData> paymentData;

}
