package com.superum.db.customer;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
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
	public boolean hasId() {
		return id > 0;
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
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Customer ID: " + id,
				"Name: " + name,
				"Phone: " + phone,
				"Website: " + website,
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
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.website, other.website)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		result = (result << 5) - result + (phone == null ? 0 : phone.hashCode());
		result = (result << 5) - result + (website == null ? 0 : website.hashCode());
		result = (result << 5) - result + (comment == null ? 0 : comment.hashCode());
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Customer(@JsonProperty("id") int id,
					@JsonProperty("name") String name, 
					@JsonProperty("phone") String phone,
					@JsonProperty("website") String website,
					@JsonProperty("comment") String comment) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.website = website;
		this.comment = comment;
	}
	
	public static Customer valueOf(Record customerRecord) {
		if (customerRecord == null)
			return null;
		
		int id = customerRecord.getValue(CUSTOMER.ID);
		String name = customerRecord.getValue(CUSTOMER.NAME);
		String phone = customerRecord.getValue(CUSTOMER.PHONE);
		String website = customerRecord.getValue(CUSTOMER.WEBSITE);
		String comment = customerRecord.getValue(CUSTOMER.COMMENT_ABOUT);
		return new Customer(id, name, phone, website, comment);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative customer ids not allowed")
	private final int id;
	
	@NotNull(message = "The customer must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
	@NotNull(message = "The customer must have a phone")
	@Size(max = 30, message = "Phone size must not exceed 30 characters")
	private final String phone;
	
	@NotNull(message = "The customer must have a website")
	@Size(max = 30, message = "Website size must not exceed 30 characters")
	private final String website;
	
	@NotNull(message = "The customer must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

}
