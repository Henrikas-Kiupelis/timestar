package com.superum.db.group.student;

import com.fasterxml.jackson.annotation.*;
import com.superum.utils.ObjectUtils;
import com.superum.utils.RandomUtils;
import com.superum.utils.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.STUDENT;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

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
    public Student withId(int id) {
        return new Student(id, code, customerId, startDate, email, name, createdAt, updatedAt);
    }
    @JsonIgnore
    public Student withoutId() {
        return new Student(0, code, customerId, startDate, email, name, createdAt, updatedAt);
    }

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }
	@JsonIgnore
    public Student withCode(int code) {
        return new Student(id, code, customerId, startDate, email, name, createdAt, updatedAt);
    }
    @JsonIgnore
    public Student withoutCode() {
        return new Student(id, null, customerId, startDate, email, name, createdAt, updatedAt);
    }

    public Student withGeneratedCode() {
        return withCode(generateCode());
    }

	@JsonProperty("customerId")
	public Integer getCustomerId() {
		return customerId;
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
	
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}

    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public static int generateCode() {
        return RandomUtils.randomNumber(900000) + 100000;
    }
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "Student" + StringUtils.toString(
				"Student ID: " + id,
                "Student code: " + code,
				"Customer ID: " + customerId,
                "Start date: " + startDate,
				"Email: " + email,
				"Name: " + name,
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Student))
			return false;

		Student other = (Student) o;

		return this.id == other.id
                && Objects.equals(this.code, other.code)
				&& Objects.equals(this.customerId, other.customerId)
                && ObjectUtils.equalsJavaUtilDate(this.startDate, other.startDate)
				&& Objects.equals(this.email, other.email)
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, code, customerId, startDate, email, name);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Student(@JsonProperty("id") int id,
                   @JsonProperty("customerId") Integer customerId,
                   @JsonProperty("startDate")  Date startDate,
                   @JsonProperty("email") String email,
                   @JsonProperty("name") String name) {
		this(id, null, customerId, startDate, email, name, null, null);
	}

    public Student(int id, Integer code, Integer customerId, Date startDate, String email, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.code = code;
        this.customerId = customerId;
        this.startDate = startDate;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Student valueOf(Record studentRecord) {
		if (studentRecord == null)
			return null;
		
		int id = studentRecord.getValue(STUDENT.ID);
        int code = studentRecord.getValue(STUDENT.CODE);
        Integer customerId = studentRecord.getValue(STUDENT.CUSTOMER_ID);
        Date startDate = studentRecord.getValue(STUDENT.START_DATE);
		String email = studentRecord.getValue(STUDENT.EMAIL);
		String name = studentRecord.getValue(STUDENT.NAME);

        long createdTimestamp = studentRecord.getValue(STUDENT.CREATED_AT);
        Date createdAt = Date.from(Instant.ofEpochMilli(createdTimestamp));
        long updatedTimestamp = studentRecord.getValue(STUDENT.UPDATED_AT);
        Date updatedAt = Date.from(Instant.ofEpochMilli(updatedTimestamp));
		return new Student(id, code, customerId, startDate, email, name, createdAt, updatedAt);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative student ids not allowed")
	private final int id;

	private final Integer code;
	
	private final Integer customerId;

    @NotNull(message = "There must be a start date")
    private final Date startDate;
	
	@NotNull(message = "The student must have an email")
	@Size(max = 60, message = "Email size must not exceed 60 characters")
	@Email
	private final String email;
	
	@NotNull(message = "The student must have a name")
	@Size(max = 60, message = "Name size must not exceed 60 characters")
	private final String name;

    private final Date createdAt;
    private final Date updatedAt;
	
}
