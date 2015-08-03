package com.superum.api.customer;

import com.fasterxml.jackson.annotation.*;
import com.superum.db.customer.Customer;
import com.superum.fields.*;
import com.superum.fields.primitives.IntField;
import com.superum.helper.SetFieldComparator;
import org.jooq.Record;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.utils.ValidationUtils.fitsOrNull;

/**
 * <pre>
 * Contains all information about the customer in one place
 *
 * When creating an instance of FullCustomer with JSON, these fields are required:
 *     FIELD_NAME    : FIELD_DESCRIPTION                                          FIELD_CONSTRAINTS
 *
 *     startDate     : date when a contract was signed;                           any java.sql.Date
 *     name          : name                                                       any String, max 30 chars
 *     phone         : phone number                                               any String, max 30 chars
 *     website       : website                                                    any String, max 30 chars
 *
 * These fields are optional:
 *     picture       : name of the picture of the customer, stored somewhere      any String, max 100 chars
 *     comment       : comment, made by the app client                            any String, max 500 chars
 *
 * These fields should only be specified if they are known:
 *     id            : number representation of this customer in the system       1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will assume default value (null for objects, 0/false for primitives),
 * all of which are assumed to be allowed unless stated otherwise (check FIELD_CONSTRAINTS)
 *
 * Example of JSON:
 * {
 *     "id":"1",
 *     "startDate":"2015-07-22",
 *     "name":"SUPERUM",
 *     "phone":"+37069900001",
 *     "website":"http://superum.eu",
 *     "picture":"superum.jpg",
 *     "comment":"What a company"
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullCustomer {

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
        return new FullCustomer(id, getStartDate(), getName(), getPhone(), getWebsite(), getPictureName(), getComment(), getCreatedAt(), getUpdatedAt());
    }
    /**
     * <pre>
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * </pre>
     * @return a copy of this FullCustomer, with its id no longer set
     */
    @JsonIgnore
    public FullCustomer withoutId() {
        return new FullCustomer(0, getStartDate(), getName(), getPhone(), getWebsite(), getPictureName(), getComment(), getCreatedAt(), getUpdatedAt());
    }

	@JsonProperty(START_DATE_FIELD)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
	public Date getStartDate() {
		return startDate.getValue();
	}
    @JsonIgnore
    public boolean hasStartDate() {
        return startDate.isSet();
    }
    @JsonIgnore
    public java.sql.Date getStartDateSQL() {
        return new java.sql.Date(getStartDate().getTime());
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
	public String getPictureName() {
		return pictureName.getValue();
	}
    @JsonIgnore
    public boolean hasPictureName() {
        return pictureName.isSet();
    }
	
	@JsonProperty(COMMENT_FIELD)
	public String getComment() {
		return comment.getValue();
	}
    @JsonIgnore
    public boolean hasComment() {
        return comment.isSet();
    }

    @JsonProperty(CREATED_AT_FIELD)
    public Date getCreatedAt() {
        return createdAt;
    }
    @JsonProperty(UPDATED_AT_FIELD)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return true if at least one field is set; false otherwise
     */
    @JsonIgnore
    public boolean hasAnyFieldsSet() {
        return allFields().anyMatch(Field::isSet);
    }

    /**
     * @return true if all the mandatory fields are set; false otherwise
     */
    @JsonIgnore
    public boolean canBeInserted() {
        return mandatoryFields().allMatch(Field::isSet);
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
    public boolean hasEqualSetCustomerFields(FullCustomer other) {
        return setFieldComparator.compare(other, FullCustomer::customerFields);
    }

    /**
     * @return a list of names of mandatory fields that are not set
     */
    @JsonIgnore
    public List<String> missingMandatoryFieldNames() {
        return mandatoryFields()
                .filter(field -> !field.isSet())
                .map(NamedField::getFieldName)
                .collect(Collectors.toList());
    }

    /**
     * @return Customer value of this FullCustomer
     */
    @JsonIgnore
    public Customer toCustomer() {
        return new Customer(getId(), getStartDate(), getName(), getPhone(), getWebsite(), getPictureName(), getComment(), getCreatedAt(), getUpdatedAt());
    }

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return string;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof FullCustomer))
			return false;

		FullCustomer other = (FullCustomer) o;

		return Objects.equals(this.allFields, other.allFields);
	}

	@Override
	public int hashCode() {
        return hash;
	}

	// CONSTRUCTORS

    @JsonCreator
    public FullCustomer(@JsonProperty(ID_FIELD) int id,
                        @JsonProperty(START_DATE_FIELD) Date startDate,
                        @JsonProperty(NAME_FIELD) String name,
                        @JsonProperty(PHONE_FIELD) String phone,
                        @JsonProperty(WEBSITE_FIELD) String website,
                        @JsonProperty(PICTURE_FIELD) String picture,
                        @JsonProperty(COMMENT_FIELD) String comment) {
        this(id, startDate, name, phone, website, picture, comment, null, null);
    }

	public FullCustomer(int id, Date startDate, String name, String phone, String website, String pictureName,
                        String comment, Date createdAt, Date updatedAt) {
        // Equivalent to (id != 0 && id <= 0); when id == 0, it simply was not set, so the state is valid, while id is not
        if (id < 0)
            throw new InvalidCustomerException("Customer id must be positive.");

        this.id = new IntField(ID_FIELD, id, Mandatory.NO);

        if (!fitsOrNull(NAME_SIZE_LIMIT, name))
            throw new InvalidCustomerException("Customer name must not exceed " + NAME_SIZE_LIMIT + " chars");

		this.name = SimpleField.valueOf(NAME_FIELD, name, Mandatory.YES);

        if (!fitsOrNull(PHONE_SIZE_LIMIT, phone))
            throw new InvalidCustomerException("Customer phone must not exceed " + PHONE_SIZE_LIMIT + " chars");

		this.phone = SimpleField.valueOf(PHONE_FIELD, phone, Mandatory.YES);

        if (!fitsOrNull(WEBSITE_SIZE_LIMIT, website))
            throw new InvalidCustomerException("Customer website must not exceed " + WEBSITE_SIZE_LIMIT + " chars");

		this.website = SimpleField.valueOf(WEBSITE_FIELD, website, Mandatory.YES);

        if (!fitsOrNull(PICTURE_SIZE_LIMIT, pictureName))
            throw new InvalidCustomerException("Customer picture name must not exceed " + PICTURE_SIZE_LIMIT + " chars");

		this.pictureName = SimpleField.valueOf(PICTURE_FIELD, pictureName, Mandatory.NO);

        if (!fitsOrNull(COMMENT_SIZE_LIMIT, comment))
            throw new InvalidCustomerException("Customer comment must not exceed " + COMMENT_SIZE_LIMIT + " chars");

		this.comment = SimpleField.valueOf(COMMENT_FIELD, comment, Mandatory.NO);

        this.startDate = SimpleField.valueOf(START_DATE_FIELD, startDate, Mandatory.YES);

        this.allFields = fieldList();
        // This class is immutable, and it will almost always be turned into a string at least once (logs); it makes sense to cache the value
        this.string = allFields()
                .map(Field::toString)
                .collect(Collectors.joining(", "));
        // Caching for hashCode(), just like toString()
        this.hash = allFields.hashCode();

        this.setFieldComparator = new SetFieldComparator<>(this);

        // The following fields are not significant, i.e. they are not to be used when comparing for equality
        // Intended for JSON conversion purposes only
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FullCustomer(Customer customer) {
        this(customer.getId(), customer.getStartDate(), customer.getName(), customer.getPhone(), customer.getWebsite(),
                customer.getPicture(), customer.getComment(), customer.getCreatedAt(), customer.getUpdatedAt());
    }

    public static FullCustomer valueOf(Customer customer) {
        return new FullCustomer(customer);
    }

    public static FullCustomer valueOf(Record record) {
        if (record == null)
            return null;

        long createdTimestamp = record.getValue(CUSTOMER.CREATED_AT);
        Date createdAt = new Date(createdTimestamp);
        long updatedTimestamp = record.getValue(CUSTOMER.UPDATED_AT);
        Date updatedAt = new Date(updatedTimestamp);

        return stepBuilder()
                .withStartDate(record.getValue(CUSTOMER.START_DATE))
                .withName(record.getValue(CUSTOMER.NAME))
                .withPhone(record.getValue(CUSTOMER.PHONE))
                .withWebsite(record.getValue(CUSTOMER.WEBSITE))
                .withId(record.getValue(CUSTOMER.ID))
                .withPictureName(record.getValue(CUSTOMER.PICTURE))
                .withComment(record.getValue(CUSTOMER.COMMENT))
                .withCreatedAt(createdAt)
                .withUpdatedAt(updatedAt)
                .build();
    }

    /**
     * <pre>
     * Intended for updating
     * </pre>
     * @return a new builder which can be used to make any kind of FullCustomer
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * <pre>
     * Intended for creating
     * </pre>
     * @return a new builder which creates a FullCustomer that has all its mandatory fields set
     */
    public static StartDateStep stepBuilder() {
        return new StepBuilder();
    }

	// PRIVATE
	
	private final IntField id;
	private final NamedField<Date> startDate;
	private final NamedField<String> name;
	private final NamedField<String> phone;
	private final NamedField<String> website;
	private final NamedField<String> pictureName;
	private final NamedField<String> comment;

    private final Date createdAt;
    private final Date updatedAt;

    private final List<NamedField> allFields;
    private final String string;
    private final int hash;

    private final SetFieldComparator<FullCustomer> setFieldComparator;

    /**
     * <pre>
     * Probably best to cache this, i.e. use in constructor
     *
     * The fields that are returned are the significant ones, i.e. the ones that should be used for equality tests
     * </pre>
     * @return a list of all fields
     */
    private List<NamedField> fieldList() {
        return Collections.unmodifiableList(Arrays.asList(id, startDate, name, phone, website, pictureName, comment));
    }

    /**
     * <pre>
     * Intended to be used by other methods to reduce the call chain
     * </pre>
     * @return a stream of all fields
     */
    private Stream<NamedField> allFields() {
        return allFields.stream();
    }

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all Customer fields
     */
    private Stream<NamedField> customerFields() {
        return allFields();
    }

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all mandatory fields
     */
    private Stream<NamedField> mandatoryFields() {
        return allFields().filter(MaybeField::isMandatory);
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

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

    // GENERATED

    public interface StartDateStep {
        NameStep withStartDate(Date startDate);
        NameStep withStartDate(java.sql.Date startDate);
    }

    public interface NameStep {
        PhoneStep withName(String name);
    }

    public interface PhoneStep {
        WebsiteStep withPhone(String phone);
    }

    public interface WebsiteStep {
        BuildStep withWebsite(String website);
    }

    public interface BuildStep {
        BuildStep withId(int id);
        BuildStep withPictureName(String pictureName);
        BuildStep withComment(String comment);
        BuildStep withCreatedAt(Date createdAt);
        BuildStep withUpdatedAt(Date updatedAt);
        FullCustomer build();
    }

    public static class StepBuilder implements StartDateStep, NameStep, PhoneStep, WebsiteStep, BuildStep {
        private int id;
        private Date startDate;
        private String name;
        private String phone;
        private String website;
        private String pictureName;
        private String comment;

        private Date createdAt;
        private Date updatedAt;

        private StepBuilder() {}

        @Override
        public NameStep withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public NameStep withStartDate(java.sql.Date startDate) {
            this.startDate = new Date(startDate.getTime());
            return this;
        }

        @Override
        public PhoneStep withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public WebsiteStep withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public BuildStep withWebsite(String website) {
            this.website = website;
            return this;
        }

        @Override
        public BuildStep withId(int id) {
            this.id = id;
            return this;
        }

        @Override
        public BuildStep withPictureName(String pictureName) {
            this.pictureName = pictureName;
            return this;
        }

        @Override
        public BuildStep withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public BuildStep withCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public BuildStep withUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public FullCustomer build() {
            return new FullCustomer(
                    this.id,
                    this.startDate,
                    this.name,
                    this.phone,
                    this.website,
                    this.pictureName,
                    this.comment,
                    this.createdAt,
                    this.updatedAt
            );
        }
    }

    public static final class Builder {
        private int id;
        private Date startDate;
        private String name;
        private String phone;
        private String website;
        private String pictureName;
        private String comment;

        private Date createdAt;
        private Date updatedAt;

        private Builder() {}

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withStartDate(java.sql.Date startDate) {
            this.startDate = new Date(startDate.getTime());
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder withPictureName(String pictureName) {
            this.pictureName = pictureName;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder withCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FullCustomer build() {
            return new FullCustomer(
                    this.id,
                    this.startDate,
                    this.name,
                    this.phone,
                    this.website,
                    this.pictureName,
                    this.comment,
                    this.createdAt,
                    this.updatedAt
            );
        }
    }

}
