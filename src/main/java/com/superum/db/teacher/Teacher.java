package com.superum.db.teacher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.superum.utils.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.joda.time.Instant;
import org.jooq.Record;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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
    @JsonIgnore
    public Teacher withId(int id) {
        return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment, createdAt, updatedAt);
    }
    @JsonIgnore
    public Teacher withoutId() {
        return new Teacher(0, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment, createdAt, updatedAt);
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
        return Objects.hash(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment);
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
                   String phone, String city, String email, String picture, String document, String comment, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.paymentDay = paymentDay;
        this.hourlyWage = hourlyWage;
        this.academicWage = academicWage;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.picture = picture;
        this.document = document;
        this.comment = comment;
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
		Instant createdAt = new Instant(createdTimestamp);
		long updatedTimestamp = teacherRecord.getValue(TEACHER.UPDATED_AT);
		Instant updatedAt = new Instant(updatedTimestamp);
		return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, pictureName, documentName, comment, createdAt, updatedAt);
	}

    public static Builder builder() {
        return new Builder();
    }

    public static PaymentDayStep stepBuilder() {
        return new Builder();
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

	private final Instant createdAt;
    private final Instant updatedAt;

    // GENERATED

	public interface PaymentDayStep {
		HourlyWageStep paymentDay(int paymentDay);
	}

	public interface HourlyWageStep {
		AcademicWageStep hourlyWage(BigDecimal hourlyWage);
        AcademicWageStep hourlyWage(double hourlyWage);
	}

	public interface AcademicWageStep {
		NameStep academicWage(BigDecimal academicWage);
        NameStep academicWage(double academicWage);
	}

	public interface NameStep {
		SurnameStep name(String name);
	}

	public interface SurnameStep {
		PhoneStep surname(String surname);
	}

	public interface PhoneStep {
		CityStep phone(String phone);
	}

	public interface CityStep {
		EmailStep city(String city);
	}

	public interface EmailStep {
        BuildStep email(String email);
	}

	public interface BuildStep {
        BuildStep id(int id);
        BuildStep picture(String picture);
        BuildStep document(String document);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
		Teacher build();
	}


	public static class Builder implements PaymentDayStep, HourlyWageStep, AcademicWageStep, NameStep, SurnameStep, PhoneStep, CityStep, EmailStep, BuildStep {
		private int id;
		private int paymentDay;
		private BigDecimal hourlyWage;
		private BigDecimal academicWage;
		private String name;
		private String surname;
		private String phone;
		private String city;
		private String email;
		private String picture;
		private String document;
		private String comment;
		private Instant createdAt;
		private Instant updatedAt;

		private Builder() {}

		@Override
		public Builder id(int id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder paymentDay(int paymentDay) {
			this.paymentDay = paymentDay;
			return this;
		}

		@Override
		public Builder hourlyWage(BigDecimal hourlyWage) {
			this.hourlyWage = hourlyWage;
			return this;
		}

        @Override
        public Builder hourlyWage(double hourlyWage) {
            this.hourlyWage = BigDecimal.valueOf(hourlyWage);
            return this;
        }

        @Override
		public Builder academicWage(BigDecimal academicWage) {
			this.academicWage = academicWage;
			return this;
		}

        @Override
        public Builder academicWage(double academicWage) {
            this.academicWage = BigDecimal.valueOf(academicWage);
            return this;
        }

        @Override
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder surname(String surname) {
			this.surname = surname;
			return this;
		}

		@Override
		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}

		@Override
		public Builder city(String city) {
			this.city = city;
			return this;
		}

		@Override
		public Builder email(String email) {
			this.email = email;
			return this;
		}

		@Override
		public Builder picture(String picture) {
			this.picture = picture;
			return this;
		}

		@Override
		public Builder document(String document) {
			this.document = document;
			return this;
		}

		@Override
		public Builder comment(String comment) {
			this.comment = comment;
			return this;
		}

		@Override
		public Builder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

        @Override
        public Builder createdAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        @Override
		public Builder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

        @Override
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        @Override
		public Teacher build() {
			return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment, createdAt, updatedAt);
		}
	}
}
