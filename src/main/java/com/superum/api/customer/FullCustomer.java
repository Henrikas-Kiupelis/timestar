package com.superum.api.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.db.customer.Customer;
import com.superum.db.customer.lang.CustomerLanguages;
import com.superum.fields.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.superum.api.customer.FullCustomer.RequiredBuilder.fullCustomer;
import static com.superum.utils.ValidationUtils.*;

/**
 * <pre>
 * Contains all information about the customer in one place
 *
 * When creating an instance of FullCustomer with JSON, these fields are required:
 *     FIELD_NAME    : FIELD_DESCRIPTION                                          FIELD_CONSTRAINTS
 *
 *     paymentDay    : day of the month that the customer must pay money before;  1 <= paymentDay <= 31
 *     startDate     : date when a contract was signed;                           any java.sql.Date
 *     paymentValue  : hourly rate for services, in euros;                        0 <= paymentValue; BigDecimal
 *     name          : name                                                       any String, max 30 chars
 *     phone         : phone number                                               any String, max 30 chars
 *     website       : website                                                    any String, max 30 chars
 *     languages     : list of languages and levels the customer is paying for    any List of Strings, max 20 chars
 *
 * These fields are optional:
 *     pictureName   : name of the picture of the customer, stored somewhere      any String, max 100 chars
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
 *     "paymentDay":"1",
 *     "startDate":"2015-07-22",
 *     "paymentValue":"23.33",
 *     "name":"SUPERUM",
 *     "phone":"+37069900001",
 *     "website":"http://superum.eu",
 *     "languages":["English: C1", "English: C2"],
 *     "pictureName":"superum.jpg",
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
    @JsonIgnore
    public FullCustomer withId(int id) {
        return new FullCustomer(id, getPaymentDay(), getStartDate(), getPaymentValue(), getName(), getPhone(), getWebsite(), getPictureName(), getComment(), getLanguages());
    }
	
	@JsonProperty(PAYMENT_DAY_FIELD)
	public byte getPaymentDay() {
		return paymentDay.byteValue();
	}
    @JsonIgnore
    public boolean hasPaymentDay() {
        return paymentDay.isSet();
    }
	
	@JsonProperty(START_DATE_FIELD)
	public Date getStartDate() {
		return startDate.getValue();
	}
    @JsonIgnore
    public boolean hasStartDate() {
        return startDate.isSet();
    }
	
	@JsonProperty(PAYMENT_VALUE_FIELD)
	public BigDecimal getPaymentValue() {
		return paymentValue.getValue();
	}
    @JsonIgnore
    public boolean hasPaymentValue() {
        return paymentValue.isSet();
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
	
	@JsonProperty(PICTURE_NAME_FIELD)
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

    @JsonProperty(LANGUAGES_FIELD)
    public List<String> getLanguages() {
        return languages.getValue();
    }
    @JsonIgnore
    public boolean hasLanguages() {
        return languages.isSet();
    }

    /**
     * <pre>
     * Returns true if all the mandatory fields are set; false otherwise
     * </pre>
     */
    @JsonIgnore
    public boolean canBeInserted() {
        return mandatoryFields().allMatch(Field::isSet);
    }

    /**
     * <pre>
     * Returns true if at least one field, other than id or languages, is set; false otherwise
     *
     * Languages is not a Customer field, but a field of CustomerLanguages
     * </pre>
     */
    @JsonIgnore
    public boolean canUpdateCustomer() {
        return allFields.stream()
                .filter(field -> field != id)
                .filter(field -> field != languages)
                .anyMatch(Field::isSet);
    }

    /**
     * <pre>
     * Returns true if languages field is set; false otherwise;
     *
     * Languages is the only field in CustomerLanguages
     * </pre>
     */
    @JsonIgnore
    public boolean canUpdateCustomerLanguages() {
        return hasLanguages();
    }

    /**
     * <pre>
     * Returns a list of names of mandatory fields that are not set
     * </pre>
     */
    @JsonIgnore
    public List<String> missingMandatoryFieldNames() {
        return mandatoryFields()
                .filter(field -> !field.isSet())
                .map(NamedField::getFieldName)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Customer toCustomer() {
        return new Customer(getId(), getPaymentDay(), getStartDate(), getPaymentValue(), getName(), getPhone(), getWebsite(), getPictureName(), getComment());
    }

    @JsonIgnore
    public CustomerLanguages toCustomerLanguages() {
        return new CustomerLanguages(getId(), getLanguages());
    }

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return allFields.stream()
                .map(Field::toString)
                .collect(Collectors.joining(", "));
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
        return allFields.hashCode();
	}

	// CONSTRUCTORS

	@JsonCreator
	public FullCustomer(@JsonProperty(ID_FIELD) int id,
                        @JsonProperty(PAYMENT_DAY_FIELD) byte paymentDay,
                        @JsonProperty(START_DATE_FIELD) Date startDate,
                        @JsonProperty(PAYMENT_VALUE_FIELD) BigDecimal paymentValue,
                        @JsonProperty(NAME_FIELD) String name,
                        @JsonProperty(PHONE_FIELD) String phone,
                        @JsonProperty(WEBSITE_FIELD) String website,
                        @JsonProperty(PICTURE_NAME_FIELD) String pictureName,
                        @JsonProperty(COMMENT_FIELD) String comment,
                        @JsonProperty(LANGUAGES_FIELD) List<String> languages) {
        // Equivalent to (id != 0 && id <= 0); when id == 0, it simply was not set, so the state is valid, while id is not
        if (id < 0)
            throw new InvalidCustomerException("Customer id must be positive.");

        this.id = new IntField(ID_FIELD, id, Mandatory.YES);

        if (paymentDay != 0 && !isDayOfMonth(paymentDay))
            throw new InvalidCustomerException("Payment day for customer doesn't exist: " + paymentDay);

		this.paymentDay = new ByteField(PAYMENT_DAY_FIELD, paymentDay, Mandatory.YES);

        if (isNegativeNotNull(paymentValue))
            throw new InvalidCustomerException("Payment value for customer must be non-negative: " + paymentValue);

		this.paymentValue = new SimpleField<>(PAYMENT_VALUE_FIELD, paymentValue, Mandatory.YES);

        if (!fitsOrNull(NAME_SIZE_LIMIT, name))
            throw new InvalidCustomerException("Customer name must not exceed " + NAME_SIZE_LIMIT + " chars");

		this.name = new SimpleField<>(NAME_FIELD, name, Mandatory.YES);

        if (!fitsOrNull(PHONE_SIZE_LIMIT, phone))
            throw new InvalidCustomerException("Customer phone must not exceed " + PHONE_SIZE_LIMIT + " chars");

		this.phone = new SimpleField<>(PHONE_FIELD, phone, Mandatory.YES);

        if (!fitsOrNull(WEBSITE_SIZE_LIMIT, website))
            throw new InvalidCustomerException("Customer website must not exceed " + WEBSITE_SIZE_LIMIT + " chars");

		this.website = new SimpleField<>(WEBSITE_FIELD, website, Mandatory.YES);

        if (!fitsOrNull(PICTURE_NAME_SIZE_LIMIT, pictureName))
            throw new InvalidCustomerException("Customer picture name must not exceed " + PICTURE_NAME_SIZE_LIMIT + " chars");

		this.pictureName = new SimpleField<>(PICTURE_NAME_FIELD, pictureName, Mandatory.NO);

        if (!fitsOrNull(COMMENT_SIZE_LIMIT, comment))
            throw new InvalidCustomerException("Customer comment must not exceed " + COMMENT_SIZE_LIMIT + " chars");

		this.comment = new SimpleField<>(COMMENT_FIELD, comment, Mandatory.NO);

        if (languages == null)
            this.languages = SimpleField.empty(LANGUAGES_FIELD, Mandatory.YES);
        else {
            if (!languages.stream().allMatch(languageString -> fitsNotNull(LANGUAGE_ELEMENT_SIZE_LIMIT, languageString)))
                throw new InvalidCustomerException("Customer languages must not exceed " + LANGUAGE_ELEMENT_SIZE_LIMIT + " chars");
            // To ensure that this object is immutable, we wrap the list with an unmodifiable version
            this.languages = new SimpleField<>(LANGUAGES_FIELD, Collections.unmodifiableList(languages), Mandatory.YES);
        }

        this.startDate = new JavaSqlDateField(START_DATE_FIELD, startDate, Mandatory.YES);

        List<NamedField> allFields = new ArrayList<>();
        allFields.add(this.paymentDay);
        allFields.add(this.paymentValue);
        allFields.add(this.name);
        allFields.add(this.phone);
        allFields.add(this.website);
        allFields.add(this.languages);
        allFields.add(this.startDate);
        allFields.add(this.id);
        allFields.add(this.pictureName);
        allFields.add(this.comment);
        this.allFields = Collections.unmodifiableList(allFields);
    }

    public FullCustomer(Customer customer, CustomerLanguages languages) {
        this(customer, languages.getLanguages());
    }

    public FullCustomer(Customer customer, List<String> languages) {
        this(customer.getId(), customer.getPaymentDay(), customer.getStartDate(), customer.getPaymentValue(),
                customer.getName(), customer.getPhone(), customer.getWebsite(), customer.getPictureName(),
                customer.getComment(), languages);
    }

    /**
     * <pre>
     * Creates a new builder which can be used to make any kind of FullCustomer
     *
     * Intended for updating
     * </pre>
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * <pre>
     * Creates a new builder which creates a FullCustomer that has all its mandatory fields set
     *
     * Intended for creating
     * </pre>
     */
    public static PaymentDayStep newRequiredBuilder() {
        return fullCustomer();
    }

	// PRIVATE
	
	private final IntField id;
	private final ByteField paymentDay;
	private final JavaSqlDateField startDate;
	private final NamedField<BigDecimal> paymentValue;
	private final NamedField<String> name;
	private final NamedField<String> phone;
	private final NamedField<String> website;
	private final NamedField<String> pictureName;
	private final NamedField<String> comment;
    private final NamedField<List<String>> languages;

    private final List<NamedField> allFields;

    private Stream<NamedField> mandatoryFields() {
        return allFields.stream().filter(MaybeField::isMandatory);
    }

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int WEBSITE_SIZE_LIMIT = 30;
    private static final int PICTURE_NAME_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;
    private static final int LANGUAGE_ELEMENT_SIZE_LIMIT = 20;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String PAYMENT_DAY_FIELD = "paymentDay";
    private static final String START_DATE_FIELD = "startDate";
    private static final String PAYMENT_VALUE_FIELD = "paymentValue";
    private static final String NAME_FIELD = "name";
    private static final String PHONE_FIELD = "phone";
    private static final String WEBSITE_FIELD = "website";
    private static final String PICTURE_NAME_FIELD = "pictureName";
    private static final String COMMENT_FIELD = "comment";
    private static final String LANGUAGES_FIELD = "languages";

    // GENERATED

    public interface PaymentDayStep {
        StartDateStep withPaymentDay(byte paymentDay);
        StartDateStep withPaymentDay(int paymentDay);
    }

    public interface StartDateStep {
        PaymentValueStep withStartDate(Date startDate);
    }

    public interface PaymentValueStep {
        NameStep withPaymentValue(BigDecimal paymentValue);
    }

    public interface NameStep {
        PhoneStep withName(String name);
    }

    public interface PhoneStep {
        WebsiteStep withPhone(String phone);
    }

    public interface WebsiteStep {
        LanguagesStep withWebsite(String website);
    }

    public interface LanguagesStep {
        BuildStep withLanguages(String... languages);
        BuildStep withLanguages(Collection<String> languages);
        BuildStep withLanguages(List<String> languages);
        BuildStep noLanguages();
    }

    public interface BuildStep {
        BuildStep withId(int id);
        BuildStep withPictureName(String pictureName);
        BuildStep withComment(String comment);
        FullCustomer build();
    }

    public static class RequiredBuilder implements PaymentDayStep, StartDateStep, PaymentValueStep, NameStep, PhoneStep, WebsiteStep, LanguagesStep, BuildStep {
        private int id;
        private byte paymentDay;
        private Date startDate;
        private BigDecimal paymentValue;
        private String name;
        private String phone;
        private String website;
        private String pictureName;
        private String comment;
        private List<String> languages;

        private RequiredBuilder() {}

        public static PaymentDayStep fullCustomer() {
            return new RequiredBuilder();
        }

        @Override
        public StartDateStep withPaymentDay(byte paymentDay) {
            this.paymentDay = paymentDay;
            return this;
        }

        @Override
        public StartDateStep withPaymentDay(int paymentDay) {
            this.paymentDay = (byte)paymentDay;
            return this;
        }

        @Override
        public PaymentValueStep withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public NameStep withPaymentValue(BigDecimal paymentValue) {
            this.paymentValue = paymentValue;
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
        public LanguagesStep withWebsite(String website) {
            this.website = website;
            return this;
        }

        @Override
        public BuildStep withLanguages(String... languages) {
            this.languages = Arrays.asList(languages);
            return this;
        }

        @Override
        public BuildStep withLanguages(Collection<String> languages) {
            this.languages = new ArrayList<>(languages);
            return this;
        }

        @Override
        public BuildStep withLanguages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        @Override
        public BuildStep noLanguages() {
            this.languages = Collections.emptyList();
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
        public FullCustomer build() {
            return new FullCustomer(
                    this.id,
                    this.paymentDay,
                    this.startDate,
                    this.paymentValue,
                    this.name,
                    this.phone,
                    this.website,
                    this.pictureName,
                    this.comment,
                    this.languages
            );
        }
    }

    public static final class Builder {
        private int id;
        private byte paymentDay;
        private Date startDate;
        private BigDecimal paymentValue;
        private String name;
        private String phone;
        private String website;
        private String pictureName;
        private String comment;
        private List<String> languages;

        private Builder() {}

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withPaymentDay(int paymentDay) {
            this.paymentDay = (byte)paymentDay;
            return this;
        }

        public Builder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withPaymentValue(BigDecimal paymentValue) {
            this.paymentValue = paymentValue;
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

        public Builder withLanguages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        public FullCustomer build() {
            return new FullCustomer(
                    this.id,
                    this.paymentDay,
                    this.startDate,
                    this.paymentValue,
                    this.name,
                    this.phone,
                    this.website,
                    this.pictureName,
                    this.comment,
                    this.languages
            );
        }
    }

}
