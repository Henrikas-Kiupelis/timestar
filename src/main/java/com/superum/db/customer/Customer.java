package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	@JsonIgnore
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
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
	
	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}
	
	@JsonProperty("pictureName")
	public String getPictureName() {
		return pictureName;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Customer ID: " + id,
				"Payment day: " + paymentDay,
				"Contract started: " + startDate,
				"Payment value: " + paymentValue, 
				"Name: " + name,
				"Phone: " + phone,
				"Website: " + website,
				"Picture: " + pictureName,
				"Comment: " + comment);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Customer))
			return false;

		Customer other = (Customer) o;

		return this.id == other.id
				&& this.paymentDay == other.paymentDay
				&& Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.paymentValue, other.paymentValue)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.website, other.website)
				&& Objects.equals(this.pictureName, other.pictureName)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (startDate == null ? 0 : startDate.hashCode());
		result = (result << 5) - result + (paymentValue == null ? 0 : paymentValue.hashCode());
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		result = (result << 5) - result + (phone == null ? 0 : phone.hashCode());
		result = (result << 5) - result + (website == null ? 0 : website.hashCode());
		result = (result << 5) - result + pictureName.hashCode();
		result = (result << 5) - result + comment.hashCode();
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Customer(@JsonProperty("id") int id,
					@JsonProperty("paymentDay") byte paymentDay, 
					@JsonProperty("startDate") Date startDate,
					@JsonProperty("paymentValue") BigDecimal paymentValue, 
					@JsonProperty("name") String name, 
					@JsonProperty("phone") String phone,
					@JsonProperty("website") String website,
					@JsonProperty("pictureName") String pictureName,
					@JsonProperty("comment") String comment) {
		this.id = id;
		this.paymentDay = paymentDay;
		this.startDate = startDate;
		this.paymentValue = paymentValue;
		this.name = name;
		this.phone = phone;
		this.website = website;
		this.pictureName = pictureName != null ? pictureName : "";
		this.comment = comment != null ? comment : "";
	}
	
	public static Customer valueOf(Record customerRecord) {
		if (customerRecord == null)
			return null;
		
		int id = customerRecord.getValue(CUSTOMER.ID);
		byte paymentDay = customerRecord.getValue(CUSTOMER.PAYMENT_DAY);
		Date startDate = customerRecord.getValue(CUSTOMER.START_DATE);
		BigDecimal paymentValue = customerRecord.getValue(CUSTOMER.PAYMENT_VALUE);
		String name = customerRecord.getValue(CUSTOMER.NAME);
		String phone = customerRecord.getValue(CUSTOMER.PHONE);
		String website = customerRecord.getValue(CUSTOMER.WEBSITE);
		String pictureName = customerRecord.getValue(CUSTOMER.PICTURE_NAME);
		String comment = customerRecord.getValue(CUSTOMER.COMMENT_ABOUT);
		return new Customer(id, paymentDay, startDate, paymentValue, name, phone, website, pictureName, comment);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative customer ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;
	
	@NotNull(message = "There must be a start date")
	private final Date startDate;
	
	@NotNull(message = "There must be a payment")
	private final BigDecimal paymentValue;
	
	@NotNull(message = "The customer must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
	@NotNull(message = "The customer must have a phone")
	@Size(max = 30, message = "Phone size must not exceed 30 characters")
	private final String phone;
	
	@NotNull(message = "The customer must have a website")
	@Size(max = 30, message = "Website size must not exceed 30 characters")
	private final String website;
	
	@NotNull(message = "The customer must have a website")
	@Size(max = 100, message = "Picture name size must not exceed 100 characters")
	private final String pictureName;
	
	@NotNull(message = "The customer must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

}
