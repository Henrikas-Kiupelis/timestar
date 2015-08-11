package com.superum.api.customer;

import com.fasterxml.jackson.annotation.*;
import com.superum.db.customer.Customer;
import com.superum.helper.JodaLocalDate;
import com.superum.helper.field.FieldDef;
import com.superum.helper.field.FieldDefinition;
import com.superum.helper.field.FieldDefinitions;
import com.superum.helper.field.MappedClassWithTimestamps;
import com.superum.helper.field.core.Mandatory;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.core.Primary;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;
import org.jooq.lambda.Seq;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Contains all information about a customer in one place
 *
 * When creating an instance of FullCustomer with JSON, these fields are required:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *
 *      startDate   : date when a contract was signed                           date String, "yyyy-MM-dd"
 *      name        : name                                                      any String, max 30 chars
 *      phone       : phone number                                              any String, max 30 chars
 *      website     : website                                                   any String, max 30 chars
 *
 * These fields are optional:
 *      picture     : link to a picture of this customer, stored somewhere      any String, max 100 chars
 *      comment     : comment, made by the app client                           any String, max 500 chars
 *
 * These fields should only be specified if they are known:
 *      id          : number representation of this customer in the system      1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will assume default value (null for objects, 0/false for primitives),
 * all of which are assumed to be allowed unless stated otherwise (check FIELD_CONSTRAINTS)
 *
 * Example of JSON to send:
 * {
 *      "id": "1",
 *      "startDate": "2015-07-22",
 *      "name": "SUPERUM",
 *      "phone": "+37069900001",
 *      "website": "http://superum.eu",
 *      "picture": "http://timestar.lt/uploads/superum.jpg",
 *      "comment": "What a company"
 * }
 *
 * When returning an instance of FullCustomer with JSON, these fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      id          : number representation of this customer in the system
 *      startDate   : string containing the date when a contract was signed; yyyy-MM-dd format
 *      name        : name
 *      phone       : phone number
 *      website     : website
 *      picture     : link to a picture of this customer, stored somewhere
 *      comment     : comment, made by the app client
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
public final class FullCustomer extends MappedClassWithTimestamps<FullCustomer, Integer> {

	@JsonProperty(ID_FIELD)
	public int getId() {
		return id;
	}
    /**
     * @return true if id field is set; false otherwise
     */
    public boolean hasId() {
        return primaryKey().isSet();
    }
    /**
     * @return true if only id field is set; false otherwise
     */
    public boolean hasOnlyId() {
        return hasId() && nonPrimaryFields().noneMatch(MappedField::isSet);
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullCustomer, with its id replaced by the provided one
     */
    public FullCustomer withId(int id) {
        return valueOf(id, startDate, name, phone, website, picture, comment, getCreatedAt(), getUpdatedAt());
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullCustomer, with its id no longer set
     */
    public FullCustomer withoutId() {
        return valueOf(0, startDate, name, phone, website, picture, comment, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(START_DATE_FIELD)
	public String getStartDateString() {
		return startDate.toString();
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

    @Override
    public MappedField<Integer> primaryKey() {
        return primaryField();
    }

    @Override
    public Seq<MappedField<?>> createFields() {
        throw new UnsupportedOperationException("FullCustomer should be created by using Customer, not directly");
    }

    @Override
    public Seq<MappedField<?>> updateFields() {
        return allNonPrimarySetFields();
    }


    @Override
    public Seq<MappedField<?>> conditionFields() {
        return allSetFields();
    }

    /**
     * @return true if at least one customer field, other than id is set; false otherwise
     */
    public boolean canUpdateCustomer() {
        return customerFields()
                .filter(MappedField::isNotPrimary)
                .anyMatch(MappedField::isSet);
    }

    /**
     * <pre>
     * Takes every Customer field that is set in this FullCustomer and checks, if they are equal to the appropriate
     * fields of given FullCustomer
     *
     * Intended to be used during updating, to avoid making a DB query if the fields already have appropriate values
     * </pre>
     * @return true if all the set Customer fields of this FullCustomer are equal to the given FullCustomer's; false otherwise
     */
    @JsonIgnore
    public boolean hasEqualSetCustomerFields(FullCustomer other) {
        return compare(other, FullCustomer::customerFields);
    }

    /**
     * @return Customer value of this FullCustomer
     */
    @JsonIgnore
    public Customer toCustomer() {
        return new Customer(id, startDate, name, phone, website, picture, comment, getCreatedAt(), getUpdatedAt());
    }

	// OBJECT OVERRIDES

    @Override
	public String toString() {
		return "FullCustomer{" + super.toString() + "}";
	}

	// CONSTRUCTORS

    @JsonCreator
    public static FullCustomer valueOf(@JsonProperty(ID_FIELD) int id,
                                       @JsonProperty(START_DATE_FIELD) String startDate,
                                       @JsonProperty(NAME_FIELD) String name,
                                       @JsonProperty(PHONE_FIELD) String phone,
                                       @JsonProperty(WEBSITE_FIELD) String website,
                                       @JsonProperty(PICTURE_FIELD) String picture,
                                       @JsonProperty(COMMENT_FIELD) String comment) {
        LocalDate date = startDate == null ? null : LocalDate.parse(startDate);
        return valueOf(id, date, name, phone, website, picture, comment, null, null);
    }

    /**
     * Main entry point for creating FullCustomer; this method should be the only one that invokes the constructor
     */
    public static FullCustomer valueOf(int id, LocalDate startDate, String name, String phone, String website,
                                       String picture, String comment, Instant createdAt, Instant updatedAt) {
        // when id == 0, it simply was not set, so the state is valid, while id is not
        validate(id).equal(0).or().moreThan(0).ifInvalid(() -> new InvalidCustomerException("Customer id can't be negative: " + id));
        validate(name).isNull().or().notEmpty().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer name must not exceed " + NAME_SIZE_LIMIT + " chars or be empty: " + name));
        validate(phone).isNull().or().notEmpty().fits(PHONE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer phone must not exceed " + PHONE_SIZE_LIMIT + " chars or be empty: " + phone));
        validate(website).isNull().or().notEmpty().fits(WEBSITE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer website must not exceed " + WEBSITE_SIZE_LIMIT + " chars or be empty: " + website));
        validate(picture).isNull().or().fits(PICTURE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer picture must not exceed " + PICTURE_SIZE_LIMIT + " chars: " + picture));
        validate(comment).isNull().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer comment must not exceed " + COMMENT_SIZE_LIMIT + " chars: " + comment));

        return new FullCustomer(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }

    public static FullCustomer valueOf(Customer customer) {
        return valueOf(customer.getId(), customer.getStartDate(), customer.getName(), customer.getPhone(), customer.getWebsite(),
                customer.getPicture(), customer.getComment(), customer.getCreatedAt(), customer.getUpdatedAt());
    }

    public static FullCustomer valueOf(Record record) {
        if (record == null)
            return null;

        java.sql.Date sqlDate = record.getValue(CUSTOMER.START_DATE);
        LocalDate localDate;
        try {
            localDate = JodaLocalDate.from(sqlDate).toOrgJodaTimeLocalDate();
        } catch (ParseException e) {
            throw new IllegalStateException("Error occurred when parsing date while reading Customer from database: " + sqlDate, e);
        }

        return stepBuilder()
                .startDate(localDate)
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

    /**
     * Intended for updating
     * @return a new builder which can be used to make any kind of FullCustomer
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Intended for creating
     * @return a new builder which creates a FullCustomer that has all its mandatory fields set
     */
    public static StartDateStep stepBuilder() {
        return new Builder();
    }

    private FullCustomer(int id, LocalDate startDate, String name, String phone, String website, String picture, String comment, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;

        registerFields(FIELD_DEFINITIONS);
    }

    // PROTECTED

    @Override
    public FullCustomer thisObject() {
        return this;
    }

	// PRIVATE

    private final int id;
    private final LocalDate startDate;
    private final String name;
    private final String phone;
    private final String website;
    private final String picture;
    private final String comment;

    /**
     * @return a stream of all Customer fields (currently synonymous with allFields)
     */
    @JsonIgnore
    private Seq<MappedField<?>> customerFields() {
        return allFields();
    }

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int WEBSITE_SIZE_LIMIT = 30;
    private static final int PICTURE_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String START_DATE_FIELD = "startDate";
    private static final String NAME_FIELD = "name";
    private static final String PHONE_FIELD = "phone";
    private static final String WEBSITE_FIELD = "website";
    private static final String PICTURE_FIELD = "picture";
    private static final String COMMENT_FIELD = "comment";

    // FIELD DEFINITIONS

    private static final List<FieldDef<FullCustomer, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDefinition.ofInt(ID_FIELD, CUSTOMER.ID, Mandatory.NO, Primary.YES, FullCustomer::getId),
            FieldDefinition.ofDate(START_DATE_FIELD, CUSTOMER.START_DATE, Mandatory.YES, Primary.NO, FullCustomer::getStartDate),
            FieldDefinition.of(NAME_FIELD, CUSTOMER.NAME, Mandatory.YES, Primary.NO, FullCustomer::getName),
            FieldDefinition.of(PHONE_FIELD, CUSTOMER.PHONE, Mandatory.YES, Primary.NO, FullCustomer::getPhone),
            FieldDefinition.of(WEBSITE_FIELD, CUSTOMER.WEBSITE, Mandatory.YES, Primary.NO, FullCustomer::getWebsite),
            FieldDefinition.of(PICTURE_FIELD, CUSTOMER.PICTURE, Mandatory.NO, Primary.NO, FullCustomer::getPicture),
            FieldDefinition.of(COMMENT_FIELD, CUSTOMER.COMMENT, Mandatory.NO, Primary.NO, FullCustomer::getComment)
    );
    private static final FieldDefinitions<FullCustomer> FIELD_DEFINITIONS = new FieldDefinitions<>(FIELD_DEFINITION_LIST);

    // GENERATED

    public interface StartDateStep {
        NameStep startDate(LocalDate startDate);
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
        BuildStep id(int id);
        BuildStep picture(String pictureName);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        FullCustomer build();
    }

    public static class Builder implements StartDateStep, NameStep, PhoneStep, WebsiteStep, BuildStep {
        private int id;
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
        public Builder id(int id) {
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
        public FullCustomer build() {
            return valueOf(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
        }
    }

}
