package com.superum.api.v3.customer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.superum.api.v3.customer.CustomerConstants.*;

/**
 * <pre>
 * DTO which primarily focuses on representing outgoing customer data
 *
 * Instances of this class are created directly from a Record from the database
 *
 * Expect the following fields in JSON:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      id          : id of this customer
 *      startDate   : day at which the customer's contract started
 *      name        : name of the customer
 *      phone       : phone of the customer
 *      website     : website of the customer
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
public class FetchedCustomer {

    @JsonProperty(value = ID_FIELD)
    public int getId() {
        return id;
    }

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

    @JsonProperty(value = CREATED_AT_FIELD)
    public long getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(value = UPDATED_AT_FIELD)
    public long getUpdatedAt() {
        return updatedAt;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static FetchedCustomer jsonInstance(@JsonProperty(value = ID_FIELD) int id,
                                               @JsonProperty(value = START_DATE_FIELD) String startDate,
                                               @JsonProperty(value = NAME_FIELD) String name,
                                               @JsonProperty(value = PHONE_FIELD) String phone,
                                               @JsonProperty(value = WEBSITE_FIELD) String website,
                                               @JsonProperty(value = PICTURE_FIELD) String picture,
                                               @JsonProperty(value = COMMENT_FIELD) String comment,
                                               @JsonProperty(value = CREATED_AT_FIELD) long createdAt,
                                               @JsonProperty(value = UPDATED_AT_FIELD) long updatedAt) {
        return new FetchedCustomer(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }

    public static IdStep stepBuilder() {
        return builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public FetchedCustomer(int id, String startDate, String name, String phone, String website, String picture,
                           String comment, long createdAt, long updatedAt) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PRIVATE

    private final int id;
    private final String startDate;
    private final String name;
    private final String phone;
    private final String website;
    private final String picture;
    private final String comment;
    private final long createdAt;
    private final long updatedAt;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startDate", startDate)
                .add("name", name)
                .add("phone", phone)
                .add("website", website)
                .add("picture", picture)
                .add("comment", comment)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedCustomer)) return false;
        FetchedCustomer that = (FetchedCustomer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(website, that.website) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, name, phone, website, picture, comment, createdAt, updatedAt);
    }

    // BUILDER

    public interface IdStep {
        StartDateStep withId(int id);
    }

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
        CreatedAtStep withComment(String comment);
    }

    public interface CreatedAtStep {
        UpdatedAtStep withCreatedAt(long createdAt);
    }

    public interface UpdatedAtStep {
        BuildStep withUpdatedAt(long updatedAt);
    }

    public interface BuildStep {
        FetchedCustomer build();
    }


    public static class Builder implements IdStep, StartDateStep, NameStep, PhoneStep, WebsiteStep, PictureStep, CommentStep, CreatedAtStep, UpdatedAtStep, BuildStep {
        private int id;
        private String startDate;
        private String name;
        private String phone;
        private String website;
        private String picture;
        private String comment;
        private long createdAt;
        private long updatedAt;

        private Builder() {}

        @Override
        public Builder withId(int id) {
            this.id = id;
            return this;
        }

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
        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public Builder withCreatedAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public Builder withUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public FetchedCustomer build() {
            return new FetchedCustomer(
                    this.id,
                    this.startDate,
                    this.name,
                    this.phone,
                    this.website,
                    this.picture,
                    this.comment,
                    this.createdAt,
                    this.updatedAt
            );
        }
    }

}
