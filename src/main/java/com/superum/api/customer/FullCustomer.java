package com.superum.api.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.google.common.collect.ImmutableList;
import com.superum.db.customer.Customer;
import com.superum.fields.Field;
import com.superum.fields.Mandatory;
import com.superum.fields.NamedField;
import com.superum.fields.SimpleField;
import com.superum.fields.primitives.IntField;
import com.superum.helper.AbstractFullClassWithTimestamps;
import com.superum.helper.JodaLocalDate;
import com.superum.helper.SetFieldComparator;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.jooq.Record;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Stream;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.utils.ValidationUtils.fitsOrNull;
import static com.superum.utils.ValidationUtils.fitsOrNullNotEmpty;

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
 *      "id":"1",
 *      "startDate":"2015-07-22",
 *      "name":"SUPERUM",
 *      "phone":"+37069900001",
 *      "website":"http://superum.eu",
 *      "picture":"http://timestar.lt/uploads/superum.jpg",
 *      "comment":"What a company"
 * }
 *
 * When returning an instance of FullCustomer with JSON, these fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      id          : number representation of this customer in the system
 *      startDate   : array containing the date when a contract was signed; [year, month, day], all integers
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
 *      "startDate": [2015, 7, 22],
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
public class FullCustomer extends AbstractFullClassWithTimestamps {

    // PUBLIC API

	@JsonProperty(ID_FIELD)
	public int getId() {
		return id.intValue();
	}
	@JsonIgnore
	public boolean hasId() {
		return id.isSet();
	}
    /**
     * @return true if only id field is set; false otherwise
     */
    @JsonIgnore
    public boolean hasOnlyId() {
        return hasId() && allFields().filter(field -> field != id).noneMatch(Field::isSet);
    }
    /**
     * <pre>
     * Intended to be used when the id field is not set yet, and is retrieved from the database
     * </pre>
     * @return a copy of this FullCustomer, with its id replaced by the provided one
     */
    @JsonIgnore
    public FullCustomer withId(int id) {
        return valueOf(id, getStartDate(), getName(), getPhone(), getWebsite(), getPicture(), getComment(), getCreatedAt(), getUpdatedAt());
    }
    /**
     * <pre>
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * </pre>
     * @return a copy of this FullCustomer, with its id no longer set
     */
    @JsonIgnore
    public FullCustomer withoutId() {
        return valueOf(0, getStartDate(), getName(), getPhone(), getWebsite(), getPicture(), getComment(), getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(START_DATE_FIELD)
    @JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate getStartDate() {
		return startDate.getValue();
	}

    @JsonIgnore
    public boolean hasStartDate() {
        return startDate.isSet();
    }

    @JsonIgnore
    public java.sql.Date getStartDateSQL() {
        try {
            return JodaLocalDate.from(getStartDate()).toJavaSqlDate();
        } catch (ParseException e) {
            throw new IllegalStateException("Error occurred when parsing date while writing Customer to database: " + startDate, e);
        }
    }

	@JsonProperty(NAME_FIELD)
	public String getName() {
		return name.getValue();
	}

    @JsonIgnore
    public boolean hasName() {
        return name.isSet();
    }

    @JsonProperty(PHONE_FIELD)
	public String getPhone() {
		return phone.getValue();
	}

    @JsonIgnore
    public boolean hasPhone() {
        return phone.isSet();
    }

    @JsonProperty(WEBSITE_FIELD)
	public String getWebsite() {
		return website.getValue();
	}

    @JsonIgnore
    public boolean hasWebsite() {
        return website.isSet();
    }
	
	@JsonProperty(PICTURE_FIELD)
	public String getPicture() {
		return picture.getValue();
	}
    @JsonIgnore
    public boolean hasPictureName() {
        return picture.isSet();
    }
	
	@JsonProperty(COMMENT_FIELD)
	public String getComment() {
		return comment.getValue();
	}

    @JsonIgnore
    public boolean hasComment() {
        return comment.isSet();
    }

    /**
     * @return true if at least one field, other than id is set; false otherwise
     */
    @JsonIgnore
    public boolean canUpdateCustomer() {
        return customerFields()
                .filter(field -> field != id)
                .anyMatch(Field::isSet);
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
        return setFieldComparator.compare(other, FullCustomer::customerFields);
    }

    /**
     * @return Customer value of this FullCustomer
     */
    @JsonIgnore
    public Customer toCustomer() {
        return new Customer(getId(), getStartDate(), getName(), getPhone(), getWebsite(), getPicture(), getComment(), getCreatedAt(), getUpdatedAt());
    }

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "FullCustomer{" + super.toString() + "}";
	}

	// CONSTRUCTORS

    @JsonCreator
    public static FullCustomer valueOf(@JsonProperty(ID_FIELD) int id,
                                       @JsonProperty(START_DATE_FIELD) @JsonDeserialize(using=LocalDateDeserializer.class) LocalDate startDate,
                                       @JsonProperty(NAME_FIELD) String name,
                                       @JsonProperty(PHONE_FIELD) String phone,
                                       @JsonProperty(WEBSITE_FIELD) String website,
                                       @JsonProperty(PICTURE_FIELD) String picture,
                                       @JsonProperty(COMMENT_FIELD) String comment) {
        return valueOf(id, startDate, name, phone, website, picture, comment, null, null);
    }

    public static FullCustomer valueOf(int id, LocalDate startDate, String name, String phone, String website,
                                       String picture, String comment, Instant createdAt, Instant updatedAt) {
        // Equivalent to (id != 0 && id <= 0); when id == 0, it simply was not set, so the state is valid, while id is not
        if (id < 0)
            throw new InvalidCustomerException("Customer id must be positive.");

        IntField idField = new IntField(ID_FIELD, id, Mandatory.NO);

        if (!fitsOrNullNotEmpty(NAME_SIZE_LIMIT, name))
            throw new InvalidCustomerException("Customer name must not exceed " + NAME_SIZE_LIMIT + " chars");

        SimpleField<String> nameField = SimpleField.valueOf(NAME_FIELD, name, Mandatory.YES);

        if (!fitsOrNullNotEmpty(PHONE_SIZE_LIMIT, phone))
            throw new InvalidCustomerException("Customer phone must not exceed " + PHONE_SIZE_LIMIT + " chars");

        SimpleField<String> phoneField = SimpleField.valueOf(PHONE_FIELD, phone, Mandatory.YES);

        if (!fitsOrNullNotEmpty(WEBSITE_SIZE_LIMIT, website))
            throw new InvalidCustomerException("Customer website must not exceed " + WEBSITE_SIZE_LIMIT + " chars");

        SimpleField<String> websiteField = SimpleField.valueOf(WEBSITE_FIELD, website, Mandatory.YES);

        if (!fitsOrNull(PICTURE_SIZE_LIMIT, picture))
            throw new InvalidCustomerException("Customer picture name must not exceed " + PICTURE_SIZE_LIMIT + " chars");

        SimpleField<String> pictureField = SimpleField.valueOf(PICTURE_FIELD, picture, Mandatory.NO);

        if (!fitsOrNull(COMMENT_SIZE_LIMIT, comment))
            throw new InvalidCustomerException("Customer comment must not exceed " + COMMENT_SIZE_LIMIT + " chars");

        SimpleField<String> commentField = SimpleField.valueOf(COMMENT_FIELD, comment, Mandatory.NO);

        SimpleField<LocalDate> startDateField = SimpleField.valueOf(START_DATE_FIELD, startDate, Mandatory.YES);

        List<NamedField> allFields = ImmutableList.of(idField, startDateField, nameField, phoneField, websiteField, pictureField, commentField);
        return new FullCustomer(allFields, createdAt, updatedAt, idField, startDateField, nameField, phoneField, websiteField, pictureField, commentField);
    }

    public static FullCustomer valueOf(Customer customer) {
        return valueOf(customer.getId(), customer.getStartDate(), customer.getName(), customer.getPhone(), customer.getWebsite(),
                customer.getPicture(), customer.getComment(), customer.getCreatedAt(), customer.getUpdatedAt());
    }

    public static FullCustomer valueOf(Record record) {
        if (record == null)
            return null;

        return stepBuilder()
                .startDate(from(record.getValue(CUSTOMER.START_DATE)))
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
     * <pre>
     * Intended for updating
     * </pre>
     * @return a new builder which can be used to make any kind of FullCustomer
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * <pre>
     * Intended for creating
     * </pre>
     * @return a new builder which creates a FullCustomer that has all its mandatory fields set
     */
    public static StartDateStep stepBuilder() {
        return new Builder();
    }

    private FullCustomer(List<NamedField> allFields, Instant createdAt, Instant updatedAt, IntField id,
                         SimpleField<LocalDate> startDate, SimpleField<String> name, SimpleField<String> phone,
                         SimpleField<String> website, SimpleField<String> picture, SimpleField<String> comment) {
        super(allFields, createdAt, updatedAt);

        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;

        this.setFieldComparator = new SetFieldComparator<>(this);
    }

	// PRIVATE
	
	private final IntField id;
	private final NamedField<LocalDate> startDate;
	private final NamedField<String> name;
	private final NamedField<String> phone;
	private final NamedField<String> website;
	private final NamedField<String> picture;
	private final NamedField<String> comment;

    private final SetFieldComparator<FullCustomer> setFieldComparator;

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all Customer fields
     */
    @JsonIgnore
    private Stream<NamedField> customerFields() {
        return allFields();
    }

    private static LocalDate from(java.sql.Date sqlDate) {
        try {
            return JodaLocalDate.from(sqlDate).toOrgJodaTimeLocalDate();
        } catch (ParseException e) {
            throw new IllegalStateException("Error occurred when parsing date while reading Customer from database: " + sqlDate, e);
        }
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
