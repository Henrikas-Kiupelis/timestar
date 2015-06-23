package com.superum.db.teacher.contract;

import static com.superum.db.generated.timestar.Tables.TEACHER_CONTRACT;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherContract {

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
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Contract ID: " + id,
				"Payment day: " + paymentDay);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof TeacherContract))
			return false;

		TeacherContract other = (TeacherContract) o;

		return this.id == other.id
				&& this.paymentDay == other.paymentDay;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + paymentDay;
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public TeacherContract(@JsonProperty("id") int id, 
						   @JsonProperty("paymentDay") byte paymentDay) {
		this.id = id;
		this.paymentDay = paymentDay;
	}
	
	public static TeacherContract valueOf(Record contractRecord) {
		if (contractRecord == null)
			return null;
		
		int id = contractRecord.getValue(TEACHER_CONTRACT.ID);
		byte paymentDay = contractRecord.getValue(TEACHER_CONTRACT.PAYMENT_DAY);
		return new TeacherContract(id, paymentDay);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative contract ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;

}
