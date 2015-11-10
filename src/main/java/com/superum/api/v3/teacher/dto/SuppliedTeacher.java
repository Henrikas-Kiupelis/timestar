package com.superum.api.v3.teacher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.v3.teacher.TeacherErrors;
import eu.goodlike.misc.Scaleless;
import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.v2.validate.Validate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_STRING;
import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_STRING_BLANK_ABLE;
import static com.superum.api.v3.teacher.TeacherConstants.*;
import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

/**
 * <pre>
 * DTO which primarily focuses on representing incoming teacher data
 *
 * Instances of this class are created directly from JSON in HTTP bodies
 *
 * The following JSON fields are parsed:
 *      FIELD_NAME   : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      paymentDay   : day, at which the teacher is to be paid                  1 <= paymentDay <= 31
 *      hourlyWage   : the amount of euros paid per hour                        0 < hourlyWage;
 *                                                                              BigDecimal of up to 4 numbers after comma
 *      academicWage : the amount of euros paid per academic hour               0 < academicWage;
 *                     this is not necessarily equal to 3/4 * hourlyWage!       BigDecimal of up to 4 numbers after comma
 *      name         : name                                                     any String, max 180 chars
 *      surname      : surname                                                  any String, max 180 chars
 *      phone        : phone number                                             any String, max 180 chars
 *      city         : city (currently unknown which city this means)           any String, max 180 chars
 *      email        : email; this is also used for account/mailing             any String, max 180 chars
 *      picture      : link to a picture of this teacher, stored somewhere      any String, max 180 chars
 *      document     : link to a document uploaded by the teacher               any String, max 180 chars
 *      comment      : comment, made by the app client                          any String, max 500 chars
 *      languages    : list of languages the teacher teaches;                   any List of Strings, max 3 chars
 *                     uses codes, such as "ENG" to identity a language
 *
 * All values are assumed optional, if email is not given, no account will be created
 *
 * Example of JSON to send:
 * {
 *      "paymentDay":1,
 *      "hourlyWage":23,
 *      "academicWage":20,
 *      "name":"Henrikas",
 *      "surname":"Kiūpelis",
 *      "phone":"+37069900011",
 *      "city":"Vilnius",
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "picture":"http://timestar.lt/uploads/henrikas.jpg",
 *      "document":"http://timestar.lt/uploads/api.doc",
 *      "comment":"What a teacher",
 *      "languages": ["ENG"]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class SuppliedTeacher {

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

    public void validateForCreation() {
        Validate.integer().isNull().or().isDayOfMonth()
                .ifInvalid(paymentDay).thenThrow(TeacherErrors::paymentDayError);
        Validate.bigDecimal().isNull().or().isPositive()
                .ifInvalid(hourlyWage).thenThrow(TeacherErrors::hourlyWageError)
                .ifInvalid(academicWage).thenThrow(TeacherErrors::academicWageError);
        OPTIONAL_JSON_STRING.ifInvalid(name).thenThrow(TeacherErrors::nameError)
                .ifInvalid(surname).thenThrow(TeacherErrors::surnameError)
                .ifInvalid(phone).thenThrow(TeacherErrors::phoneError)
                .ifInvalid(city).thenThrow(TeacherErrors::cityError)
                .and().isEmail().ifInvalid(email).thenThrow(TeacherErrors::emailError);
        OPTIONAL_JSON_STRING_BLANK_ABLE.ifInvalid(picture).thenThrow(TeacherErrors::pictureError)
                .ifInvalid(document).thenThrow(TeacherErrors::documentError);
        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT)
                .ifInvalid(comment).thenThrow(TeacherErrors::commentError);
        Validate.collectionOf(String.class).isNull().or().not().isEmpty()
                .forEachIfNot(NOT_NULL_NOT_BLANK.and().isNoLargerThan(LANGUAGE_CODE_SIZE_LIMIT)).Throw(TeacherErrors::languageCodeError)
                .ifInvalid(languages).thenThrow(TeacherErrors::languagesError);
    }

    public void validateForUpdating() {
        validateForCreation();
    }

    // CONSTRUCTORS

    public static SuppliedTeacher jsonInstance(@JsonProperty(value = PAYMENT_DAY_FIELD) Integer paymentDay,
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
        return new SuppliedTeacher(paymentDay, hourlyWage, academicWage, name, surname,
                phone, city, email, picture, document, comment, languages);
    }

    public static PaymentDayStep stepBuilder() {
        return builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public SuppliedTeacher(Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                           String surname, String phone, String city, String email, String picture, String document,
                           String comment, List<String> languages) {
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
        if (!(o instanceof SuppliedTeacher)) return false;
        SuppliedTeacher that = (SuppliedTeacher) o;
        return Objects.equals(paymentDay, that.paymentDay) &&
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
        return Objects.hash(paymentDay, Scaleless.bigDecimal(hourlyWage), Scaleless.bigDecimal(hourlyWage),
                name, surname, phone, city, email, picture, document, comment, languages);
    }

    // BUILDER

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
        SuppliedTeacher build();
    }


    public static class Builder implements PaymentDayStep, HourlyWageStep, AcademicWageStep, NameStep,
            SurnameStep, PhoneStep, CityStep, EmailStep, PictureStep, DocumentStep, CommentStep, LanguagesStep, BuildStep {
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
        public SuppliedTeacher build() {
            return new SuppliedTeacher(
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
