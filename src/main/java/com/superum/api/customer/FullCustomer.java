package com.superum.api.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
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

    public boolean canBeInsertedAsCustomer() {
        return !hasId() && hasPaymentDay() && hasStartDate() && hasPaymentValue() && hasName() && hasPhone() && hasWebsite();
    }

	public boolean canBeInsertedAsCustomerLanguages() {
        return hasId() && hasLanguages();
    }

    public boolean canUpdateCustomer() {
        return hasId() && (hasPaymentDay() || hasStartDate() || hasPaymentValue() || hasName() || hasPhone() || hasWebsite() || hasPictureName() || hasComment());
    }

    public boolean canUpdateCustomerLanguages() {
        return hasId() && hasLanguages();
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
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + paymentDay;
		result = (result << 5) - result + (startDate == null ? 0 : startDate.hashCode());
		result = (result << 5) - result + (paymentValue == null ? 0 : paymentValue.hashCode());
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		result = (result << 5) - result + (phone == null ? 0 : phone.hashCode());
		result = (result << 5) - result + (website == null ? 0 : website.hashCode());
		result = (result << 5) - result + (website == null ? 0 : pictureName.hashCode());
		result = (result << 5) - result + (website == null ? 0 : comment.hashCode());
        result = (result << 5) - result + languages.hashCode();
        return result;
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
		this.id = id;
		this.paymentDay = paymentDay;
		this.startDate = startDate;
		this.paymentValue = paymentValue;
		this.name = name;
		this.phone = phone;
		this.website = website;
		this.pictureName = pictureName;
		this.comment = comment;
        this.languages = languages;
	}

    private FullCustomer(Builder builder) {
        id = builder.id;
        paymentDay = builder.paymentDay;
        startDate = builder.startDate;
        paymentValue = builder.paymentValue;
        name = builder.name;
        phone = builder.phone;
        website = builder.website;
        pictureName = builder.pictureName;
        comment = builder.comment;
        languages = builder.languages;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

	// PRIVATE
	
	@Min(value = 0, message = "All IDs must be non-negative")
	private final int id;
	
	@Min(value = 0, message = "No negative days exist")
	@Max(value = 31, message = "The payment day must be at most the last day of the month")
	private final byte paymentDay;
	
	private final Date startDate;
	
	private final BigDecimal paymentValue;
	
	@Size(max = 30, message = "Name size must not exceed 30 characters")
	private final String name;
	
	@Size(max = 30, message = "Phone size must not exceed 30 characters")
	private final String phone;
	
	@Size(max = 30, message = "Website size must not exceed 30 characters")
	private final String website;
	
	@Size(max = 100, message = "Picture name size must not exceed 100 characters")
	private final String pictureName;
	
	@Size(max = 500, message = "The comment must not exceed 500 characters")
	private final String comment;

    private final List<String> languages;

    // GENERATED

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

        public Builder withPaymentDay(byte paymentDay) {
            this.paymentDay = paymentDay;
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
            return new FullCustomer(this);
        }
    }

}
