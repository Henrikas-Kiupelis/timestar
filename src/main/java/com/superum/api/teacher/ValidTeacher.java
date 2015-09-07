package com.superum.api.teacher;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.validation.Validate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.TEACHER;


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
        Validate.Int(fullTeacherDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidTeacherException("Teacher id can't be negative: " + fullTeacherDTO.getId()));

        Validate.Int(fullTeacherDTO.getPaymentDay()).Null().or().dayOfMonth()
                .ifInvalid(() -> new InvalidTeacherException("Such payment day for teacher is impossible: " + fullTeacherDTO.getPaymentDay()));

        Validate.bigDecimal(fullTeacherDTO.getHourlyWage()).Null().or().positive()
                .ifInvalid(() -> new InvalidTeacherException("Hourly wage for teacher must be positive, not " + fullTeacherDTO.getHourlyWage()));

        Validate.bigDecimal(fullTeacherDTO.getAcademicWage()).Null().or().positive()
                .ifInvalid(() -> new InvalidTeacherException("Academic wage for teacher must be positive, not " + fullTeacherDTO.getAcademicWage()));

        Validate.string(fullTeacherDTO.getName()).Null().or().not().blank().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher name must not exceed " +
                        NAME_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getName()));

        Validate.string(fullTeacherDTO.getSurname()).Null().or().not().blank().fits(SURNAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher surname must not exceed " +
                        SURNAME_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getSurname()));

        Validate.string(fullTeacherDTO.getPhone()).Null().or().not().blank().fits(PHONE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher phone must not exceed " +
                        PHONE_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getPhone()));

        Validate.string(fullTeacherDTO.getCity()).Null().or().not().blank().fits(CITY_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher city must not exceed " +
                        CITY_SIZE_LIMIT + " chars or be blank: " + fullTeacherDTO.getCity()));

        Validate.string(fullTeacherDTO.getEmail()).Null().or().not().blank().fits(EMAIL_SIZE_LIMIT).email()
                .ifInvalid(() -> new InvalidTeacherException("Teacher email must not exceed " +
                        EMAIL_SIZE_LIMIT + " chars, be blank or be of invalid format: " +
                        fullTeacherDTO.getEmail()));

        Validate.string(fullTeacherDTO.getPicture()).Null().or().fits(PICTURE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher picture must not exceed " +
                        PICTURE_SIZE_LIMIT + " chars: " + fullTeacherDTO.getPicture()));

        Validate.string(fullTeacherDTO.getDocument()).Null().or().fits(DOCUMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidTeacherException("Teacher document must not exceed " +
                        DOCUMENT_SIZE_LIMIT + " chars: " + fullTeacherDTO.getDocument()));

        Validate.string(fullTeacherDTO.getComment()).Null().or().fits(COMMENT_SIZE_LIMIT)
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
