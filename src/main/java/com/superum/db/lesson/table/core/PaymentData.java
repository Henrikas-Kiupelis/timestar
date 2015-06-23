package com.superum.db.lesson.table.core;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentData {

	// PUBLIC API

	@JsonProperty("paymentDay")
	public byte getPaymentDay() {
		return paymentDay;
	}
	
	@JsonProperty("cost")
	public BigDecimal getCost() {
		return cost;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Payment day: " + paymentDay,
				"Cost: " + cost);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof PaymentData))
			return false;

		PaymentData other = (PaymentData) o;

		return this.paymentDay == other.paymentDay
				&& Objects.equals(this.cost, other.cost);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (cost == null ? 0 : cost.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public PaymentData(@JsonProperty("paymentDay") byte paymentDay,
					   @JsonProperty("cost") BigDecimal cost) {
		this.paymentDay = paymentDay;
		this.cost = cost;
	}

	// PRIVATE
	
	private final byte paymentDay;
	private final BigDecimal cost;

}
