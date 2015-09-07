package com.superum.db.lesson.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.db.customer.Customer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CustomerLessonData {

	// PUBLIC API

	@JsonProperty("customer")
	public Customer getCustomer() {
		return customer;
	}
	
	@JsonProperty("customerContractLanguages")
	public CustomerLanguages getCustomerContractLanguages() {
		return customerContractLanguages;
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

	// CONSTRUCTORS

	@JsonCreator
	public CustomerLessonData(@JsonProperty("customer") Customer customer, 
							  @JsonProperty("customerContractLanguages") CustomerLanguages customerContractLanguages, 
							  @JsonProperty("teacherLessonData") List<TeacherLessonData> teacherLessonData,
							  @JsonProperty("totalData") TotalLessonData totalData, 
							  @JsonProperty("paymentData") PaymentData paymentData) {
		this.customer = customer;
		this.customerContractLanguages = customerContractLanguages;
		this.teacherLessonData = teacherLessonData;
		this.totalData = totalData;
		this.paymentData = paymentData;
	}

	// PRIVATE
	
	@NotNull
	private final Customer customer;
		
	@NotNull
	private final CustomerLanguages customerContractLanguages;
	
	@NotNull
	private final List<TeacherLessonData> teacherLessonData;
	
	@NotNull
	private final TotalLessonData totalData;
	
	@NotNull
	private final PaymentData paymentData;

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("CustomerLessonData")
				.add("Customer", customer)
				.add("Customer contract languages", customerContractLanguages)
				.add("Lesson data", teacherLessonData)
				.add("Total data", totalData)
				.add("Payment data", paymentData)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof CustomerLessonData))
			return false;

		CustomerLessonData other = (CustomerLessonData) o;

		return Objects.equals(this.customer, other.customer)
				&& Objects.equals(this.customerContractLanguages, other.customerContractLanguages)
				&& Objects.equals(this.teacherLessonData, other.teacherLessonData)
				&& Objects.equals(this.totalData, other.totalData)
				&& Objects.equals(this.paymentData, other.paymentData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, customerContractLanguages, teacherLessonData, totalData, paymentData);
	}

}
