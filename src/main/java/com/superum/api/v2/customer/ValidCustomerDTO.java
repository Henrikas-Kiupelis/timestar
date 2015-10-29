package com.superum.api.v2.customer;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import com.superum.api.core.DTOWithTimestamps;
import eu.goodlike.libraries.joda.time.Time;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import java.util.Objects;

import static timestar_v2.Tables.CUSTOMER;

/**
 * <pre>
 * Data Transport Object for customers
 *
 * This object is used to de-serialize and serialize JSON that is coming in and out of the back end;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When creating an instance of ValidCustomerDTO with JSON, these fields are required:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      startDate   : date when a contract was signed                           date String, "yyyy-MM-dd"
 *      name        : name                                                      any String, max 180 chars
 *      phone       : phone number                                              any String, max 180 chars
 *      website     : website                                                   any String, max 180 chars
 * These fields are optional:
 *      picture     : link to a picture of this customer, stored somewhere      any String, max 180 chars
 *      comment     : comment, made by the app client                           any String, max 500 chars
 * These fields should only be specified if they are known:
 *      id          : number representation of this customer in the system      1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "id": 1,
 *      "startDate": "2015-07-22",
 *      "name": "SUPERUM",
 *      "phone": "+37069900001",
 *      "website": "http://superum.eu",
 *      "picture": "http://timestar.lt/uploads/superum.jpg",
 *      "comment": "What a company"
 * }
 *
 * When returning an instance of ValidCustomerDTO with JSON, these additional fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "startDate": "2015-07-22",
 *      "name": "SUPERUM",
 *      "phone": "+37069900001",
 *      "website": "http://superum.eu",
 *      "picture":"http://timestar.lt/uploads/superum.jpg",
 *      "comment": "What a company",
 *      "createdAt": 1437512400000,
 *      "updatedAt": 1438689946954
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidCustomerDTO extends DTOWithTimestamps {

	@JsonProperty(ID_FIELD)
	public Integer getId() {
		return id;
	}
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this ValidCustomerDTO, with its id no longer set
     */
    @JsonIgnore
    public ValidCustomerDTO withoutId() {
        return new ValidCustomerDTO(null, startDate, name, phone, website, picture, comment, getCreatedAt(), getUpdatedAt());
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this ValidCustomerDTO, with its id replaced by the provided one
     */
    @JsonIgnore
    public ValidCustomerDTO withId(Integer id) {
        return new ValidCustomerDTO(id, startDate, name, phone, website, picture, comment, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(START_DATE_FIELD)
	public String getStartDateString() {
		return startDate == null ? null : startDate.toString();
	}
    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

	@JsonProperty(NAME_FIELD)
	public String getName() {
		return name;
	}

    @JsonProperty(PHONE_FIELD)
	public String getPhone() {
		return phone;
	}

    @JsonProperty(WEBSITE_FIELD)
	public String getWebsite() {
		return website;
	}
	
	@JsonProperty(PICTURE_FIELD)
	public String getPicture() {
		return picture;
	}
	
	@JsonProperty(COMMENT_FIELD)
	public String getComment() {
		return comment;
	}

	// CONSTRUCTORS

    @JsonCreator
    public static ValidCustomerDTO jsonInstance(@JsonProperty(ID_FIELD) Integer id,
                                                @JsonProperty(START_DATE_FIELD) String startDate,
                                                @JsonProperty(NAME_FIELD) String name,
                                                @JsonProperty(PHONE_FIELD) String phone,
                                                @JsonProperty(WEBSITE_FIELD) String website,
                                                @JsonProperty(PICTURE_FIELD) String picture,
                                                @JsonProperty(COMMENT_FIELD) String comment) {
        LocalDate date = startDate == null ? null : LocalDate.parse(startDate);
        return new ValidCustomerDTO(id, date, name, phone, website, picture, comment, null, null);
    }

    public static ValidCustomerDTO valueOf(Record record) {
        if (record == null)
            return null;

        return stepBuilder()
                .startDate(record.getValue(CUSTOMER.START_DATE))
                .name(record.getValue(CUSTOMER.NAME))
                .phone(record.getValue(CUSTOMER.PHONE))
                .website(record.getValue(CUSTOMER.WEBSITE))
                .id(record.getValue(CUSTOMER.ID))
                .picture(record.getValue(CUSTOMER.PICTURE))
                .comment(record.getValue(CUSTOMER.COMMENT))
                .createdAt(record.getValue(CUSTOMER.CREATED_AT))
                .updatedAt(record.getValue(CUSTOMER.UPDATED_AT))
                .build();
    }

    public ValidCustomerDTO(Integer id, LocalDate startDate, String name, String phone, String website, String picture,
                            String comment, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;
    }

    /**
     * Intended for updating
     * @return a new builder which can be used to make any kind of ValidCustomerDTO
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Intended for creating
     * @return a new builder which creates a ValidCustomerDTO that has all its mandatory fields set
     */
    public static StartDateStep stepBuilder() {
        return new Builder();
    }

	// PRIVATE

    private final Integer id;
    private final LocalDate startDate;
    private final String name;
    private final String phone;
    private final String website;
    private final String picture;
    private final String comment;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String START_DATE_FIELD = "startDate";
    private static final String NAME_FIELD = "name";
    private static final String PHONE_FIELD = "phone";
    private static final String WEBSITE_FIELD = "website";
    private static final String PICTURE_FIELD = "picture";
    private static final String COMMENT_FIELD = "comment";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidCustomer")
                .add(ID_FIELD, id)
                .add(START_DATE_FIELD, startDate)
                .add(NAME_FIELD, name)
                .add(PHONE_FIELD, phone)
                .add(WEBSITE_FIELD, website)
                .add(PICTURE_FIELD, picture)
                .add(COMMENT_FIELD, comment)
                .add(CREATED_AT_FIELD, getCreatedAt())
                .add(UPDATED_AT_FIELD, getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidCustomerDTO)) return false;
        ValidCustomerDTO that = (ValidCustomerDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(website, that.website) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, name, phone, website, picture, comment);
    }

    // GENERATED

    public interface StartDateStep {
        NameStep startDate(LocalDate startDate);
        NameStep startDate(java.sql.Date startDate);
        NameStep startDate(String startDate);
    }

    public interface NameStep {
        PhoneStep name(String name);
    }

    public interface PhoneStep {
        WebsiteStep phone(String phone);
    }

    public interface WebsiteStep {
        BuildStep website(String website);
    }

    public interface BuildStep {
        BuildStep id(Integer id);
        BuildStep picture(String pictureName);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        ValidCustomerDTO build();
    }

    public static class Builder implements StartDateStep, NameStep, PhoneStep, WebsiteStep, BuildStep {
        private Integer id;
        private LocalDate startDate;
        private String name;
        private String phone;
        private String website;
        private String picture;
        private String comment;

        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder startDate(java.sql.Date startDate) {
            this.startDate = startDate == null ? null : Time.convert(startDate).toJodaLocalDate();
            return this;
        }

        @Override
        public Builder startDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public Builder website(String website) {
            this.website = website;
            return this;
        }

        @Override
        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder picture(String picture) {
            this.picture = picture;
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
        public ValidCustomerDTO build() {
            return new ValidCustomerDTO(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
        }
    }

}
