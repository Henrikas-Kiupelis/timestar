package com.superum.db.group.student;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.google.common.base.MoreObjects;
import com.superum.helper.Random;
import eu.goodlike.libraries.jodatime.Time;
import org.hibernate.validator.constraints.Email;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.STUDENT;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
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
    @JsonIgnore
    public Student withGeneratedCode() {
        return withCode(generateCode());
    }

	@JsonProperty("customerId")
	public Integer getCustomerId() {
		return customerId;
	}

    @JsonProperty("startDate")
    public String getStartDateString() {
        return startDate.toString();
    }
    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }
    @JsonIgnore
    public java.sql.Date getStartDateSql() {
        return Time.convert(startDate).toSqlDate();
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
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static int generateCode() {
        return Random.numberWithDigits(6);
    }

	// CONSTRUCTORS

	@JsonCreator
	public Student(@JsonProperty("id") int id,
                   @JsonProperty("customerId") Integer customerId,
                   @JsonProperty("startDate") String startDate,
                   @JsonProperty("email") String email,
                   @JsonProperty("name") String name) {
		this(id, null, customerId, startDate == null ? null : LocalDate.parse(startDate), email, name, null, null);
	}

    public Student(int id, Integer code, Integer customerId, LocalDate startDate, String email, String name, Instant createdAt, Instant updatedAt) {
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
        Integer code = studentRecord.getValue(STUDENT.CODE);
        Integer customerId = studentRecord.getValue(STUDENT.CUSTOMER_ID);
        LocalDate startDate = Time.convert(studentRecord.getValue(STUDENT.START_DATE)).toJodaLocalDate();
		String email = studentRecord.getValue(STUDENT.EMAIL);
		String name = studentRecord.getValue(STUDENT.NAME);

        long createdTimestamp = studentRecord.getValue(STUDENT.CREATED_AT);
        Instant createdAt = new Instant(createdTimestamp);
        long updatedTimestamp = studentRecord.getValue(STUDENT.UPDATED_AT);
        Instant updatedAt = new Instant(updatedTimestamp);
		return new Student(id, code, customerId, startDate, email, name, createdAt, updatedAt);
	}

    public static Builder builder() {
        return new Builder();
    }

    public static CustomerIdStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

	@Min(value = 0, message = "Negative student ids not allowed")
	private final int id;

	private final Integer code;
	
	private final Integer customerId;

    @NotNull(message = "There must be a start date")
    private final LocalDate startDate;
	
	@NotNull(message = "The student must have an email")
	@Size(max = 60, message = "Email size must not exceed 60 characters")
	@Email
	private final String email;
	
	@NotNull(message = "The student must have a name")
	@Size(max = 60, message = "Name size must not exceed 60 characters")
	private final String name;

    private final Instant createdAt;
    private final Instant updatedAt;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Student")
                .add("Student id", id)
                .add("Student code", code)
                .add("Customer id", customerId)
                .add("Start date", startDate)
                .add("Email", email)
                .add("Name", name)
                .add("Created at", createdAt)
                .add("Updated at", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(code, student.code) &&
                Objects.equals(customerId, student.customerId) &&
                Objects.equals(startDate, student.startDate) &&
                Objects.equals(email, student.email) &&
                Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, customerId, startDate, email, name);
    }

    // GENERATED

    public interface CustomerIdStep {
        EmailStep customerId(Integer customerId);
        StartDateStep noCustomer();
    }

    public interface StartDateStep {
        EmailStep withStartDate(LocalDate startDate);
        EmailStep withStartDate(String startDate);
    }

    public interface EmailStep {
        NameStep withEmail(String email);
    }

    public interface NameStep {
        BuildStep withName(String name);
    }

    public interface BuildStep {
        BuildStep withId(int id);
        BuildStep withCode(Integer code);
        BuildStep withCreatedAt(Instant createdAt);
        BuildStep withCreatedAt(long createdAt);
        BuildStep withUpdatedAt(Instant updatedAt);
        BuildStep withUpdatedAt(long updatedAt);
        Student build();
    }


    public static class Builder implements CustomerIdStep, StartDateStep, EmailStep, NameStep, BuildStep {
        private int id;
        private Integer code;
        private Integer customerId;
        private LocalDate startDate;
        private String email;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder withCode(Integer code) {
            this.code = code;
            return this;
        }

        @Override
        public Builder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        @Override
        public Builder noCustomer() {
            this.customerId = null;
            return this;
        }

        @Override
        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder withStartDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        @Override
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public Builder withCreatedAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        @Override
        public Builder withUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public Builder withUpdatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        @Override
        public Student build() {
            return new Student(id, code, customerId, startDate, email, name, createdAt, updatedAt);
        }
    }
}
