package com.superum.db.lesson.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import eu.goodlike.misc.SpecialUtils;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PaymentData {

	// PUBLIC API

	@JsonProperty("paymentDate")
	public LocalDate getPaymentDate() {
		return paymentDate;
	}
	
	@JsonProperty("cost")
	public BigDecimal getCost() {
		return cost;
	}

	// CONSTRUCTORS

	@JsonCreator
	public PaymentData(@JsonProperty("paymentDate") LocalDate paymentDate,
					   @JsonProperty("cost") BigDecimal cost) {
		this.paymentDate = paymentDate;
		this.cost = cost;
	}

	// PRIVATE
	
	@NotNull
	private final LocalDate paymentDate;
	
	@NotNull
	private final BigDecimal cost;

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("PaymentData")
				.add("Payment date", paymentDate)
				.add("Cost", cost)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof PaymentData))
			return false;

		PaymentData other = (PaymentData) o;

		return Objects.equals(this.paymentDate, other.paymentDate)
                && SpecialUtils.equalsJavaMathBigDecimal(this.cost, other.cost);
	}

	@Override
	public int hashCode() {
		return Objects.hash(paymentDate, cost == null ? 0 : cost.doubleValue());
	}

}
