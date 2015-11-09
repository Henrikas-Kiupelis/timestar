package com.superum.api.v3.teacher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import eu.goodlike.misc.Scaleless;
import eu.goodlike.misc.SpecialUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.superum.api.v3.teacher.TeacherConstants.*;

public final class FetchedTeacher {

    @JsonProperty(value = ID_FIELD)
    public int getId() {
        return id;
    }

    @JsonProperty(value = CREATED_AT_FIELD)
    public long getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(value = UPDATED_AT_FIELD)
    public long getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(value = PAYMENT_DAY_FIELD)
    public Integer getPaymentDay() {
        return paymentDay;
    }

    @JsonProperty(value = HOURLY_WAGE_FIELD)
    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    @JsonProperty(value = ACADEMIC_WAGE_FIELD)
    public BigDecimal getAcademicWage() {
        return academicWage;
    }

    @JsonProperty(value = NAME_FIELD)
    public String getName() {
        return name;
    }

    @JsonProperty(value = SURNAME_FIELD)
    public String getSurname() {
        return surname;
    }

    @JsonProperty(value = PHONE_FIELD)
    public String getPhone() {
        return phone;
    }

    @JsonProperty(value = CITY_FIELD)
    public String getCity() {
        return city;
    }

    @JsonProperty(value = EMAIL_FIELD)
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = PICTURE_FIELD)
    public String getPicture() {
        return picture;
    }

    @JsonProperty(value = DOCUMENT_FIELD)
    public String getDocument() {
        return document;
    }

    @JsonProperty(value = COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    @JsonProperty(value = LANGUAGES_FIELD)
    public List<String> getLanguages() {
        return languages;
    }

    // CONSTRUCTORS

    public static FetchedTeacher jsonInstance(@JsonProperty(value = ID_FIELD) int id,
                                              @JsonProperty(value = CREATED_AT_FIELD) long createdAt,
                                              @JsonProperty(value = UPDATED_AT_FIELD) long updatedAt,
                                              @JsonProperty(value = PAYMENT_DAY_FIELD) Integer paymentDay,
                                              @JsonProperty(value = HOURLY_WAGE_FIELD) BigDecimal hourlyWage,
                                              @JsonProperty(value = ACADEMIC_WAGE_FIELD) BigDecimal academicWage,
                                              @JsonProperty(value = NAME_FIELD) String name,
                                              @JsonProperty(value = SURNAME_FIELD) String surname,
                                              @JsonProperty(value = PHONE_FIELD) String phone,
                                              @JsonProperty(value = CITY_FIELD) String city,
                                              @JsonProperty(value = EMAIL_FIELD) String email,
                                              @JsonProperty(value = PICTURE_FIELD) String picture,
                                              @JsonProperty(value = DOCUMENT_FIELD) String document,
                                              @JsonProperty(value = COMMENT_FIELD) String comment,
                                              @JsonProperty(value = LANGUAGES_FIELD) List<String> languages) {
        return new FetchedTeacher(id, createdAt, updatedAt, paymentDay, hourlyWage, academicWage, name, surname,
                phone, city, email, picture, document, comment, languages);
    }

    public static IdStep stepBuilder() {
        return builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public FetchedTeacher(int id, long createdAt, long updatedAt, Integer paymentDay, BigDecimal hourlyWage,
                          BigDecimal academicWage, String name, String surname, String phone, String city, String email,
                          String picture, String document, String comment, List<String> languages) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paymentDay = paymentDay;
        this.hourlyWage = hourlyWage;
        this.academicWage = academicWage;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.picture = picture;
        this.document = document;
        this.comment = comment;
        this.languages = languages;
    }

    // PRIVATE

    private final int id;
    private final long createdAt;
    private final long updatedAt;

    private final Integer paymentDay;
    private final BigDecimal hourlyWage;
    private final BigDecimal academicWage;
    private final String name;
    private final String surname;
    private final String phone;
    private final String city;
    private final String email;
    private final String picture;
    private final String document;
    private final String comment;
    private final List<String> languages;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("paymentDay", paymentDay)
                .add("hourlyWage", hourlyWage)
                .add("academicWage", academicWage)
                .add("name", name)
                .add("surname", surname)
                .add("phone", phone)
                .add("city", city)
                .add("email", email)
                .add("picture", picture)
                .add("document", document)
                .add("comment", comment)
                .add("languages", languages)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedTeacher)) return false;
        FetchedTeacher that = (FetchedTeacher) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(paymentDay, that.paymentDay) &&
                SpecialUtils.equals(hourlyWage, that.hourlyWage, Scaleless::bigDecimal) &&
                SpecialUtils.equals(academicWage, that.academicWage, Scaleless::bigDecimal) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(city, that.city) &&
                Objects.equals(email, that.email) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(document, that.document) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(languages, that.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, paymentDay, Scaleless.bigDecimal(hourlyWage),
                Scaleless.bigDecimal(hourlyWage), name, surname, phone, city, email, picture, document,
                comment, languages);
    }

    // BUILDER

    public interface IdStep {
        CreatedAtStep withId(int id);
    }

    public interface CreatedAtStep {
        UpdatedAtStep withCreatedAt(long createdAt);
    }

    public interface UpdatedAtStep {
        PaymentDayStep withUpdatedAt(long updatedAt);
    }

    public interface PaymentDayStep {
        HourlyWageStep withPaymentDay(Integer paymentDay);
    }

    public interface HourlyWageStep {
        AcademicWageStep withHourlyWage(BigDecimal hourlyWage);
    }

    public interface AcademicWageStep {
        NameStep withAcademicWage(BigDecimal academicWage);
    }

    public interface NameStep {
        SurnameStep withName(String name);
    }

    public interface SurnameStep {
        PhoneStep withSurname(String surname);
    }

    public interface PhoneStep {
        CityStep withPhone(String phone);
    }

    public interface CityStep {
        EmailStep withCity(String city);
    }

    public interface EmailStep {
        PictureStep withEmail(String email);
    }

    public interface PictureStep {
        DocumentStep withPicture(String picture);
    }

    public interface DocumentStep {
        CommentStep withDocument(String document);
    }

    public interface CommentStep {
        LanguagesStep withComment(String comment);
    }

    public interface LanguagesStep {
        BuildStep withLanguages(List<String> languages);
    }

    public interface BuildStep {
        FetchedTeacher build();
    }


    public static class Builder implements IdStep, CreatedAtStep, UpdatedAtStep, PaymentDayStep, HourlyWageStep,
            AcademicWageStep, NameStep, SurnameStep, PhoneStep, CityStep, EmailStep, PictureStep, DocumentStep,
            CommentStep, LanguagesStep, BuildStep {
        private int id;
        private long createdAt;
        private long updatedAt;
        private Integer paymentDay;
        private BigDecimal hourlyWage;
        private BigDecimal academicWage;
        private String name;
        private String surname;
        private String phone;
        private String city;
        private String email;
        private String picture;
        private String document;
        private String comment;
        private List<String> languages;

        private Builder() {}

        @Override
        public Builder withId(int id) {
            this.id = id;
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
        public Builder withPaymentDay(Integer paymentDay) {
            this.paymentDay = paymentDay;
            return this;
        }

        @Override
        public Builder withHourlyWage(BigDecimal hourlyWage) {
            this.hourlyWage = hourlyWage;
            return this;
        }

        @Override
        public Builder withAcademicWage(BigDecimal academicWage) {
            this.academicWage = academicWage;
            return this;
        }

        @Override
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        @Override
        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        @Override
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public Builder withPicture(String picture) {
            this.picture = picture;
            return this;
        }

        @Override
        public Builder withDocument(String document) {
            this.document = document;
            return this;
        }

        @Override
        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public Builder withLanguages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        @Override
        public FetchedTeacher build() {
            return new FetchedTeacher(
                    this.id,
                    this.createdAt,
                    this.updatedAt,
                    this.paymentDay,
                    this.hourlyWage,
                    this.academicWage,
                    this.name,
                    this.surname,
                    this.phone,
                    this.city,
                    this.email,
                    this.picture,
                    this.document,
                    this.comment,
                    this.languages
            );
        }
    }

}
