package com.superum.api.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.core.DTOWithTimestamps;
import com.superum.helper.Equals;
import com.superum.helper.time.JodaTimeZoneHandler;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import java.util.Arrays;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.STUDENT;

/**
 * <pre>
 * Data Transport Object for students
 *
 * This object is used to de-serialize and serialize JSON that is coming in and out of the back end;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When parsing an instance of ValidStudentDTO with JSON, these fields are considered mandatory for create() operation:
 *      FIELD_NAME     : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      email          : email of this student                                     any String, max 60 chars
 *      name           : name of this student                                      any String, max 60 chars
 * You can choose from one set of these fields:
 *      customerId     : id of the customer this student belongs to;               1 <= customerId
 *                       if this is null, the student has no customer
 * or:
 *      startDate      : date when a contract was signed                           date String, "yyyy-MM-dd"
 * These fields should only be specified if they are known:
 *      id             : number representation of this student in the system       1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "id": 1,
 *      "startDate": "2015-08-25",
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "name": "Henrikas Kiūpelis"
 * }
 * or:
 * {
 *      "id": 1,
 *      "customerId": 1,
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "name": "Henrikas Kiūpelis"
 * }
 *
 * When returning an instance of ValidStudentDTO with JSON, these additional fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      code        : 6 digit code, generated during the creation process
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "code": 184267,
 *      "customerId": null,
 *      "startDate": "2015-08-25",
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "name": "Henrikas Kiūpelis"
 *      "createdAt": 1440450000000,
 *      "updatedAt": 1440499069739
 * }
 * or:
 * {
 *      "id": 1,
 *      "code": 184267,
 *      "customerId": 1,
 *      "startDate": null,
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "name": "Henrikas Kiūpelis"
 *      "createdAt": 1440450000000,
 *      "updatedAt": 1440499069739
 * }
 * </pre>
 */
public class ValidStudentDTO extends DTOWithTimestamps {

    @JsonProperty(ID_FIELD)
    public Integer getId() {
        return id;
    }
    @JsonIgnore
    public ValidStudentDTO withId(Integer id) {
        return new ValidStudentDTO(id, code, customerId, startDate, email, name, getCreatedAt(), getUpdatedAt());
    }
    @JsonIgnore
    public ValidStudentDTO withoutId() {
        return new ValidStudentDTO(null, code, customerId, startDate, email, name, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(CODE_FIELD)
    public Integer getCode() {
        return code;
    }
    @JsonIgnore
    public ValidStudentDTO withCode(Integer code) {
        return new ValidStudentDTO(id, code, customerId, startDate, email, name, getCreatedAt(), getUpdatedAt());
    }
    @JsonIgnore
    public ValidStudentDTO withoutCode() {
        return new ValidStudentDTO(id, null, customerId, startDate, email, name, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(CUSTOMER_ID_FIELD)
    public Integer getCustomerId() {
        return customerId;
    }

    @JsonProperty(START_DATE_FIELD)
    public String getStartDateString() {
        return startDate.toString();
    }
    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonProperty(EMAIL_FIELD)
    public String getEmail() {
        return email;
    }

    @JsonProperty(NAME_FIELD)
    public String getName() {
        return name;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidStudentDTO jsonInstance(@JsonProperty(ID_FIELD) Integer id,
                                               @JsonProperty(CUSTOMER_ID_FIELD) Integer customerId,
                                               @JsonProperty(START_DATE_FIELD) String startDate,
                                               @JsonProperty(EMAIL_FIELD) String email,
                                               @JsonProperty(NAME_FIELD) String name) {
        return new ValidStudentDTO(id, null, customerId,
                startDate == null ? null : LocalDate.parse(startDate),
                email, name, null, null);
    }

    public ValidStudentDTO(Integer id, Integer code, Integer customerId, LocalDate startDate, String email, String name,
                           Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
        this.code = code;
        this.customerId = customerId;
        this.startDate = startDate;
        this.email = email;
        this.name = name;
    }

    public static ValidStudentDTO valueOf(Record studentRecord) {
        if (studentRecord == null)
            return null;

        return builder()
                .id(studentRecord.getValue(STUDENT.ID))
                .code(studentRecord.getValue(STUDENT.CODE))
                .customerId(studentRecord.getValue(STUDENT.CUSTOMER_ID))
                .startDate(studentRecord.getValue(STUDENT.START_DATE))
                .email(studentRecord.getValue(STUDENT.EMAIL))
                .name(studentRecord.getValue(STUDENT.NAME))
                .createdAt(studentRecord.getValue(STUDENT.CREATED_AT))
                .updatedAt(studentRecord.getValue(STUDENT.UPDATED_AT))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static CustomerIdStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

    private final Integer id;
    private final Integer code;
    private final Integer customerId;
    private final LocalDate startDate;
    private final String email;
    private final String name;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String CODE_FIELD = "code";
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String START_DATE_FIELD = "startDate";
    private static final String EMAIL_FIELD = "email";
    private static final String NAME_FIELD = "name";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidStudentDTO")
                .add(ID_FIELD, id)
                .add(CODE_FIELD, code)
                .add(CUSTOMER_ID_FIELD, customerId)
                .add(START_DATE_FIELD, startDate)
                .add(EMAIL_FIELD, email)
                .add(NAME_FIELD, name)
                .add(CREATED_AT_FIELD, getCreatedAt())
                .add(UPDATED_AT_FIELD, getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidStudentDTO && EQUALS.equals(this, (ValidStudentDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, customerId, startDate, email, name);
    }

    private static final Equals<ValidStudentDTO> EQUALS = new Equals<>(Arrays.asList(ValidStudentDTO::getId,
            ValidStudentDTO::getCode, ValidStudentDTO::getCustomerId, ValidStudentDTO::getStartDate,
            ValidStudentDTO::getEmail, ValidStudentDTO::getName));

    // GENERATED

    public interface CustomerIdStep {
        EmailStep customerId(Integer customerId);
        StartDateStep noCustomer();
    }

    public interface StartDateStep {
        EmailStep startDate(LocalDate startDate);
        EmailStep startDate(java.sql.Date startDate);
        EmailStep startDate(String startDate);
    }

    public interface EmailStep {
        NameStep email(String email);
    }

    public interface NameStep {
        BuildStep name(String name);
    }

    public interface BuildStep {
        BuildStep id(Integer id);
        BuildStep code(Integer code);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        ValidStudentDTO build();
    }


    public static class Builder implements CustomerIdStep, StartDateStep, EmailStep, NameStep, BuildStep {
        private Integer id;
        private Integer code;
        private Integer customerId;
        private LocalDate startDate;
        private String email;
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
        public Builder code(Integer code) {
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
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder startDate(java.sql.Date startDate) {
            this.startDate = JodaTimeZoneHandler.getDefault().from(startDate).toOrgJodaTimeLocalDate();
            return this;
        }

        @Override
        public Builder startDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        @Override
        public Builder email(String email) {
            this.email = email;
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
        public ValidStudentDTO build() {
            return new ValidStudentDTO(id, code, customerId, startDate, email, name, createdAt, updatedAt);
        }
    }

}
