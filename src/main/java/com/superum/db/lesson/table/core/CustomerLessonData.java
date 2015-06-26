package com.superum.db.lesson.table.core;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.db.customer.Customer;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerLessonData {

	// PUBLIC API

	@JsonProperty("customer")
	public Customer getCustomer() {
		return customer;
	}
	
	@JsonProperty("teacherLessonData")
	public List<TeacherLessonData> getTeacherLessonData() {
		return teacherLessonData;
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
				"Customer: " + customer,
				"Lesson data: " + teacherLessonData,
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

		return Objects.equals(this.customer, other.customer)
				&& Objects.equals(this.teacherLessonData, other.teacherLessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (customer == null ? 0 : customer.hashCode());
		result = (result << 5) - result + (teacherLessonData == null ? 0 : teacherLessonData.hashCode());
		result = (result << 5) - result + (totalData == null ? 0 : totalData.hashCode());
		result = (result << 5) - result + (paymentData == null ? 0 : paymentData.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public CustomerLessonData(@JsonProperty("customer") Customer customer, 
							  @JsonProperty("teacherLessonData") List<TeacherLessonData> teacherLessonData,
							  @JsonProperty("totalData") TotalLessonData totalData, 
							  @JsonProperty("paymentData") PaymentData paymentData) {
		this.customer = customer;
		this.teacherLessonData = teacherLessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	@NotNull
	private final Customer customer;
	
	@NotNull
	private final List<TeacherLessonData> teacherLessonData;
	
	@NotNull
	private final TotalLessonData totalData;
	
	@NotNull
	private final PaymentData paymentData;

}
