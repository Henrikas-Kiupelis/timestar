package com.superum.db.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.superum.db.teacher.WageType;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.joda.time.Instant;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

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
    public Group withId(int id) {
        return new Group(id, customerId, teacherId, getUsesHourlyWage(), languageLevel, name, createdAt, updatedAt);
    }
    @JsonIgnore
    public Group withoutId() {
        return new Group(0, customerId, teacherId, getUsesHourlyWage(), languageLevel, name, createdAt, updatedAt);
    }
	
	@JsonProperty("customerId")
	public Integer getCustomerId() {
		return customerId;
	}

    @JsonProperty("teacherId")
    public int getTeacherId() {
        return teacherId;
    }

    @JsonProperty("usesHourlyWage")
    public boolean getUsesHourlyWage() {
        return wage == WageType.HOURLY;
    }

    @JsonProperty("languageLevel")
    public String getLanguageLevel() {
        return languageLevel;
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
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "Group" + StringUtils.toString(
				"Group ID: " + id,
                "Customer ID: " + customerId,
				"Teacher ID: " + teacherId,
                "WageType: " + wage,
                "Language level: " + languageLevel,
				"Name: " + name,
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Group))
			return false;

		Group other = (Group) o;

		return this.id == other.id
                && this.teacherId == other.teacherId
				&& Objects.equals(this.customerId, other.customerId)
                && Objects.equals(this.wage, other.wage)
                && Objects.equals(this.languageLevel, other.languageLevel)
                && Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, customerId, teacherId, wage, languageLevel, name);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Group(@JsonProperty("id") int id,
                 @JsonProperty("customerId") Integer customerId,
                 @JsonProperty("teacherId") int teacherId,
                 @JsonProperty("usesHourlyWage") boolean usesHourlyWage,
                 @JsonProperty("languageLevel") String languageLevel,
                 @JsonProperty("name") String name) {
		this(id, customerId, teacherId, usesHourlyWage, languageLevel, name, null, null);
	}

    public Group(int id, Integer customerId, int teacherId, boolean usesHourlyWage, String languageLevel, String name, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.teacherId = teacherId;
        this.wage = usesHourlyWage ? WageType.HOURLY : WageType.ACADEMIC;
        this.languageLevel = languageLevel;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Group valueOf(Record groupRecord) {
		if (groupRecord == null)
			return null;
		
		int id = groupRecord.getValue(GROUP_OF_STUDENTS.ID);
        Integer customerId = groupRecord.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID);
		int teacherId = groupRecord.getValue(GROUP_OF_STUDENTS.TEACHER_ID);
        boolean usesHourlyWage = groupRecord.getValue(GROUP_OF_STUDENTS.USE_HOURLY_WAGE);
        String languageLevel = groupRecord.getValue(GROUP_OF_STUDENTS.LANGUAGE_LEVEL);
		String name = groupRecord.getValue(GROUP_OF_STUDENTS.NAME);

        long createdTimestamp = groupRecord.getValue(GROUP_OF_STUDENTS.CREATED_AT);
        Instant createdAt = new Instant(createdTimestamp);
        long updatedTimestamp = groupRecord.getValue(GROUP_OF_STUDENTS.UPDATED_AT);
        Instant updatedAt = new Instant(updatedTimestamp);
		return new Group(id, customerId, teacherId, usesHourlyWage, languageLevel, name, createdAt, updatedAt);
	}

	// PRIVATE

	@Min(value = 0, message = "Negative group ids not allowed")
	private final int id;
	
	private final Integer customerId;

    @Min(value = 1, message = "Teacher id must be set")
    private final int teacherId;

    @NotNull(message = "The group must have a wage setting")
    private final WageType wage;

    @NotNull(message = "The group must have a language")
    @Size(max = 20, message = "Language size must not exceed 20 characters")
    private final String languageLevel;
	
	@NotNull(message = "The group must have a name")
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;

	private final Instant createdAt;
	private final Instant updatedAt;
	
}
