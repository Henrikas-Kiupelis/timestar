package com.superum.api.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.dto.DTOWithTimestamps;
import com.superum.helper.Equals;
import com.superum.helper.time.JodaTimeZoneHandler;
import org.joda.time.Instant;
import org.jooq.Record;

import java.util.Arrays;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.GROUP_OF_STUDENTS;

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
 *      "customerId": null,
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
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ValidGroupDTO extends DTOWithTimestamps {

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }
    public ValidGroupDTO withoutId() {
        return new ValidGroupDTO(null, customerId, teacherId, usesHourlyWage, languageLevel, name, getCreatedAt(), getUpdatedAt());
    }
    public ValidGroupDTO withId(Integer id) {
        return new ValidGroupDTO(id, customerId, teacherId, usesHourlyWage, languageLevel, name, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty("customerId")
    public Integer getCustomerId() {
        return customerId;
    }

    @JsonProperty("teacherId")
    public Integer getTeacherId() {
        return teacherId;
    }

    @JsonProperty("usesHourlyWage")
    public Boolean getUsesHourlyWage() {
        return usesHourlyWage;
    }

    @JsonProperty("languageLevel")
    public String getLanguageLevel() {
        return languageLevel;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidGroupDTO jsonInstance(@JsonProperty("id") Integer id,
                                             @JsonProperty("customerId") Integer customerId,
                                             @JsonProperty("teacherId") Integer teacherId,
                                             @JsonProperty("usesHourlyWage") Boolean usesHourlyWage,
                                             @JsonProperty("languageLevel") String languageLevel,
                                             @JsonProperty("name") String name) {
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

        Instant createdAt = JodaTimeZoneHandler.getDefault()
                .from(record.getValue(GROUP_OF_STUDENTS.CREATED_AT))
                .toOrgJodaTimeInstant();

        Instant updatedAt = JodaTimeZoneHandler.getDefault()
                .from(record.getValue(GROUP_OF_STUDENTS.UPDATED_AT))
                .toOrgJodaTimeInstant();

        return new ValidGroupDTO(
                record.getValue(GROUP_OF_STUDENTS.ID),
                record.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID),
                record.getValue(GROUP_OF_STUDENTS.TEACHER_ID),
                record.getValue(GROUP_OF_STUDENTS.USE_HOURLY_WAGE),
                record.getValue(GROUP_OF_STUDENTS.LANGUAGE_LEVEL),
                record.getValue(GROUP_OF_STUDENTS.NAME),
                createdAt,
                updatedAt
        );
    }

    // PRIVATE

    private final Integer id;
    private final Integer customerId;
    private final Integer teacherId;
    private final Boolean usesHourlyWage;
    private final String languageLevel;
    private final String name;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Group")
                .add("Group id", id)
                .add("Customer id", customerId)
                .add("Teacher id", teacherId)
                .add("UsesHourlyWage?", usesHourlyWage)
                .add("Language level", languageLevel)
                .add("Name", name)
                .add("Created at", getCreatedAt())
                .add("Updated at", getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidGroupDTO && EQUALS.equals(this, (ValidGroupDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, teacherId, usesHourlyWage, languageLevel, name);
    }

    private static final Equals<ValidGroupDTO> EQUALS = new Equals<>(Arrays.asList(ValidGroupDTO::getId,
            ValidGroupDTO::getTeacherId, ValidGroupDTO::getCustomerId, ValidGroupDTO::getUsesHourlyWage,
            ValidGroupDTO::getLanguageLevel, ValidGroupDTO::getName));

}
