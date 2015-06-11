package com.superum.db.contract;

import static com.superum.db.generated.timestar.Tables.CONTRACT;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

public class Contract {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("teacherId")
	public int getTeacherId() {
		return teacherId;
	}
	
	@JsonProperty("customerId")
	public int getCustomerId() {
		return customerId;
	}
	
	@JsonProperty("paymentDay")
	public byte getPaymentDay() {
		return paymentDay;
	}
	
	@JsonProperty("paymentValue")
	public BigDecimal getPaymentValue() {
		return paymentValue;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Contract ID: " + id,
				"Teacher ID: " + teacherId,
				"Customer ID: " + customerId,
				"Payment day: " + paymentDay,
				"Payment value: " + paymentValue);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Contract))
			return false;

		Contract other = (Contract) o;

		return this.id == other.id
				&& this.teacherId == other.teacherId
				&& this.customerId == other.customerId
				&& this.paymentDay == other.paymentDay
				&& Objects.equals(this.paymentValue, other.paymentValue);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + teacherId;
		result = (result << 5) - result + customerId;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (paymentValue == null ? 0 : paymentValue.hashCode());;
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Contract(@JsonProperty("id") int id, 
					@JsonProperty("teacherId") int teacherId, 
					@JsonProperty("customerId") int customerId, 
					@JsonProperty("paymentDay") byte paymentDay, 
					@JsonProperty("paymentValue") BigDecimal paymentValue) {
		this.id = id;
		this.teacherId = teacherId;
		this.customerId = customerId;
		this.paymentDay = paymentDay;
		this.paymentValue = paymentValue;
	}
	
	public static Contract valueOf(Record contractRecord) {
		if (contractRecord == null)
			return null;
		
		int id = contractRecord.getValue(CONTRACT.ID);
		int teacherId = contractRecord.getValue(CONTRACT.TEACHER_ID);
		int customerId = contractRecord.getValue(CONTRACT.CUSTOMER_ID);
		byte paymentDay = contractRecord.getValue(CONTRACT.PAYMENT_DAY);
		BigDecimal paymentValue = contractRecord.getValue(CONTRACT.PAYMENT_VALUE);
		return new Contract(id, teacherId, customerId, paymentDay, paymentValue);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative contract ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The teacher id must be set")
	private final int teacherId;
	
	@Min(value = 1, message = "The customer id must be set")
	private final int customerId;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;
	
	@NotNull(message = "There must be a payment")
	private final BigDecimal paymentValue;

}
