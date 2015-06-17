package com.superum.db.contract;

import static com.superum.db.generated.timestar.Tables.CONTRACT;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public boolean hasId() {
		return id > 0;
	}
	
	@JsonProperty("groupId")
	public int getGroupId() {
		return groupId;
	}
	
	@JsonProperty("paymentDay")
	public byte getPaymentDay() {
		return paymentDay;
	}
	
	@JsonProperty("startDate")
	public Date getStartDate() {
		return startDate;
	}
	
	@JsonProperty("languageLevel")
	public String getLanguageLevel() {
		return languageLevel;
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
				"Group ID: " + groupId,
				"Payment day: " + paymentDay,
				"Contract started: " + startDate,
				"Level: " + languageLevel,
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
				&& this.groupId == other.groupId
				&& this.paymentDay == other.paymentDay
				&& Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.languageLevel, other.languageLevel)
				&& Objects.equals(this.paymentValue, other.paymentValue);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + groupId;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (startDate == null ? 0 : startDate.hashCode());
		result = (result << 5) - result + (languageLevel == null ? 0 : languageLevel.hashCode());
		result = (result << 5) - result + (paymentValue == null ? 0 : paymentValue.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Contract(@JsonProperty("id") int id, 
					@JsonProperty("groupId") int groupId, 
					@JsonProperty("paymentDay") byte paymentDay, 
					@JsonProperty("startDate") Date startDate,
					@JsonProperty("languageLevel") String languageLevel,
					@JsonProperty("paymentValue") BigDecimal paymentValue) {
		this.id = id;
		this.groupId = groupId;
		this.paymentDay = paymentDay;
		this.startDate = startDate;
		this.languageLevel = languageLevel;
		this.paymentValue = paymentValue;
	}
	
	public static Contract valueOf(Record contractRecord) {
		if (contractRecord == null)
			return null;
		
		int id = contractRecord.getValue(CONTRACT.ID);
		int groupId = contractRecord.getValue(CONTRACT.GROUP_ID);
		byte paymentDay = contractRecord.getValue(CONTRACT.PAYMENT_DAY);
		Date startDate = contractRecord.getValue(CONTRACT.START_DATE);
		String languageLevel = contractRecord.getValue(CONTRACT.LANGUAGE_LEVEL);
		BigDecimal paymentValue = contractRecord.getValue(CONTRACT.PAYMENT_VALUE);
		return new Contract(id, groupId, paymentDay, startDate, languageLevel, paymentValue);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative contract ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The group id must be set")
	private final int groupId;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;
	
	@NotNull(message = "There must be a start date")
	private final Date startDate;
	
	@NotNull(message = "There must be a language level")
	@Size(max = 20, message = "Language level should be defined it at most 20 characters")
	private final String languageLevel;	
	
	@NotNull(message = "There must be a payment")
	private final BigDecimal paymentValue;

}
