package com.superum.api.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.db.customer.Customer;
import com.superum.db.customer.lang.CustomerLanguages;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

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
 * If you omit a field, it will assume default value (null for objects, 0 for primitive numbers, false for boolean),
 * all of which are assumed to be allowed unless stated otherwise (check FIELD_CONSTRAINTS)
 *
 * Example of JSON:
 * {
 *     "id":"1",
 *     "paymentDay":"1",
 *     "startDate":"2015-07-22",
 *     "paymentValue":"23,33",
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

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	@JsonIgnore
	public boolean hasId() {
		return id > 0;
	}
    @JsonIgnore
    public FullCustomer withId(int id) {
        return new FullCustomer(id, paymentDay, startDate, paymentValue, name, phone, website, pictureName, comment, languages);
    }
	
	@JsonProperty("paymentDay")
	public byte getPaymentDay() {
		return paymentDay;
	}
    @JsonIgnore
    public boolean hasPaymentDay() {
        return paymentDay > 0;
    }
	
	@JsonProperty("startDate")
	public Date getStartDate() {
		return startDate;
	}
    @JsonIgnore
    public boolean hasStartDate() {
        return startDate != null;
    }
	
	@JsonProperty("paymentValue")
	public BigDecimal getPaymentValue() {
		return paymentValue;
	}
    @JsonIgnore
    public boolean hasPaymentValue() {
        return paymentValue != null;
    }
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
    @JsonIgnore
    public boolean hasName() {
        return name != null;
    }
	
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
    @JsonIgnore
    public boolean hasPhone() {
        return phone != null;
    }
	
	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}
    @JsonIgnore
    public boolean hasWebsite() {
        return website != null;
    }
	
	@JsonProperty("pictureName")
	public String getPictureName() {
		return pictureName;
	}
    @JsonIgnore
    public boolean hasPictureName() {
        return pictureName != null;
    }
	
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
    @JsonIgnore
    public boolean hasComment() {
        return comment != null;
    }

    @JsonProperty("languages")
    public List<String> getLanguages() {
        return languages;
    }
    @JsonIgnore
    public boolean hasLanguages() {
        return languages != null;
    }

    /**
     * <pre>
     * Returns true if all the mandatory fields are set, and the id field is NOT set; false otherwise
     * </pre>
     */
    @JsonIgnore
    public boolean canBeInserted() {
        return !hasId() && hasPaymentDay() && hasStartDate() && hasPaymentValue() && hasName() && hasPhone() && hasWebsite() && hasLanguages();
    }

    /**
     * <pre>
     * Returns true if the id field is set, and at least one more Customer field is set; false otherwise
     * </pre>
     */
    @JsonIgnore
    public boolean canUpdateCustomer() {
        return hasId() && (hasPaymentDay() || hasStartDate() || hasPaymentValue() || hasName() || hasPhone() || hasWebsite() || hasPictureName() || hasComment());
    }

    /**
     * <pre>
     * Returns true if the id field is set, and languages field is set; false otherwise;
     *
     * Languages is the only other field in CustomerLanguages
     * </pre>
     */
    @JsonIgnore
    public boolean canUpdateCustomerLanguages() {
        return hasId() && hasLanguages();
    }

    @JsonIgnore
    public Customer toCustomer() {
        return new Customer(id, paymentDay, startDate, paymentValue, name, phone, website, pictureName, comment);
    }

    @JsonIgnore
    public CustomerLanguages toCustomerLanguages() {
        return new CustomerLanguages(id, languages);
    }
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Customer ID: " + id,
				"Payment day: " + paymentDay,
				"Contract started: " + startDate,
				"Payment value: " + paymentValue, 
				"Name: " + name,
				"Phone: " + phone,
				"Website: " + website,
				"Picture: " + pictureName,
				"Comment: " + comment,
                "Languages: " + languages);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof FullCustomer))
			return false;

		FullCustomer other = (FullCustomer) o;

		return this.id == other.id
				&& this.paymentDay == other.paymentDay
				&& Objects.equals(this.startDate, other.startDate)
				&& Objects.equals(this.paymentValue, other.paymentValue)
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.phone, other.phone)
				&& Objects.equals(this.website, other.website)
				&& Objects.equals(this.pictureName, other.pictureName)
				&& Objects.equals(this.comment, other.comment)
                && Objects.equals(this.languages, other.languages);
	}

	@Override
	public int hashCode() {
        return ObjectUtils.hash(id, paymentDay, startDate, paymentValue, name, phone, website, pictureName, comment, languages);
	}

	// CONSTRUCTORS

	@JsonCreator
	public FullCustomer(@JsonProperty("id") int id,
                        @JsonProperty("paymentDay") byte paymentDay,
                        @JsonProperty("startDate") Date startDate,
                        @JsonProperty("paymentValue") BigDecimal paymentValue,
                        @JsonProperty("name") String name,
                        @JsonProperty("phone") String phone,
                        @JsonProperty("website") String website,
                        @JsonProperty("pictureName") String pictureName,
                        @JsonProperty("comment") String comment,
                        @JsonProperty("languages") List<String> languages) {
        // Equivalent to (id != 0 && id <= 0); when id == 0, it simply was not set, so the state is valid, while id is not
        if (id < 0)
            throw new InvalidCustomerException("Customer id must be positive.");
		this.id = id;

        if (paymentDay != 0 && !isDayOfMonth(paymentDay))
            throw new InvalidCustomerException("Payment day for customer doesn't exist: " + paymentDay);
		this.paymentDay = paymentDay;

        if (isNegativeNotNull(paymentValue))
            throw new InvalidCustomerException("Payment value for customer must be non-negative: " + paymentValue);
		this.paymentValue = paymentValue;

        if (!fitsOrNull(NAME_SIZE_LIMIT, name))
            throw new InvalidCustomerException("Customer name must not exceed " + NAME_SIZE_LIMIT + " chars");
		this.name = name;

        if (!fitsOrNull(PHONE_SIZE_LIMIT, phone))
            throw new InvalidCustomerException("Customer phone must not exceed " + PHONE_SIZE_LIMIT + " chars");
		this.phone = phone;

        if (!fitsOrNull(WEBSITE_SIZE_LIMIT, website))
            throw new InvalidCustomerException("Customer website must not exceed " + WEBSITE_SIZE_LIMIT + " chars");
		this.website = website;

        if (!fitsOrNull(PICTURE_NAME_SIZE_LIMIT, pictureName))
            throw new InvalidCustomerException("Customer picture name must not exceed " + PICTURE_NAME_SIZE_LIMIT + " chars");
		this.pictureName = pictureName;

        if (!fitsOrNull(COMMENT_SIZE_LIMIT, comment))
            throw new InvalidCustomerException("Customer comment must not exceed " + COMMENT_SIZE_LIMIT + " chars");
		this.comment = comment;

        if (languages == null)
            this.languages = null;
        else {
            if (!languages.stream().allMatch(languageString -> fitsNotNull(LANGUAGE_SIZE_LIMIT, languageString)))
                throw new InvalidCustomerException("Customer languages must not be null or exceed " + LANGUAGE_SIZE_LIMIT + " chars");
            // To ensure that this object is immutable, we wrap the list with an unmodifiable version
            this.languages = Collections.unmodifiableList(languages);
        }

        this.startDate = startDate;
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
     * Intended for inserting
     * </pre>
     */
    public static PaymentDayStep newRequiredBuilder() {
        return fullCustomer();
    }

	// PRIVATE
	
	private final int id;
	private final byte paymentDay;
	private final Date startDate;
	private final BigDecimal paymentValue;
	private final String name;
	private final String phone;
	private final String website;
	private final String pictureName;
	private final String comment;
    private final List<String> languages;

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int WEBSITE_SIZE_LIMIT = 30;
    private static final int PICTURE_NAME_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;
    private static final int LANGUAGE_SIZE_LIMIT = 20;

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
