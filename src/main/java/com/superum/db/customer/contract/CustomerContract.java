package com.superum.db.customer.contract;

import static com.superum.db.generated.timestar.Tables.CUSTOMER_CONTRACT;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerContract {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("paymentDay")
	public byte getPaymentDay() {
		return paymentDay;
	}
	
	@JsonProperty("startDate")
	public Date getStartDate() {
		return startDate;
	}
	
	@JsonProperty("paymentValue")
	public BigDecimal getPaymentValue() {
		return paymentValue;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Customer ID: " + id,
				"Payment day: " + paymentDay,
				"Contract started: " + startDate,
				"Payment value: " + paymentValue);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof CustomerContract))
			return false;

		CustomerContract other = (CustomerContract) o;

		return this.id == other.id
				&& this.paymentDay == other.paymentDay
				&& Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.paymentValue, other.paymentValue);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (startDate == null ? 0 : startDate.hashCode());
		result = (result << 5) - result + (paymentValue == null ? 0 : paymentValue.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public CustomerContract(@JsonProperty("id") int id, 
					@JsonProperty("paymentDay") byte paymentDay, 
					@JsonProperty("startDate") Date startDate,
					@JsonProperty("paymentValue") BigDecimal paymentValue) {
		this.id = id;
		this.paymentDay = paymentDay;
		this.startDate = startDate;
		this.paymentValue = paymentValue;
	}
	
	public static CustomerContract valueOf(Record contractRecord) {
		if (contractRecord == null)
			return null;
		
		int id = contractRecord.getValue(CUSTOMER_CONTRACT.CUSTOMER_ID);
		byte paymentDay = contractRecord.getValue(CUSTOMER_CONTRACT.PAYMENT_DAY);
		Date startDate = contractRecord.getValue(CUSTOMER_CONTRACT.START_DATE);
		BigDecimal paymentValue = contractRecord.getValue(CUSTOMER_CONTRACT.PAYMENT_VALUE);
		return new CustomerContract(id, paymentDay, startDate, paymentValue);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative contract ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;
	
	@NotNull(message = "There must be a start date")
	private final Date startDate;
	
	@NotNull(message = "There must be a payment")
	private final BigDecimal paymentValue;

}
