package com.superum.db.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.superum.helper.JodaLocalDate;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;

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
    @JsonIgnore
    public Customer withId(int id) {
        return new Customer(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }
    @JsonIgnore
    public Customer withoutId() {
        return new Customer(0, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }

	@JsonProperty("startDate")
	@JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate getStartDate() {
		return startDate;
	}

    @JsonIgnore
    public java.sql.Date getStartDateSql() {
		try {
			return JodaLocalDate.from(startDate).toJavaSqlDate();
		} catch (ParseException e) {
			throw new IllegalStateException("Error occurred when parsing date while writing Customer to database: " + startDate, e);
		}
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
	
	@JsonProperty("picture")
	public String getPicture() {
		return picture;
	}
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

    @JsonProperty("createdAt")
	@JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
	@JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "Customer" + StringUtils.toString(
				"Customer ID: " + id,
				"Contract started: " + startDate,
				"Name: " + name,
				"Phone: " + phone,
				"Website: " + website,
				"Picture: " + picture,
				"Comment: " + comment,
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Customer))
			return false;

		Customer other = (Customer) o;

		return this.id == other.id
				&& Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.website, other.website)
				&& Objects.equals(this.picture, other.picture)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, startDate, name, phone, website, picture, comment);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Customer(@JsonProperty("id") int id,
					@JsonProperty("startDate") @JsonDeserialize(using=LocalDateDeserializer.class) LocalDate startDate,
					@JsonProperty("name") String name, 
					@JsonProperty("phone") String phone,
					@JsonProperty("website") String website,
					@JsonProperty("picture") String picture,
					@JsonProperty("comment") String comment) {
        this(id, startDate, name, phone, website, picture, comment, null, null);
	}

	public Customer(int id, LocalDate startDate, String name, String phone, String website, String picture, String comment, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture != null ? picture : "";
        this.comment = comment != null ? comment : "";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Customer valueOf(Record customerRecord) {
		if (customerRecord == null)
			return null;
		
		int id = customerRecord.getValue(CUSTOMER.ID);
		LocalDate startDate = from(customerRecord.getValue(CUSTOMER.START_DATE));
		String name = customerRecord.getValue(CUSTOMER.NAME);
		String phone = customerRecord.getValue(CUSTOMER.PHONE);
		String website = customerRecord.getValue(CUSTOMER.WEBSITE);
		String pictureName = customerRecord.getValue(CUSTOMER.PICTURE);
		String comment = customerRecord.getValue(CUSTOMER.COMMENT);

        long createdTimestamp = customerRecord.getValue(CUSTOMER.CREATED_AT);
		Instant createdAt = new Instant(createdTimestamp);
        long updatedTimestamp = customerRecord.getValue(CUSTOMER.UPDATED_AT);
		Instant updatedAt = new Instant(updatedTimestamp);
		return new Customer(id, startDate, name, phone, website, pictureName, comment, createdAt, updatedAt);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative customer ids not allowed")
	private final int id;

	@NotNull(message = "There must be a start date")
	private final LocalDate startDate;

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
	private final String picture;
	
	@NotNull(message = "The customer must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

    private final Instant createdAt;
    private final Instant updatedAt;

	private static LocalDate from(java.sql.Date sqlDate) {
		try {
			return JodaLocalDate.from(sqlDate).toOrgJodaTimeLocalDate();
		} catch (ParseException e) {
			throw new IllegalStateException("Error occurred when parsing date while reading Customer from database: " + sqlDate, e);
		}
	}

}
