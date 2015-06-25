package com.superum.db.lesson.table.core;

import java.util.List;
import java.util.Objects;

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
				"Customer: " + customer,
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

		return Objects.equals(this.customer, other.customer)
				&& Objects.equals(this.lessonData, other.lessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (customer == null ? 0 : customer.hashCode());
		result = (result << 5) - result + (lessonData == null ? 0 : lessonData.hashCode());
		result = (result << 5) - result + (totalData == null ? 0 : totalData.hashCode());
		result = (result << 5) - result + (paymentData == null ? 0 : paymentData.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public CustomerLessonData(@JsonProperty("customer") Customer customer, 
							  @JsonProperty("lessonData") List<TeacherLessonData> lessonData,
							  @JsonProperty("totalData") TotalLessonData totalData, 
							  @JsonProperty("paymentData") PaymentData paymentData) {
		this.customer = customer;
		this.lessonData = lessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	private final Customer customer;
	private final List<TeacherLessonData> lessonData;
	private final TotalLessonData totalData;
	private final PaymentData paymentData;

}
