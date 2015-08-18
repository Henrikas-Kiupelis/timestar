package com.superum.api.teacher;

import com.superum.helper.field.ManyDefined;
import com.superum.helper.validation.Validator;
import org.jooq.lambda.Seq;

import java.util.List;

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
public final class ValidTeacherLanguages implements ManyDefined<Integer, String> {

    @Override
    public Integer primaryValue() {
        return id;
    }

    @Override
    public Seq<String> secondaryValues() {
        return Seq.seq(languages);
    }

    public boolean hasLanguages() {
        return languages != null;
    }

    public ValidTeacherLanguages withoutId() {
        return new ValidTeacherLanguages(null, languages);
    }

    public ValidTeacherLanguages withId(Integer id) {
        return new ValidTeacherLanguages(id, languages);
    }

    // CONSTRUCTORS

    public static ValidTeacherLanguages fromDTO(FullTeacherDTO fullTeacherDTO) {
        return new ValidTeacherLanguages(fullTeacherDTO.getId(), fullTeacherDTO.getLanguages());
    }

    private ValidTeacherLanguages(Integer id, List<String> languages) {
        validate(id).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidTeacherException("Teacher id can't be negative: " + id));

        validate(languages).Null().or().not().empty()
                .forEach(Validator::validate, language -> language.not().Null().not().blank().fits(LANGUAGE_CODE_SIZE_LIMIT)
                        .ifInvalid(() -> new InvalidTeacherException("Specific Teacher languages must not be null, blank or exceed "
                                + LANGUAGE_CODE_SIZE_LIMIT + " chars: " + language.value())))
                .ifInvalid(() -> new InvalidTeacherException("Teacher must have at least a single language!"));

        this.id = id;
        this.languages = languages;
    }

    // PRIVATE

    private final Integer id;
    private final List<String> languages;

    private static final int LANGUAGE_CODE_SIZE_LIMIT = 3;

}
