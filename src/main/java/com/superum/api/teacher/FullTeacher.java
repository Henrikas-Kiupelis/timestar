package com.superum.api.teacher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.helper.fields.AbstractFullClassWithTimestamps;
import com.superum.helper.fields.core.Field;
import com.superum.helper.fields.core.Mandatory;
import com.superum.helper.fields.core.NamedField;
import com.superum.helper.fields.core.SimpleField;
import com.superum.helper.fields.primitives.IntField;
import com.superum.helper.jooq.SetFieldComparator;
import org.joda.time.Instant;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.superum.helper.utils.ValidationUtils.*;

/**
 * <pre>
 * Contains all information about a teacher in one place
 *
 * When creating an instance of FullTeacher with JSON, these fields are required:
 *      FIELD_NAME   : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *
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
 * If you omit a field, it will assume default value (null for objects, 0/false for primitives),
 * all of which are assumed to be allowed unless stated otherwise (check FIELD_CONSTRAINTS)
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
 * When returning an instance of FullCustomer with JSON, these fields will be present:
 *      FIELD_NAME   : FIELD_DESCRIPTION
 *      id           : number representation of this teacher in the system
 *      paymentDay   : day, at which the teacher is to be paid
 *      hourlyWage   : the amount of euros paid per hour
 *      academicWage : the amount of euros paid per academic hour; this is not necessarily equal to 3/4 * hourlyWage!
 *      name         : name
 *      surname      : surname
 *      phone        : phone number
 *      city         : city
 *      email        : email
 *      languages    : list of languages the teacher teaches; uses codes, such as "ENG" to identity a language
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
public class FullTeacher extends AbstractFullClassWithTimestamps {

    @JsonProperty(ID_FIELD)
    public int getId() {
        return id.intValue();
    }
    @JsonIgnore
    public boolean hasId() {
        return id.isSet();
    }
    /**
     * <pre>
     * Intended to be used when the id field is not set yet, and is retrieved from the database
     * </pre>
     * @return a copy of this FullTeacher, with its id replaced by the provided one
     */
    @JsonIgnore
    public FullTeacher withId(int id) {
        return valueOf(id, getPaymentDay(), getHourlyWage(), getAcademicWage(), getName(), getSurname(), getPhone(),
                getCity(), getEmail(), getPicture(), getDocument(), getComment(), getLanguages(), getCreatedAt(), getUpdatedAt());
    }
    /**
     * <pre>
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * </pre>
     * @return a copy of this FullTeacher, with its id no longer set
     */
    @JsonIgnore
    public FullTeacher withoutId() {
        return valueOf(0, getPaymentDay(), getHourlyWage(), getAcademicWage(), getName(), getSurname(), getPhone(),
                getCity(), getEmail(), getPicture(), getDocument(), getComment(), getLanguages(), getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(PAYMENT_DAY_FIELD)
    public int getPaymentDay() {
        return paymentDay.intValue();
    }
    @JsonIgnore
    public boolean hasPaymentDay() {
        return paymentDay.isSet();
    }

    @JsonProperty(HOURLY_WAGE_FIELD)
    public BigDecimal getHourlyWage() {
        return hourlyWage.getValue();
    }
    @JsonIgnore
    public boolean hasHourlyWage() {
        return hourlyWage.isSet();
    }

    @JsonProperty(ACADEMIC_WAGE_FIELD)
    public BigDecimal getAcademicWage() {
        return academicWage.getValue();
    }
    @JsonIgnore
    public boolean hasAcademicWage() {
        return academicWage.isSet();
    }

    @JsonProperty(NAME_FIELD)
    public String getName() {
        return name.getValue();
    }
    @JsonIgnore
    public boolean hasName() {
        return name.isSet();
    }

    @JsonProperty(SURNAME_FIELD)
    public String getSurname() {
        return surname.getValue();
    }
    @JsonIgnore
    public boolean hasSurname() {
        return surname.isSet();
    }

    @JsonProperty(PHONE_FIELD)
    public String getPhone() {
        return phone.getValue();
    }
    @JsonIgnore
    public boolean hasPhone() {
        return phone.isSet();
    }

    @JsonProperty(CITY_FIELD)
    public String getCity() {
        return city.getValue();
    }
    @JsonIgnore
    public boolean hasCity() {
        return city.isSet();
    }

    @JsonProperty(EMAIL_FIELD)
    public String getEmail() {
        return email.getValue();
    }
    @JsonIgnore
    public boolean hasEmail() {
        return email.isSet();
    }

    @JsonProperty(PICTURE_FIELD)
    public String getPicture() {
        return picture.getValue();
    }
    @JsonIgnore
    public boolean hasPicture() {
        return picture.isSet();
    }

    @JsonProperty(DOCUMENT_FIELD)
    public String getDocument() {
        return document.getValue();
    }
    @JsonIgnore
    public boolean hasDocument() {
        return document.isSet();
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
     * @return true if at least one Teacher field, other than id is set; false otherwise
     */
    @JsonIgnore
    public boolean canUpdateTeacher() {
        return teacherFields()
                .filter(field -> field != id)
                .anyMatch(Field::isSet);
    }

    /**
     * @return true if at least one TeacherLanguages field, other than id is set; false otherwise
     */
    @JsonIgnore
    public boolean canUpdateTeacherLanguages() {
        return hasLanguages();
    }

    /**
     * <pre>
     * Takes every Teacher field that is set in this FullTeacher and checks, if they are equal to the appropriate
     * fields of given FullTeacher
     *
     * Intended to be used during updating, to avoid making a DB query if the fields already have appropriate values
     * </pre>
     * @return true if all the set Teacher fields of this FullTeacher are equal to the given FullTeacher's; false otherwise
     */
    public boolean hasEqualSetTeacherFields(FullTeacher other) {
        return setFieldComparator.compare(other, FullTeacher::teacherFields);
    }

    /**
     * <pre>
     * Takes every TeacherLanguages field that is set in this FullTeacher and checks, if they are equal to the appropriate
     * fields of given FullTeacher
     *
     * Intended to be used during updating, to avoid making a DB query if the fields already have appropriate values
     * </pre>
     * @return true if all the set TeacherLanguages fields of this FullTeacher are equal to the given FullTeacher's; false otherwise
     */
    public boolean hasEqualSetTeacherLanguagesFields(FullTeacher other) {
        return setFieldComparator.compare(other, FullTeacher::teacherLanguagesFields);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return "FullTeacher{" + super.toString() + "}";
    }

    // CONSTRUCTORS

    @JsonCreator
    public static FullTeacher valueOf(@JsonProperty(ID_FIELD) int id,
                                      @JsonProperty(PAYMENT_DAY_FIELD) int paymentDay,
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
        return valueOf(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture, document, comment, languages, null, null);
    }

    public static FullTeacher valueOf(int id, int paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                       String surname, String phone, String city, String email, String picture, String document,
                       String comment, List<String> languages, Instant createdAt, Instant updatedAt) {
        // Equivalent to (id != 0 && id <= 0); when id == 0, it simply was not set, so the state is valid, while id is not
        if (id < 0)
            throw new InvalidTeacherException("Customer id must be positive.");

        IntField idField  = new IntField(ID_FIELD, id, Mandatory.NO);

        if (paymentDay != 0 && !isDayOfMonth(paymentDay))
            throw new InvalidTeacherException("Such payment day for teacher is impossible: " + paymentDay);

        IntField paymentDayField = new IntField(PAYMENT_DAY_FIELD, paymentDay, Mandatory.YES);

        if (!isPositiveOrNull(hourlyWage))
            throw new InvalidTeacherException("Hourly wage for teacher must be positive, not " + hourlyWage);

        NamedField<BigDecimal> hourlyWageField = SimpleField.valueOf(HOURLY_WAGE_FIELD, hourlyWage, Mandatory.YES);

        if (!isPositiveOrNull(academicWage))
            throw new InvalidTeacherException("Academic wage for teacher must be positive, not " + academicWage);

        NamedField<BigDecimal> academicWageField = SimpleField.valueOf(ACADEMIC_WAGE_FIELD, academicWage, Mandatory.YES);

        if(!fitsOrNullNotEmpty(NAME_SIZE_LIMIT, name))
            throw new InvalidTeacherException("Teacher name must not exceed " + NAME_SIZE_LIMIT + " chars");

        NamedField<String> nameField = SimpleField.valueOf(NAME_FIELD, name, Mandatory.YES);

        if(!fitsOrNullNotEmpty(SURNAME_SIZE_LIMIT, surname))
            throw new InvalidTeacherException("Teacher surname must not exceed " + SURNAME_SIZE_LIMIT + " chars");

        NamedField<String> surnameField = SimpleField.valueOf(SURNAME_FIELD, surname, Mandatory.YES);

        if(!fitsOrNullNotEmpty(PHONE_SIZE_LIMIT, phone))
            throw new InvalidTeacherException("Teacher phone must not exceed " + PHONE_SIZE_LIMIT + " chars");

        NamedField<String> phoneField = SimpleField.valueOf(PHONE_FIELD, phone, Mandatory.YES);

        if(!fitsOrNullNotEmpty(CITY_SIZE_LIMIT, city))
            throw new InvalidTeacherException("Teacher city must not exceed " + CITY_SIZE_LIMIT + " chars");

        NamedField<String> cityField = SimpleField.valueOf(CITY_FIELD, city, Mandatory.YES);

        if(!fitsOrNullNotEmpty(EMAIL_SIZE_LIMIT, email))
            throw new InvalidTeacherException("Teacher email must not exceed " + EMAIL_SIZE_LIMIT + " chars");

        NamedField<String> emailField = SimpleField.valueOf(EMAIL_FIELD, email, Mandatory.YES);

        if(!fitsOrNull(PICTURE_SIZE_LIMIT, picture))
            throw new InvalidTeacherException("Teacher picture must not exceed " + PICTURE_SIZE_LIMIT + " chars");

        NamedField<String> pictureField = SimpleField.valueOf(PICTURE_FIELD, picture, Mandatory.NO);

        if(!fitsOrNull(DOCUMENT_SIZE_LIMIT, document))
            throw new InvalidTeacherException("Teacher document must not exceed " + DOCUMENT_SIZE_LIMIT + " chars");

        NamedField<String> documentField = SimpleField.valueOf(DOCUMENT_FIELD, document, Mandatory.NO);

        if(!fitsOrNull(COMMENT_SIZE_LIMIT, comment))
            throw new InvalidTeacherException("Teacher comment must not exceed " + COMMENT_SIZE_LIMIT + " chars");

        NamedField<String> commentField = SimpleField.valueOf(COMMENT_FIELD, comment, Mandatory.NO);

        if (languages != null && !languages.stream().allMatch(languageCode -> fitsNotNullNotEmpty(LANGUAGE_CODE_SIZE_LIMIT, languageCode)))
            throw new InvalidTeacherException("Teacher languages must not be null or exceed " + LANGUAGE_CODE_SIZE_LIMIT + " chars");

        NamedField<List<String>> languagesField = SimpleField.immutableValueOf(LANGUAGES_FIELD, languages, Mandatory.YES);

        List<NamedField> allFields = ImmutableList.of(idField, paymentDayField, hourlyWageField,
                academicWageField, nameField, surnameField, phoneField, cityField, emailField, pictureField,
                documentField, commentField, languagesField);
        return new FullTeacher(allFields, createdAt, updatedAt, idField, paymentDayField, hourlyWageField,
                academicWageField, nameField, surnameField, phoneField, cityField, emailField, pictureField,
                documentField, commentField, languagesField);
    }

    public static FullTeacher valueOf(Teacher teacher, TeacherLanguages languages) {
        return valueOf(teacher, languages.getLanguages());
    }

    public static FullTeacher valueOf(Teacher teacher, List<String> languages) {
        return valueOf(teacher.getId(), teacher.getPaymentDay(), teacher.getHourlyWage(), teacher.getAcademicWage(),
                teacher.getName(), teacher.getSurname(), teacher.getPhone(), teacher.getCity(), teacher.getEmail(),
                teacher.getPicture(), teacher.getDocument(), teacher.getComment(), languages, teacher.getCreatedAt(),
                teacher.getUpdatedAt());
    }

    public static FullTeacher valueOf(Record record) {
        return null; // TODO - implement valueOf for JOOQ record
    }

    /**
     * <pre>
     * Intended for updating
     * </pre>
     * @return a new builder which can be used to make any kind of FullTeacher
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * <pre>
     * Intended for creating
     * </pre>
     * @return a new builder which creates a FullTeacher that has all its mandatory fields set
     */
    public static PaymentDayStep stepBuilder() {
        return new Builder();
    }

    private FullTeacher(List<NamedField> allFields, Instant createdAt, Instant updatedAt, IntField id,
                        IntField paymentDay, NamedField<BigDecimal> hourlyWage, NamedField<BigDecimal> academicWage,
                        NamedField<String> name, NamedField<String> surname, NamedField<String> phone,
                        NamedField<String> city, NamedField<String> email, NamedField<String> picture,
                        NamedField<String> document, NamedField<String> comment, NamedField<List<String>> languages) {
        super(allFields, createdAt, updatedAt);

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
        this.languages = languages;

        this.setFieldComparator = new SetFieldComparator<>(this);
    }

    // PRIVATE

    private final IntField id;
    private final IntField paymentDay;
    private final NamedField<BigDecimal> hourlyWage;
    private final NamedField<BigDecimal> academicWage;
    private final NamedField<String> name;
    private final NamedField<String> surname;
    private final NamedField<String> phone;
    private final NamedField<String> city;
    private final NamedField<String> email;
    private final NamedField<String> picture;
    private final NamedField<String> document;
    private final NamedField<String> comment;
    private final NamedField<List<String>> languages;

    private final SetFieldComparator<FullTeacher> setFieldComparator;

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all Teacher fields
     */
    private Stream<NamedField> teacherFields() {
        return allFields().filter(field -> field != languages);
    }

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all TeacherLanguages fields
     */
    private Stream<NamedField> teacherLanguagesFields() {
        return Arrays.<NamedField>asList(id, languages).stream();
    }

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int SURNAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int CITY_SIZE_LIMIT = 30;
    private static final int EMAIL_SIZE_LIMIT = 30;
    private static final int PICTURE_SIZE_LIMIT = 100;
    private static final int DOCUMENT_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;
    private static final int LANGUAGE_CODE_SIZE_LIMIT = 3;

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

    // GENERATED

    public interface PaymentDayStep {
        HourlyWageStep paymentDay(int paymentDay);
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
        BuildStep picture(String picture);
        BuildStep document(String document);
        BuildStep comment(String comment);
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        FullTeacher build();
    }

    public static final class Builder implements PaymentDayStep, HourlyWageStep, AcademicWageStep, NameStep,
            SurnameStep, PhoneStep, CityStep, EmailStep, LanguagesStep, BuildStep {
        private int id;
        private int paymentDay;
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

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder paymentDay(int paymentDay) {
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

        public FullTeacher build() {
            return valueOf(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email,
                    picture, document, comment, languages, createdAt, updatedAt);
        }

    }
}
