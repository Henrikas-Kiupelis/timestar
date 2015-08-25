package com.superum.api.teacher;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.superum.api.core.DTOWithTimestamps;
import com.superum.helper.Equals;
import org.joda.time.Instant;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.helper.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;

/**
 * <pre>
 * Data Transport Object for teachers
 *
 * This object is used to de-serialize and serialize JSON that is coming in and out of the back end;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When creating an instance of FullTeacher with JSON, these fields are required:
 *      FIELD_NAME   : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      paymentDay   : day, at which the teacher is to be paid                  1 <= paymentDay <= 31
 *      hourlyWage   : the amount of euros paid per hour                        0 < hourlyWage;
 *                                                                              BigDecimal of up to 4 numbers after comma
 *      academicWage : the amount of euros paid per academic hour               0 < academicWage;
 *                     this is not necessarily equal to 3/4 * hourlyWage!       BigDecimal of up to 4 numbers after comma
 *      name         : name                                                     any String, max 30 chars
 *      surname      : surname                                                  any String, max 30 chars
 *      phone        : phone number                                             any String, max 30 chars
 *      city         : city (currently unknown which city this means)           any String, max 30 chars
 *      email        : email; this is also used for account/mailing             any String, max 60 chars
 *      languages    : list of languages the teacher teaches;                   any List of Strings, max 3 chars
 *                     uses codes, such as "ENG" to identity a language
 *
 * These fields are optional:
 *      picture      : link to a picture of this teacher, stored somewhere      any String, max 100 chars
 *      document     : link to a document uploaded by the teacher               any String, max 100 chars
 *      comment      : comment, made by the app client                          any String, max 500 chars
 *
 * These fields should only be specified if they are known:
 *      id           : number representation of this teacher in the system      1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * A couple of notes in regards to certain fields:
 *  1) payment days, in the case of non existent dates, such as 31st of February, are pushed forward to the next
 *     existent day, in this case 1st of March;
 *  2) hourlyWage and academicWage are fundamentally different ways of payment; while I cannot explain why they are
 *     different, they simply are. when a teacher gets a group, one of these payment models is used to calculate
 *     how much money should be paid;
 *  3) the email field has to be unique for all teachers (in the same partition); it is used for authentication and
 *     will be the email messages are sent to (i.e. password, when generated);
 *  4) there is currently no restriction on what is a "language code", other than that it is made of 3 characters;
 *
 * Example of JSON to send:
 * {
 *      "id":1,
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
 *
 * When returning an instance of FullTeacher with JSON, these additional fields will be present:
 *      FIELD_NAME   : FIELD_DESCRIPTION
 *      createdAt    : timestamp, taken by the database at the time of creation
 *      updatedAt    : timestamp, taken by the database at the time of creation and updating
 *
 * Example of JSON to expect:
 * {
 *      "id":1,
 *      "paymentDay":1,
 *      "hourlyWage":23.0000,
 *      "academicWage":20.0000,
 *      "name":"Henrikas",
 *      "surname":"Kiūpelis",
 *      "phone":"+37069900011",
 *      "city":"Vilnius",
 *      "email":"henrikas.kiupelis@gmail.com",
 *      "picture":"http://timestar.lt/uploads/henrikas.jpg",
 *      "document":"http://timestar.lt/uploads/api.doc",
 *      "comment":"What a teacher"
 *      "languages": ["ENG"]
 *      "createdAt":1438635600000,
 *      "updatedAt":1438682839418
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class FullTeacherDTO extends DTOWithTimestamps {

    @JsonProperty(ID_FIELD)
    public Integer getId() {
        return id;
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullTeacher, with its id no longer set
     */
    @JsonIgnore
    public FullTeacherDTO withoutId() {
        return new FullTeacherDTO(null, paymentDay, hourlyWage, academicWage, name, surname, phone, city,
                email, picture, document, comment, languages, getCreatedAt(), getUpdatedAt());
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullTeacher, with its id replaced by the provided one
     */
    @JsonIgnore
    public FullTeacherDTO withId(Integer id) {
        return new FullTeacherDTO(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city,
                email, picture, document, comment, languages, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(PAYMENT_DAY_FIELD)
    public Integer getPaymentDay() {
        return paymentDay;
    }

    @JsonProperty(HOURLY_WAGE_FIELD)
    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    @JsonProperty(ACADEMIC_WAGE_FIELD)
    public BigDecimal getAcademicWage() {
        return academicWage;
    }

    @JsonProperty(NAME_FIELD)
    public String getName() {
        return name;
    }

    @JsonProperty(SURNAME_FIELD)
    public String getSurname() {
        return surname;
    }

    @JsonProperty(PHONE_FIELD)
    public String getPhone() {
        return phone;
    }

    @JsonProperty(CITY_FIELD)
    public String getCity() {
        return city;
    }

    @JsonProperty(EMAIL_FIELD)
    public String getEmail() {
        return email;
    }

    @JsonProperty(PICTURE_FIELD)
    public String getPicture() {
        return picture;
    }

    @JsonProperty(DOCUMENT_FIELD)
    public String getDocument() {
        return document;
    }

    @JsonProperty(COMMENT_FIELD)
    public String getComment() {
        return comment;
    }

    @JsonProperty(LANGUAGES_FIELD)
    public List<String> getLanguages() {
        return languages;
    }

    @JsonIgnore
    public static String getLanguagesFieldName() {
        return LANGUAGES_FIELD;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static FullTeacherDTO jsonInstance(@JsonProperty(ID_FIELD) Integer id,
                                      @JsonProperty(PAYMENT_DAY_FIELD) Integer paymentDay,
                                      @JsonProperty(HOURLY_WAGE_FIELD) BigDecimal hourlyWage,
                                      @JsonProperty(ACADEMIC_WAGE_FIELD) BigDecimal academicWage,
                                      @JsonProperty(NAME_FIELD) String name,
                                      @JsonProperty(SURNAME_FIELD) String surname,
                                      @JsonProperty(PHONE_FIELD) String phone,
                                      @JsonProperty(CITY_FIELD) String city,
                                      @JsonProperty(EMAIL_FIELD) String email,
                                      @JsonProperty(PICTURE_FIELD) String picture,
                                      @JsonProperty(DOCUMENT_FIELD) String document,
                                      @JsonProperty(COMMENT_FIELD) String comment,
                                      @JsonProperty(LANGUAGES_FIELD) List<String> languages) {
        return new FullTeacherDTO(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages, null, null);
    }

    public static FullTeacherDTO valueOf(Record record) {
        if (record == null)
            return null;

        String[] languages = record.getValue(LANGUAGES_FIELD, String.class).split(MYSQL_GROUP_CONCAT_SEPARATOR);

        return FullTeacherDTO.stepBuilder()
                .paymentDay(record.getValue(TEACHER.PAYMENT_DAY))
                .hourlyWage(record.getValue(TEACHER.HOURLY_WAGE))
                .academicWage(record.getValue(TEACHER.ACADEMIC_WAGE))
                .name(record.getValue(TEACHER.NAME))
                .surname(record.getValue(TEACHER.SURNAME))
                .phone(record.getValue(TEACHER.PHONE))
                .city(record.getValue(TEACHER.CITY))
                .email(record.getValue(TEACHER.EMAIL))
                .languages(languages)
                .id(record.getValue(TEACHER.ID))
                .picture(record.getValue(TEACHER.PICTURE))
                .document(record.getValue(TEACHER.DOCUMENT))
                .comment(record.getValue(TEACHER.COMMENT))
                .createdAt(record.getValue(TEACHER.CREATED_AT))
                .updatedAt(record.getValue(TEACHER.UPDATED_AT))
                .build();
    }

    public FullTeacherDTO(Integer id, Integer paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                           String surname, String phone, String city, String email, String picture, String document,
                           String comment, List<String> languages, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);

        this.id = id;
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
        this.languages = languages == null ? null : ImmutableList.copyOf(languages);
    }

    /**
     * Intended for updating
     * @return a new builder which can be used to make any kind of FullTeacher
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Intended for creating
     * @return a new builder which creates a FullTeacher that has all its mandatory fields set
     */
    public static PaymentDayStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

    private final Integer id;
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

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String PAYMENT_DAY_FIELD = "paymentDay";
    private static final String HOURLY_WAGE_FIELD = "hourlyWage";
    private static final String ACADEMIC_WAGE_FIELD = "academicWage";
    private static final String NAME_FIELD = "name";
    private static final String SURNAME_FIELD = "surname";
    private static final String PHONE_FIELD = "phone";
    private static final String CITY_FIELD = "city";
    private static final String EMAIL_FIELD = "email";
    private static final String PICTURE_FIELD = "picture";
    private static final String DOCUMENT_FIELD = "document";
    private static final String COMMENT_FIELD = "comment";
    private static final String LANGUAGES_FIELD = "languages";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("FullTeacher")
                .add(ID_FIELD, id)
                .add(PAYMENT_DAY_FIELD, paymentDay)
                .add(HOURLY_WAGE_FIELD, hourlyWage)
                .add(ACADEMIC_WAGE_FIELD, academicWage)
                .add(NAME_FIELD, name)
                .add(SURNAME_FIELD, surname)
                .add(PHONE_FIELD, phone)
                .add(CITY_FIELD, city)
                .add(EMAIL_FIELD, email)
                .add(PICTURE_FIELD, picture)
                .add(DOCUMENT_FIELD, document)
                .add(COMMENT_FIELD, comment)
                .add(LANGUAGES_FIELD, languages)
                .add(CREATED_AT_FIELD, getCreatedAt())
                .add(UPDATED_AT_FIELD, getUpdatedAt())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof FullTeacherDTO && EQUALS.equals(this, (FullTeacherDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email,
                picture, document, comment, languages);
    }

    private static final Equals<FullTeacherDTO> EQUALS = new Equals<>(Arrays.asList(FullTeacherDTO::getId,
            FullTeacherDTO::getPaymentDay, FullTeacherDTO::getHourlyWage, FullTeacherDTO::getAcademicWage,
            FullTeacherDTO::getName, FullTeacherDTO::getSurname, FullTeacherDTO::getPhone, FullTeacherDTO::getCity,
            FullTeacherDTO::getEmail, FullTeacherDTO::getPicture, FullTeacherDTO::getDocument,
            FullTeacherDTO::getComment, FullTeacherDTO::getLanguages));

    // GENERATED

    public interface PaymentDayStep {
        HourlyWageStep paymentDay(Integer paymentDay);
    }

    public interface HourlyWageStep {
        AcademicWageStep hourlyWage(BigDecimal hourlyWage);
        AcademicWageStep hourlyWage(double hourlyWage);
    }

    public interface AcademicWageStep {
        NameStep academicWage(BigDecimal academicWage);
        NameStep academicWage(double academicWage);
    }

    public interface NameStep {
        SurnameStep name(String name);
    }

    public interface SurnameStep {
        PhoneStep surname(String surname);
    }

    public interface PhoneStep {
        CityStep phone(String phone);
    }

    public interface CityStep {
        EmailStep city(String city);
    }

    public interface EmailStep {
        LanguagesStep email(String email);
    }

    public interface LanguagesStep {
        BuildStep languages(List<String> languages);
        BuildStep languages(String... languages);
    }

    public interface BuildStep {
        BuildStep id(Integer id);
        BuildStep picture(String picture);
        BuildStep document(String document);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        FullTeacherDTO build();
    }

    public static final class Builder implements PaymentDayStep, HourlyWageStep, AcademicWageStep, NameStep,
            SurnameStep, PhoneStep, CityStep, EmailStep, LanguagesStep, BuildStep {
        private Integer id;
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

        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder paymentDay(Integer paymentDay) {
            this.paymentDay = paymentDay;
            return this;
        }

        public Builder hourlyWage(BigDecimal hourlyWage) {
            this.hourlyWage = hourlyWage;
            return this;
        }

        public Builder hourlyWage(double hourlyWage) {
            this.hourlyWage = BigDecimal.valueOf(hourlyWage);
            return this;
        }

        public Builder academicWage(BigDecimal academicWage) {
            this.academicWage = academicWage;
            return this;
        }

        public Builder academicWage(double academicWage) {
            this.academicWage = BigDecimal.valueOf(academicWage);
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder document(String document) {
            this.document = document;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder languages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        public Builder languages(String... languages) {
            this.languages = Arrays.asList(languages);
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder updatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        public FullTeacherDTO build() {
            return new FullTeacherDTO(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city,
                    email, picture, document, comment, languages, createdAt, updatedAt);
        }

    }
}
