package com.superum.db.group;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.google.common.base.MoreObjects;
import com.superum.db.teacher.WageType;
import com.superum.helper.Equals;
import org.joda.time.Instant;
import org.jooq.Record;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
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
        return MoreObjects.toStringHelper("Group")
                .add("Group id", id)
                .add("Customer id", customerId)
                .add("Teacher id", teacherId)
                .add("WageType", wage)
                .add("Language level", languageLevel)
                .add("Name", name)
                .add("Created at", createdAt)
                .add("Updated at", updatedAt)
                .toString();
	}

	@Override
	public boolean equals(Object o) {
        return this == o || o instanceof Group && EQUALS.equals(this, (Group) o);
	}

	@Override
	public int hashCode() {
        return Objects.hash(id, customerId, teacherId, wage, languageLevel, name);
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


    public static Builder builder() {
        return new Builder();
    }

    public static TeacherIdStep stepBuilder() {
        return new Builder();
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

    private static final Equals<Group> EQUALS = new Equals<>(Group::getId, Group::getTeacherId, Group::getCustomerId,
            Group::getUsesHourlyWage, Group::getLanguageLevel, Group::getName);

    // GENERATED

    public interface TeacherIdStep {
        UsesHourlyWageStep teacherId(int teacherId);
    }

    public interface UsesHourlyWageStep {
        LanguageLevelStep usesHourlyWage(boolean usesHourlyWage);
    }

    public interface LanguageLevelStep {
        NameStep languageLevel(String languageLevel);
    }

    public interface NameStep {
        BuildStep name(String name);
    }

    public interface BuildStep {
        BuildStep id(int id);
        BuildStep customerId(Integer customerId);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        Group build();
    }


    public static class Builder implements TeacherIdStep, UsesHourlyWageStep, LanguageLevelStep, NameStep, BuildStep {
        private int id;
        private Integer customerId;
        private int teacherId;
        private boolean usesHourlyWage;
        private String languageLevel;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        @Override
        public Builder teacherId(int teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        @Override
        public Builder usesHourlyWage(boolean usesHourlyWage) {
            this.usesHourlyWage = usesHourlyWage;
            return this;
        }

        @Override
        public Builder languageLevel(String languageLevel) {
            this.languageLevel = languageLevel;
            return this;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
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
        public Group build() {
            return new Group(id, customerId, teacherId, usesHourlyWage, languageLevel, name, createdAt, updatedAt);
        }
    }
}
