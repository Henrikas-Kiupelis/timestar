package com.superum.api.v2.teacher;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.v2.validate.Validate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.superum.api.core.CommonValidators.*;
import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static timestar_v2.Tables.TEACHER;

/**
 * <pre>
 * Domain object for teachers
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public final class ValidTeacher extends MappedClass<ValidTeacher, Integer> {

    // CONSTRUCTORS

    public ValidTeacher(FullTeacherDTO fullTeacherDTO) {
        OPTIONAL_JSON_ID.ifInvalid(fullTeacherDTO.getId())
                .thenThrow(id -> new InvalidTeacherException("Teacher id can't be negative: " + id));

        Validate.integer().isNull().or().isDayOfMonth().ifInvalid(fullTeacherDTO.getPaymentDay())
                .thenThrow(day -> new InvalidTeacherException("Such payment day for teacher is impossible: " + day));

        Validate.bigDecimal().isNull().or().isPositive().ifInvalid(fullTeacherDTO.getHourlyWage())
                .thenThrow(wage -> new InvalidTeacherException("Hourly wage for teacher must be positive, not " + wage));

        Validate.bigDecimal().isNull().or().isPositive().ifInvalid(fullTeacherDTO.getAcademicWage())
                .thenThrow(wage -> new InvalidTeacherException("Academic wage for teacher must be positive, not " + wage));

        OPTIONAL_JSON_STRING.ifInvalid(fullTeacherDTO.getName())
                .thenThrow(name -> new InvalidTeacherException("Teacher name must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + name));

        OPTIONAL_JSON_STRING.ifInvalid(fullTeacherDTO.getSurname())
                .thenThrow(surname -> new InvalidTeacherException("Teacher surname must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + surname));

        OPTIONAL_JSON_STRING.ifInvalid(fullTeacherDTO.getPhone())
                .thenThrow(phone -> new InvalidTeacherException("Teacher phone must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + phone));

        OPTIONAL_JSON_STRING.ifInvalid(fullTeacherDTO.getCity())
                .thenThrow(city -> new InvalidTeacherException("Teacher city must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + city));

        OPTIONAL_JSON_STRING.and().isEmail().ifInvalid(fullTeacherDTO.getEmail())
                .thenThrow(email -> new InvalidTeacherException("Teacher email must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars, be blank or be of invalid format: " + email));

        OPTIONAL_JSON_STRING_BLANK_ABLE.ifInvalid(fullTeacherDTO.getPicture())
                .thenThrow(pic -> new InvalidTeacherException("Teacher picture must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + pic));

        OPTIONAL_JSON_STRING_BLANK_ABLE.ifInvalid(fullTeacherDTO.getDocument())
                .thenThrow(doc -> new InvalidTeacherException("Teacher document must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + doc));

        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT).ifInvalid(fullTeacherDTO.getComment())
                .thenThrow(comment -> new InvalidTeacherException("Teacher comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + comment));

        this.fullTeacherDTO = fullTeacherDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final FullTeacherDTO fullTeacherDTO;

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
            FieldDef.steps(ValidTeacher.class, Integer.class)
                    .fieldName(ID_FIELD).tableField(TEACHER.ID)
                    .getter(teacher -> teacher.fullTeacherDTO.getId())
                    .primaryKey(),

            FieldDef.steps(ValidTeacher.class, Integer.class)
                    .fieldName(PAYMENT_DAY_FIELD).tableField(TEACHER.PAYMENT_DAY)
                    .getter(teacher -> teacher.fullTeacherDTO.getPaymentDay())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, BigDecimal.class)
                    .fieldName(HOURLY_WAGE_FIELD).tableField(TEACHER.HOURLY_WAGE)
                    .getter(teacher -> teacher.fullTeacherDTO.getHourlyWage())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, BigDecimal.class)
                    .fieldName(ACADEMIC_WAGE_FIELD).tableField(TEACHER.ACADEMIC_WAGE)
                    .getter(teacher -> teacher.fullTeacherDTO.getAcademicWage())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(NAME_FIELD).tableField(TEACHER.NAME)
                    .getter(teacher -> teacher.fullTeacherDTO.getName())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(SURNAME_FIELD).tableField(TEACHER.SURNAME)
                    .getter(teacher -> teacher.fullTeacherDTO.getSurname())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(PHONE_FIELD).tableField(TEACHER.PHONE)
                    .getter(teacher -> teacher.fullTeacherDTO.getPhone())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(CITY_FIELD).tableField(TEACHER.CITY)
                    .getter(teacher -> teacher.fullTeacherDTO.getCity())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(EMAIL_FIELD).tableField(TEACHER.EMAIL)
                    .getter(teacher -> teacher.fullTeacherDTO.getEmail())
                    .mandatory(),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(PICTURE_FIELD).tableField(TEACHER.PICTURE)
                    .getter(teacher -> teacher.fullTeacherDTO.getPicture()),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(DOCUMENT_FIELD).tableField(TEACHER.DOCUMENT)
                    .getter(teacher -> teacher.fullTeacherDTO.getDocument()),

            FieldDef.steps(ValidTeacher.class, String.class)
                    .fieldName(COMMENT_FIELD).tableField(TEACHER.COMMENT)
                    .getter(teacher -> teacher.fullTeacherDTO.getComment())

    );

}
