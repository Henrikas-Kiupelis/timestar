package com.superum.api.v3.customer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.v3.customer.CustomerErrors;
import eu.goodlike.v2.validate.Validate;

import java.util.Objects;

import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_STRING;
import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_STRING_BLANK_ABLE;
import static com.superum.api.v3.customer.CustomerConstants.*;

/**
 * <pre>
 * DTO which primarily focuses on representing incoming customer data
 *
 * Instances of this class are created directly from JSON in HTTP bodies
 *
 * The following JSON fields are parsed:
 *      FIELD_NAME   : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      startDate   : date when a contract was signed                           date String, "yyyy-MM-dd"
 *      name        : name                                                      any String, max 180 chars
 *      phone       : phone number                                              any String, max 180 chars
 *      website     : website                                                   any String, max 180 chars
 *      picture     : link to a picture of this customer, stored somewhere      any String, max 180 chars
 *      comment     : comment, made by the app client                           any String, max 500 chars
 *
 * All values are assumed optional
 *
 * Example of JSON to send:
 * {
 *      "startDate": "2015-07-22",
 *      "name": "SUPERUM",
 *      "phone": "+37069900001",
 *      "website": "http://superum.eu",
 *      "picture": "http://timestar.lt/uploads/superum.jpg",
 *      "comment": "What a company"
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class SuppliedCustomer {

    @JsonProperty(value = START_DATE_FIELD)
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty(value = NAME_FIELD)
    public String getName() {
        return name;
    }

    @JsonProperty(value = PHONE_FIELD)
    public String getPhone() {
        return phone;
    }

    @JsonProperty(value = WEBSITE_FIELD)
    public String getWebsite() {
        return website;
    }

    @JsonProperty(value = PICTURE_FIELD)
    public String getPicture() {
        return picture;
    }

    @JsonProperty(value = COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    public void validateForCreation() {
        Validate.string().isNull().or().not().isBlank().isDate()
                .ifInvalid(startDate).thenThrow(CustomerErrors::startDateError);
        OPTIONAL_JSON_STRING.ifInvalid(name).thenThrow(CustomerErrors::nameError)
                .ifInvalid(phone).thenThrow(CustomerErrors::phoneError)
                .ifInvalid(website).thenThrow(CustomerErrors::websiteError);
        OPTIONAL_JSON_STRING_BLANK_ABLE.ifInvalid(picture).thenThrow(CustomerErrors::pictureError);
        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT)
                .ifInvalid(comment).thenThrow(CustomerErrors::commentError);
    }

    public void validateForUpdating() {
        validateForCreation();
    }

    // CONSTRUCTORS

    @JsonCreator
    public static SuppliedCustomer jsonInstance(@JsonProperty(value = START_DATE_FIELD) String startDate,
                                                @JsonProperty(value = NAME_FIELD) String name,
                                                @JsonProperty(value = PHONE_FIELD) String phone,
                                                @JsonProperty(value = WEBSITE_FIELD) String website,
                                                @JsonProperty(value = PICTURE_FIELD) String picture,
                                                @JsonProperty(value = COMMENT_FIELD) String comment) {
        return new SuppliedCustomer(startDate, name, phone, website, picture, comment);
    }

    public static StartDateStep stepBuilder() {
        return builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public SuppliedCustomer(String startDate, String name, String phone, String website, String picture, String comment) {
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;
    }

    // PRIVATE

    private final String startDate;
    private final String name;
    private final String phone;
    private final String website;
    private final String picture;
    private final String comment;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("startDate", startDate)
                .add("name", name)
                .add("phone", phone)
                .add("website", website)
                .add("picture", picture)
                .add("comment", comment)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuppliedCustomer)) return false;
        SuppliedCustomer that = (SuppliedCustomer) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(website, that.website) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, name, phone, website, picture, comment);
    }

    // BUILDER

    public interface StartDateStep {
        NameStep withStartDate(String startDate);
    }

    public interface NameStep {
        PhoneStep withName(String name);
    }

    public interface PhoneStep {
        WebsiteStep withPhone(String phone);
    }

    public interface WebsiteStep {
        PictureStep withWebsite(String website);
    }

    public interface PictureStep {
        CommentStep withPicture(String picture);
    }

    public interface CommentStep {
        BuildStep withComment(String comment);
    }

    public interface BuildStep {
        SuppliedCustomer build();
    }

    public static class Builder implements StartDateStep, NameStep, PhoneStep, WebsiteStep, PictureStep, CommentStep, BuildStep {
        private String startDate;
        private String name;
        private String phone;
        private String website;
        private String picture;
        private String comment;

        private Builder() {}

        @Override
        public Builder withStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        @Override
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public Builder withWebsite(String website) {
            this.website = website;
            return this;
        }

        @Override
        public Builder withPicture(String picture) {
            this.picture = picture;
            return this;
        }

        @Override
        public BuildStep withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public SuppliedCustomer build() {
            return new SuppliedCustomer(
                    this.startDate,
                    this.name,
                    this.phone,
                    this.website,
                    this.picture,
                    this.comment
            );
        }
    }

}
