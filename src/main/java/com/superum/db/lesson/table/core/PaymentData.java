package com.superum.db.lesson.table.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentData {

	// PUBLIC API

	@JsonProperty("paymentDate")
	public Date getPaymentDate() {
		return paymentDate;
	}
	
	@JsonProperty("cost")
	public BigDecimal getCost() {
		return cost;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Payment date: " + paymentDate,
				"Cost: " + cost);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof PaymentData))
			return false;

		PaymentData other = (PaymentData) o;

		return Objects.equals(this.paymentDate, other.paymentDate)
				&& Objects.equals(this.cost, other.cost);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (paymentDate == null ? 0 : paymentDate.hashCode());
		result = (result << 5) - result + (cost == null ? 0 : cost.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public PaymentData(@JsonProperty("paymentDate") Date paymentDate,
					   @JsonProperty("cost") BigDecimal cost) {
		this.paymentDate = paymentDate;
		this.cost = cost;
	}

	// PRIVATE
	
	@NotNull
	private final Date paymentDate;
	
	@NotNull
	private final BigDecimal cost;

}
