package com.superum.api.v2.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.v2.core.DTOWithTimestamps;
import org.joda.time.Instant;
import org.jooq.Record;

import java.util.Objects;

import static timestar_v2.Tables.GROUP_OF_STUDENTS;

/**
 * <pre>
 * Data Transport Object for groups
 *
 * This object is used to de-serialize and serialize JSON that is coming in and out of the back end;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When parsing an instance of ValidDataDTO with JSON, these fields are considered mandatory for create() operation:
 *      FIELD_NAME     : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      teacherId      : id of the teacher responsible for this group              1 <= teacherId
 *      usesHourlyWage : true: hourly wage is used; false: academic wage is used   any boolean
 *      languageLevel  : language and level taught to this group                   any String, max 20 chars
 *      name           : name of this group                                        any String, max 30 chars
 * These fields should only be specified if they are known:
 *      customerId     : id of the customer this group belongs to;                 1 <= customerId
 *                       if this is null, the group has no customer
 *      id             : number representation of this group in the system         1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "id": 1,
 *      "teacherId": 1,
 *      "usesHourlyWage": false,
 *      "languageLevel": "English: C1",
 *      "name": "My Group"
 * }
 * or:
 * {
 *      "id": 1,
 *      "customerId": 1,
 *      "teacherId": 1,
 *      "usesHourlyWage": false,
 *      "languageLevel": "English: C1",
 *      "name": "My Group"
 * }
 *
 * When returning an instance of ValidDataDTO with JSON, these additional fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "customerId": null,
 *      "teacherId": 1,
 *      "usesHourlyWage": false,
 *      "languageLevel": "English: C1",
 *      "name": "My Group",
 *      "createdAt": 1439499600000,
 *      "updatedAt": 1439551184130
 * }
 * or:
 * {
 *      "id": 1,
 *      "customerId": 1,
 *      "teacherId": 1,
 *      "usesHourlyWage": false,
 *      "languageLevel": "English: C1",
 *      "name": "My Group",
 *      "createdAt": 1439499600000,
 *      "updatedAt": 1439551184130
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidGroupDTO extends DTOWithTimestamps {

    @JsonProperty(ID_FIELD)
    public Integer getId() {
        return id;
    }
    public ValidGroupDTO withoutId() {
        return new ValidGroupDTO(null, customerId, teacherId, usesHourlyWage, languageLevel, name, getCreatedAt(), getUpdatedAt());
    }
    public ValidGroupDTO withId(Integer id) {
        return new ValidGroupDTO(id, customerId, teacherId, usesHourlyWage, languageLevel, name, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(CUSTOMER_ID_FIELD)
    public Integer getCustomerId() {
        return customerId;
    }
    public ValidGroupDTO withCustomerId(Integer customerId) {
        return new ValidGroupDTO(id, customerId, teacherId, usesHourlyWage, languageLevel, name, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(TEACHER_ID_FIELD)
    public Integer getTeacherId() {
        return teacherId;
    }

    @JsonProperty(USES_HOURLY_WAGE_FIELD)
    public Boolean getUsesHourlyWage() {
        return usesHourlyWage;
    }

    @JsonProperty(LANGUAGE_LEVEL_FIELD)
    public String getLanguageLevel() {
        return languageLevel;
    }

    @JsonProperty(NAME_FIELD)
    public String getName() {
        return name;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidGroupDTO jsonInstance(@JsonProperty(ID_FIELD) Integer id,
                                             @JsonProperty(CUSTOMER_ID_FIELD) Integer customerId,
                                             @JsonProperty(TEACHER_ID_FIELD) Integer teacherId,
                                             @JsonProperty(USES_HOURLY_WAGE_FIELD) Boolean usesHourlyWage,
                                             @JsonProperty(LANGUAGE_LEVEL_FIELD) String languageLevel,
                                             @JsonProperty(NAME_FIELD) String name) {
        return new ValidGroupDTO(id, customerId, teacherId, usesHourlyWage, languageLevel, name, null, null);
    }

    public ValidGroupDTO(Integer id, Integer customerId, Integer teacherId, Boolean usesHourlyWage,
                         String languageLevel, String name, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
        this.customerId = customerId;
        this.teacherId = teacherId;
        this.usesHourlyWage = usesHourlyWage;
        this.languageLevel = languageLevel;
        this.name = name;
    }

    public static ValidGroupDTO valueOf(Record record) {
        if (record == null)
            return null;

        return stepBuilder()
                .teacherId(record.getValue(GROUP_OF_STUDENTS.TEACHER_ID))
                .usesHourlyWage(record.getValue(GROUP_OF_STUDENTS.USE_HOURLY_WAGE))
                .languageLevel(record.getValue(GROUP_OF_STUDENTS.LANGUAGE_LEVEL))
                .name(record.getValue(GROUP_OF_STUDENTS.NAME))
                .id(record.getValue(GROUP_OF_STUDENTS.ID))
                .customerId(record.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID))
                .createdAt(record.getValue(GROUP_OF_STUDENTS.CREATED_AT))
                .updatedAt(record.getValue(GROUP_OF_STUDENTS.UPDATED_AT))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TeacherIdStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

    private final Integer id;
    private final Integer customerId;
    private final Integer teacherId;
    private final Boolean usesHourlyWage;
    private final String languageLevel;
    private final String name;

    // FIELDS NAMES

    private static final String ID_FIELD = "id";
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String USES_HOURLY_WAGE_FIELD = "usesHourlyWage";
    private static final String LANGUAGE_LEVEL_FIELD = "languageLevel";
    private static final String NAME_FIELD = "name";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidGroup")
                .add(ID_FIELD, id)
                .add(CUSTOMER_ID_FIELD, customerId)
                .add(TEACHER_ID_FIELD, teacherId)
                .add(USES_HOURLY_WAGE_FIELD, usesHourlyWage)
                .add(LANGUAGE_LEVEL_FIELD, languageLevel)
                .add(NAME_FIELD, name)
                .add(CREATED_AT_FIELD, getCreatedAt())
                .add(UPDATED_AT_FIELD, getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidGroupDTO)) return false;
        ValidGroupDTO that = (ValidGroupDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(usesHourlyWage, that.usesHourlyWage) &&
                Objects.equals(languageLevel, that.languageLevel) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, teacherId, usesHourlyWage, languageLevel, name);
    }

    // GENERATED

    public interface TeacherIdStep {
        UsesHourlyWageStep teacherId(Integer teacherId);
    }

    public interface UsesHourlyWageStep {
        LanguageLevelStep usesHourlyWage(Boolean usesHourlyWage);
    }

    public interface LanguageLevelStep {
        NameStep languageLevel(String languageLevel);
    }

    public interface NameStep {
        BuildStep name(String name);
    }

    public interface BuildStep {
        BuildStep id(Integer id);
        BuildStep customerId(Integer customerId);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        ValidGroupDTO build();
    }

    public static class Builder implements TeacherIdStep, UsesHourlyWageStep, LanguageLevelStep, NameStep, BuildStep {
        private Integer id;
        private Integer customerId;
        private Integer teacherId;
        private Boolean usesHourlyWage;
        private String languageLevel;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        @Override
        public Builder teacherId(Integer teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        @Override
        public Builder usesHourlyWage(Boolean usesHourlyWage) {
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
        public ValidGroupDTO build() {
            return new ValidGroupDTO(id, customerId, teacherId, usesHourlyWage, languageLevel, name, createdAt, updatedAt);
        }
    }

}
