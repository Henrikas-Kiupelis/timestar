package com.superum.api.teacher;

import com.superum.helper.field.FieldDefinition;
import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.helper.validation.Validator.validate;

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
 * When returning an instance of FullTeacher with JSON, these fields will be present:
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
public final class ValidTeacher extends MappedClass<ValidTeacher, Integer> {

    // CONSTRUCTORS

    public ValidTeacher(FullTeacherDTO fullTeacherDTO) {
        validate(fullTeacherDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidTeacherException("Teacher id can't be negative: " + fullTeacherDTO.getId()));

        validate(fullTeacherDTO.getPaymentDay()).Null().or().dayOfMonth()
                .ifInvalid(() -> new InvalidTeacherException("Such payment day for teacher is impossible: " + fullTeacherDTO.getPaymentDay()));

        validate(fullTeacherDTO.getHourlyWage()).Null().or().positive()
                .ifInvalid(() -> new InvalidTeacherException("Hourly wage for teacher must be positive, not " + fullTeacherDTO.getHourlyWage()));

        validate(fullTeacherDTO.getAcademicWage()).Null().or().positive()
                .ifInvalid(() -> new InvalidTeacherException("Academic wage for teacher must be positive, not " + fullTeacherDTO.getAcademicWage()));

        validate(fullTeacherDTO.getName()).Null().or().not().blank().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher name must not exceed " +
                        NAME_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getName()));

        validate(fullTeacherDTO.getSurname()).Null().or().not().blank().fits(SURNAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher surname must not exceed " +
                        SURNAME_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getSurname()));

        validate(fullTeacherDTO.getPhone()).Null().or().not().blank().fits(PHONE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher phone must not exceed " +
                        PHONE_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getPhone()));

        validate(fullTeacherDTO.getCity()).Null().or().not().blank().fits(CITY_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher city must not exceed " +
                        CITY_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getCity()));

        validate(fullTeacherDTO.getEmail()).Null().or().not().blank().fits(EMAIL_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher email must not exceed " +
                        EMAIL_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getEmail()));

        validate(fullTeacherDTO.getPicture()).Null().or().fits(PICTURE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher picture must not exceed " +
                        PICTURE_SIZE_LIMIT + " chars: " + fullTeacherDTO.getPicture()));

        validate(fullTeacherDTO.getDocument()).Null().or().fits(DOCUMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher document must not exceed " +
                        DOCUMENT_SIZE_LIMIT + " chars: " + fullTeacherDTO.getDocument()));

        validate(fullTeacherDTO.getComment()).Null().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + fullTeacherDTO.getComment()));

        this.fullTeacherDTO = fullTeacherDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final FullTeacherDTO fullTeacherDTO;

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int SURNAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int CITY_SIZE_LIMIT = 30;
    private static final int EMAIL_SIZE_LIMIT = 30;
    private static final int PICTURE_SIZE_LIMIT = 100;
    private static final int DOCUMENT_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;

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

    // FIELD DEFINITIONS

    private static final List<FieldDef<ValidTeacher, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDefinition.steps(ValidTeacher.class, Integer.class)
                    .fieldName(ID_FIELD).tableField(TEACHER.ID)
                    .getter(teacher -> teacher.fullTeacherDTO.getId())
                    .primaryKey(),

            FieldDefinition.steps(ValidTeacher.class, Integer.class)
                    .fieldName(PAYMENT_DAY_FIELD).tableField(TEACHER.PAYMENT_DAY)
                    .getter(teacher -> teacher.fullTeacherDTO.getPaymentDay())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, BigDecimal.class)
                    .fieldName(HOURLY_WAGE_FIELD).tableField(TEACHER.HOURLY_WAGE)
                    .getter(teacher -> teacher.fullTeacherDTO.getHourlyWage())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, BigDecimal.class)
                    .fieldName(ACADEMIC_WAGE_FIELD).tableField(TEACHER.ACADEMIC_WAGE)
                    .getter(teacher -> teacher.fullTeacherDTO.getAcademicWage())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(NAME_FIELD).tableField(TEACHER.NAME)
                    .getter(teacher -> teacher.fullTeacherDTO.getName())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(SURNAME_FIELD).tableField(TEACHER.SURNAME)
                    .getter(teacher -> teacher.fullTeacherDTO.getSurname())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(PHONE_FIELD).tableField(TEACHER.PHONE)
                    .getter(teacher -> teacher.fullTeacherDTO.getPhone())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(CITY_FIELD).tableField(TEACHER.CITY)
                    .getter(teacher -> teacher.fullTeacherDTO.getCity())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(EMAIL_FIELD).tableField(TEACHER.EMAIL)
                    .getter(teacher -> teacher.fullTeacherDTO.getEmail())
                    .mandatory(),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(PICTURE_FIELD).tableField(TEACHER.PICTURE)
                    .getter(teacher -> teacher.fullTeacherDTO.getPicture()),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(DOCUMENT_FIELD).tableField(TEACHER.DOCUMENT)
                    .getter(teacher -> teacher.fullTeacherDTO.getDocument()),

            FieldDefinition.steps(ValidTeacher.class, String.class)
                    .fieldName(COMMENT_FIELD).tableField(TEACHER.COMMENT)
                    .getter(teacher -> teacher.fullTeacherDTO.getComment())

    );

}
