package com.superum.db.teacher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.jooq.Record;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.TEACHER;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Teacher {

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
	public int getPaymentDay() {
		return paymentDay;
	}

    @JsonProperty("hourlyWage")
    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    @JsonProperty("academicWage")
    public BigDecimal getAcademicWage() {
        return academicWage;
    }

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("surname")
	public String getSurname() {
		return surname;
	}
	
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
	
	@JsonProperty("city")
	public String getCity() {
		return city;
	}
	
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	
	@JsonProperty("picture")
	public String getPicture() {
		return picture;
	}
	
	@JsonProperty("document")
	public String getDocument() {
		return document;
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
		return "Teacher" + StringUtils.toString(
				"Teacher ID: " + id,
				"Payment day: " + paymentDay,
                "Hourly wage: " + hourlyWage,
                "Academic wage: " + academicWage,
				"Name: " + name,
				"Surname: " + surname,
				"Phone: " + phone,
				"City: " + city,
				"Email: " + email, 
				"Picture: " + picture,
				"Document: " + document,
				"Comment: " + comment,
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Teacher))
			return false;

		Teacher other = (Teacher) o;

		return this.id == other.id
				&& this.paymentDay == other.paymentDay
                && Objects.equals(this.hourlyWage, other.hourlyWage)
                && Objects.equals(this.academicWage, other.academicWage)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.surname, other.surname)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.city, other.city)
				&& Objects.equals(this.email, other.email)
				&& Objects.equals(this.picture, other.picture)
				&& Objects.equals(this.document, other.document)
				&& Objects.equals(this.comment, other.comment);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Teacher(@JsonProperty("id") int id,
                   @JsonProperty("paymentDay") int paymentDay,
                   @JsonProperty("hourlyWage") BigDecimal hourlyWage,
                   @JsonProperty("academicWage") BigDecimal academicWage,
                   @JsonProperty("name") String name,
                   @JsonProperty("surname") String surname,
                   @JsonProperty("phone") String phone,
                   @JsonProperty("city") String city,
                   @JsonProperty("email") String email,
                   @JsonProperty("picture") String picture,
                   @JsonProperty("document") String document,
                   @JsonProperty("comment") String comment) {
		this(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment, null, null);
	}

    public Teacher(int id, int paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name, String surname,
                   String phone, String city, String email, String picture, String document, String comment, Date createdAt, Date updatedAt) {
        this.id = id;
        this.paymentDay = paymentDay;
        this.hourlyWage = hourlyWage;
        this.academicWage = academicWage;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.picture = picture != null ? picture : "";
        this.document = document != null ? document : "";
        this.comment = comment != null ? comment : "";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Teacher valueOf(Record teacherRecord) {
		if (teacherRecord == null)
			return null;
		
		int id = teacherRecord.getValue(TEACHER.ID);
        int paymentDay = teacherRecord.getValue(TEACHER.PAYMENT_DAY);
        BigDecimal hourlyWage = teacherRecord.getValue(TEACHER.HOURLY_WAGE);
        BigDecimal academicWage = teacherRecord.getValue(TEACHER.ACADEMIC_WAGE);
		String name = teacherRecord.getValue(TEACHER.NAME);
		String surname = teacherRecord.getValue(TEACHER.SURNAME);
		String phone = teacherRecord.getValue(TEACHER.PHONE);
		String city = teacherRecord.getValue(TEACHER.CITY);
		String email = teacherRecord.getValue(TEACHER.EMAIL);
		String pictureName = teacherRecord.getValue(TEACHER.PICTURE);
		String documentName = teacherRecord.getValue(TEACHER.DOCUMENT);
		String comment = teacherRecord.getValue(TEACHER.COMMENT);

        long createdTimestamp = teacherRecord.getValue(TEACHER.CREATED_AT);
        Date createdAt = Date.from(Instant.ofEpochMilli(createdTimestamp));
        long updatedTimestamp = teacherRecord.getValue(TEACHER.UPDATED_AT);
        Date updatedAt = Date.from(Instant.ofEpochMilli(updatedTimestamp));
		return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, pictureName, documentName, comment, createdAt, updatedAt);
	}

	// PRIVATE
	
	@Min(value = 0, message = "Negative teacher ids not allowed")
	private final int id;
	
	@Min(value = 1, message = "The payment day must be at least the first day of the month")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final int paymentDay;

    @NotNull
    private final BigDecimal hourlyWage;

    @NotNull
    private final BigDecimal academicWage;
	
	@NotNull(message = "The teacher must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
	@NotNull(message = "The teacher must have a surname")
	@Size(max = 30, message = "Surname size must not exceed 30 characters")
	private final String surname;
	
	@NotNull(message = "The teacher must have a phone")
	@Size(max = 30, message = "Phone size must not exceed 30 characters")
	private final String phone;
	
	@NotNull(message = "The teacher must have a city")
	@Size(max = 30, message = "City size must not exceed 30 characters")
	private final String city;
	
	@NotNull(message = "The teacher must have an email")
	@Size(max = 60, message = "Email size must not exceed 60 characters")
	@Email
	private final String email;
	
	@NotNull(message = "The teacher must have a picture name")
	@Size(max = 100, message = "Picture name size must not exceed 100 characters")
	private final String picture;
	
	@NotNull(message = "The teacher must have a document name")
	@Size(max = 100, message = "Document name size must not exceed 100 characters")
	private final String document;
	
	@NotNull(message = "The teacher must have a comment, even if empty")
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

	private final Date createdAt;
    private final Date updatedAt;

}
