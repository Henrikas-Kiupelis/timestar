package com.superum.api.teacher;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.helper.field.FieldDef;
import com.superum.helper.field.FieldDefinition;
import com.superum.helper.field.FieldDefinitions;
import com.superum.helper.field.MappedClassWithTimestamps;
import com.superum.helper.field.core.Mandatory;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.core.Primary;
import com.superum.helper.validation.Validator;
import org.joda.time.Instant;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.lambda.Seq;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.db.generated.timestar.Tables.TEACHER_LANGUAGE;
import static com.superum.helper.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static com.superum.helper.validation.Validator.validate;
import static org.jooq.impl.DSL.groupConcat;

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
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class FullTeacher extends MappedClassWithTimestamps<FullTeacher, Integer> {

    @JsonProperty(ID_FIELD)
    public int getId() {
        return id;
    }
    /**
     * @return true if id field is set; false otherwise
     */
    public boolean hasId() {
        return primaryKey().isSet();
    }
    /**
     * @return true if only id field is set; false otherwise
     */
    public boolean hasOnlyId() {
        return hasId() && nonPrimaryFields().noneMatch(MappedField::isSet);
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullTeacher, with its id replaced by the provided one
     */
    @JsonIgnore
    public FullTeacher withId(int id) {
        return valueOf(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages, getCreatedAt(), getUpdatedAt());
    }
    /**
     * Intended to be used for unit/integration testing, to help emulate database behaviour
     * @return a copy of this FullTeacher, with its id no longer set
     */
    @JsonIgnore
    public FullTeacher withoutId() {
        return valueOf(0, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages, getCreatedAt(), getUpdatedAt());
    }

    @JsonProperty(PAYMENT_DAY_FIELD)
    public int getPaymentDay() {
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

    @Override
    public MappedField<Integer> primaryKey() {
        return primaryField();
    }

    @Override
    public Seq<MappedField<?>> createFields() {
        throw new UnsupportedOperationException("FullTeacher should be created by using Teacher, not directly");
    }

    @Override
    public Seq<MappedField<?>> updateFields() {
        return allNonPrimarySetFields().filter(field -> field != languagesField());
    }

    @Override
    public Seq<MappedField<?>> conditionFields() {
        return allSetFields().filter(field -> field != languagesField());
    }

    /**
     * @return true if at least one Teacher field, other than id is set; false otherwise
     */
    @JsonIgnore
    public boolean canUpdateTeacher() {
        return teacherFields()
                .filter(MappedField::isNotPrimary)
                .anyMatch(MappedField::isSet);
    }

    /**
     * @return true if at least one TeacherLanguages field, other than id is set; false otherwise
     */
    @JsonIgnore
    public boolean canUpdateTeacherLanguages() {
        return languagesField().isSet();
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
        return compare(other, FullTeacher::teacherFields);
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
        return compare(other, FullTeacher::teacherLanguagesFields);
    }

    public Teacher toTeacher() {
        return new Teacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email,
                picture, document, comment, getCreatedAt(), getUpdatedAt());
    }

    public TeacherLanguages toTeacherLanguages() {
        return new TeacherLanguages(id, languages);
    }

    public static Field<?>[] fullTeacherFields() {
        return FULL_TEACHER_FIELDS;
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

    /**
     * Main entry point for creating FullTeacher; this method should be the only one that invokes the constructor
     */
    public static FullTeacher valueOf(int id, int paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
                                      String surname, String phone, String city, String email, String picture, String document,
                                      String comment, List<String> languages, Instant createdAt, Instant updatedAt) {
        // when id == 0, it simply was not set, so the state is valid, while id is not
        validate(id).equal(0).or().moreThan(0).ifInvalid(() -> new InvalidTeacherException("Teacher id can't be negative: " + id));
        validate(paymentDay).equal(0).or().dayOfMonth().ifInvalid(() -> new InvalidTeacherException("Such payment day for teacher is impossible: " + paymentDay));
        validate(hourlyWage).isNull().or().positive().ifInvalid(() -> new InvalidTeacherException("Hourly wage for teacher must be positive, not " + hourlyWage));
        validate(academicWage).isNull().or().positive().ifInvalid(() -> new InvalidTeacherException("Academic wage for teacher must be positive, not " + academicWage));
        validate(name).isNull().or().notEmpty().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher name must not exceed " + NAME_SIZE_LIMIT + " chars or be empty: " + name));
        validate(surname).isNull().or().notEmpty().fits(SURNAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher surname must not exceed " + SURNAME_SIZE_LIMIT + " chars or be empty: " + surname));
        validate(phone).isNull().or().notEmpty().fits(PHONE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher phone must not exceed " + PHONE_SIZE_LIMIT + " chars or be empty: " + phone));
        validate(city).isNull().or().notEmpty().fits(CITY_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher city must not exceed " + CITY_SIZE_LIMIT + " chars or be empty: " + city));
        validate(email).isNull().or().notEmpty().fits(EMAIL_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher email must not exceed " + EMAIL_SIZE_LIMIT + " chars or be empty: " + email));
        validate(picture).isNull().or().fits(PICTURE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher picture must not exceed " + PICTURE_SIZE_LIMIT + " chars: " + picture));
        validate(document).isNull().or().fits(DOCUMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher document must not exceed " + DOCUMENT_SIZE_LIMIT + " chars: " + document));
        validate(comment).isNull().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher comment must not exceed " + COMMENT_SIZE_LIMIT + " chars: " + comment));
        validate(languages).isNull().or().notEmpty()
                .forEach(Validator::validate, language -> language.notNull().notEmpty().fits(LANGUAGE_CODE_SIZE_LIMIT)
                        .ifInvalid(() -> new InvalidTeacherException("Specific Teacher languages must not be null, empty or exceed "
                                + LANGUAGE_CODE_SIZE_LIMIT + " chars: " + language.value())))
                .ifInvalid(() -> new InvalidTeacherException("Teacher must have at least a single language!"));

        return new FullTeacher(id, paymentDay, hourlyWage, academicWage, name, surname, phone, city, email, picture,
                document, comment, languages, createdAt, updatedAt);
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
        if (record == null)
            return null;

        String[] languages = record.getValue(LANGUAGES_FIELD, String.class).split(MYSQL_GROUP_CONCAT_SEPARATOR);

        return FullTeacher.stepBuilder()
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

    private FullTeacher(int id, int paymentDay, BigDecimal hourlyWage, BigDecimal academicWage, String name,
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
        this.languages = ImmutableList.copyOf(languages);

        registerFields(FIELD_DEFINITIONS);
    }

    // PROTECTED

    @Override
    protected FullTeacher thisObject() {
        return this;
    }

    // PRIVATE

    private final int id;
    private final int paymentDay;
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

    /**
     * Intended to be used by other methods to reduce the filtering chain
     * @return a stream of all Teacher fields
     */
    private Seq<MappedField<?>> teacherFields() {
        return allFields().filter(field -> field != languagesField());
    }

    /**
     * Intended to be used by other methods to reduce the filtering chain
     * @return a stream of all TeacherLanguages fields
     */
    private Seq<MappedField<?>> teacherLanguagesFields() {
        return Seq.of(primaryField(), languagesField());
    }

    /**
     * @return languages field
     */
    private MappedField<?> languagesField() {
        return allFields().filter(field -> field.nameEquals(LANGUAGES_FIELD)).findAny()
                .orElseThrow(() -> new IllegalStateException("There should be only a single 'languages' field!"));
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

    private static final Field<?>[] FULL_TEACHER_FIELDS = ObjectArrays.concat(TEACHER.fields(),
            groupConcat(TEACHER_LANGUAGE.CODE).as(LANGUAGES_FIELD));

    // FIELD DEFINITIONS

    private static final List<FieldDef<FullTeacher, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDefinition.ofInt(ID_FIELD, TEACHER.ID, Mandatory.NO, Primary.YES, FullTeacher::getId),
            FieldDefinition.ofInt(PAYMENT_DAY_FIELD, TEACHER.PAYMENT_DAY, Mandatory.YES, Primary.NO, FullTeacher::getPaymentDay),
            FieldDefinition.ofBigDecimal(HOURLY_WAGE_FIELD, TEACHER.HOURLY_WAGE, Mandatory.YES, Primary.NO, FullTeacher::getHourlyWage),
            FieldDefinition.ofBigDecimal(ACADEMIC_WAGE_FIELD, TEACHER.ACADEMIC_WAGE, Mandatory.YES, Primary.NO, FullTeacher::getAcademicWage),
            FieldDefinition.of(NAME_FIELD, TEACHER.NAME, Mandatory.YES, Primary.NO, FullTeacher::getName),
            FieldDefinition.of(SURNAME_FIELD, TEACHER.SURNAME, Mandatory.YES, Primary.NO, FullTeacher::getSurname),
            FieldDefinition.of(PHONE_FIELD, TEACHER.PHONE, Mandatory.YES, Primary.NO, FullTeacher::getPhone),
            FieldDefinition.of(CITY_FIELD, TEACHER.CITY, Mandatory.YES, Primary.NO, FullTeacher::getCity),
            FieldDefinition.of(EMAIL_FIELD, TEACHER.EMAIL, Mandatory.YES, Primary.NO, FullTeacher::getEmail),
            FieldDefinition.of(PICTURE_FIELD, TEACHER.PICTURE, Mandatory.NO, Primary.NO, FullTeacher::getPicture),
            FieldDefinition.of(DOCUMENT_FIELD, TEACHER.DOCUMENT, Mandatory.NO, Primary.NO, FullTeacher::getDocument),
            FieldDefinition.of(COMMENT_FIELD, TEACHER.COMMENT, Mandatory.NO, Primary.NO, FullTeacher::getComment),
            FieldDefinition.of(LANGUAGES_FIELD, null, Mandatory.YES, Primary.NO, FullTeacher::getLanguages)
    );
    private static final FieldDefinitions<FullTeacher> FIELD_DEFINITIONS = new FieldDefinitions<>(FIELD_DEFINITION_LIST);

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
        BuildStep id(int id);
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

        @Override
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
