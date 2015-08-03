package com.superum.db.customer;

import com.fasterxml.jackson.annotation.*;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
	public Date getStartDate() {
		return startDate;
	}

    @JsonIgnore
    public java.sql.Date getStartDateSql() {
        return new java.sql.Date(startDate.getTime());
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
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
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
				&& ObjectUtils.equalsJavaUtilDate(this.startDate, other.startDate)
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
					@JsonProperty("startDate") Date startDate,
					@JsonProperty("name") String name, 
					@JsonProperty("phone") String phone,
					@JsonProperty("website") String website,
					@JsonProperty("picture") String picture,
					@JsonProperty("comment") String comment) {
        this(id, startDate, name, phone, website, picture, comment, null, null);
	}

	public Customer(int id, Date startDate, String name, String phone, String website, String picture, String comment, Date createdAt, Date updatedAt) {
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
        java.sql.Date startDateSql = customerRecord.getValue(CUSTOMER.START_DATE);
		Date startDate = new Date(startDateSql.getTime());
		String name = customerRecord.getValue(CUSTOMER.NAME);
		String phone = customerRecord.getValue(CUSTOMER.PHONE);
		String website = customerRecord.getValue(CUSTOMER.WEBSITE);
		String pictureName = customerRecord.getValue(CUSTOMER.PICTURE);
		String comment = customerRecord.getValue(CUSTOMER.COMMENT);

        long createdTimestamp = customerRecord.getValue(CUSTOMER.CREATED_AT);
        Date createdAt = new Date(createdTimestamp);
        long updatedTimestamp = customerRecord.getValue(CUSTOMER.UPDATED_AT);
        Date updatedAt = new Date(updatedTimestamp);
		return new Customer(id, startDate, name, phone, website, pictureName, comment, createdAt, updatedAt);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative customer ids not allowed")
	private final int id;

	@NotNull(message = "There must be a start date")
	private final Date startDate;

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

    private final Date createdAt;
    private final Date updatedAt;

}
