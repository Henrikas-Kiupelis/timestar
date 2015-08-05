package com.superum.db.lesson.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.helper.utils.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonTable {

	// PUBLIC API
	
	@JsonProperty("totalTeacherCount")
	public int getTotalTeacherCount() {
		return totalTeacherCount;
	}

	@JsonProperty("teachers")
	public List<Teacher> getTeachers() {
		return teachers;
	}
	
	@JsonProperty("languages")
	public List<TeacherLanguages> getLanguages() {
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
		return "LessonTable" + StringUtils.toString(
				"Teachers total: " + totalTeacherCount,
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

		return this.totalTeacherCount == other.totalTeacherCount
				&& Objects.equals(this.teachers, other.teachers)
				&& Objects.equals(this.languages, other.languages)
				&& Objects.equals(this.customerLessonData, other.customerLessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalTeacherCount, teachers, languages, customerLessonData, totalData, paymentData);
	}

	// CONSTRUCTORS

	@JsonCreator
	public LessonTable(@JsonProperty("totalTeacherCount") int totalTeacherCount,
					   @JsonProperty("teachers") List<Teacher> teachers,
					   @JsonProperty("languages") List<TeacherLanguages> languages,
					   @JsonProperty("customerLessonData") List<CustomerLessonData> customerLessonData,
					   @JsonProperty("totalData") List<TotalLessonData> totalData,
					   @JsonProperty("paymentData") List<PaymentData> paymentData) {
		this.totalTeacherCount = totalTeacherCount;
		this.teachers = teachers;
		this.languages = languages;
		this.customerLessonData = customerLessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	private final int totalTeacherCount;
	
	@NotNull
	private final List<Teacher> teachers;
	
	@NotNull
	private final List<TeacherLanguages> languages;
	
	@NotNull
	private final List<CustomerLessonData> customerLessonData;
	
	@NotNull
	private final List<TotalLessonData> totalData;
	
	@NotNull
	private final List<PaymentData> paymentData;

}
