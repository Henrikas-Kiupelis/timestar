package com.superum.api.student;

import com.superum.helper.Random;
import com.superum.helper.field.MappedClass;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.libraries.jodatime.Time;
import eu.goodlike.validation.Validate;
import org.jooq.lambda.Seq;

import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.STUDENT;


/**
 * <pre>
 * Domain object for students
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidStudent extends MappedClass<ValidStudent, Integer> {

    @Override
    public Seq<MappedField<?>> createFields() {
        return super.createFields().concat(CODE_FIELD_DEF.toField(this));
    }

    public boolean hasNeitherCustomerIdNorStartDate() {
        return validStudentDTO.getCustomerId() == null && validStudentDTO.getStartDate() == null;
    }

    public boolean hasBothCustomerAndStartDate() {
        return validStudentDTO.getCustomerId() != null && validStudentDTO.getStartDate() != null;
    }

    public static int generateCode() {
        return Random.numberWithDigits(6);
    }

    // CONSTRUCTORS

    public ValidStudent(ValidStudentDTO validStudentDTO) {
        Validate.Int(validStudentDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidStudentException("Student id must be positive, not: " +
                        validStudentDTO.getId()));

        Validate.Int(validStudentDTO.getCustomerId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidStudentException("Customer id for student must be positive, not: " +
                        validStudentDTO.getCustomerId()));

        Validate.string(validStudentDTO.getEmail()).Null().or().not().blank().fits(EMAIL_SIZE_LIMIT).email()
                .ifInvalid(() -> new InvalidStudentException("Student email must not exceed " +
                        EMAIL_SIZE_LIMIT + " chars, be blank or be of invalid format: " +
                        validStudentDTO.getEmail()));

        Validate.string(validStudentDTO.getName()).Null().or().not().blank().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidStudentException("Student name must not exceed " +
                        NAME_SIZE_LIMIT + " chars or be blank: " + validStudentDTO.getName()));

        this.validStudentDTO = validStudentDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final ValidStudentDTO validStudentDTO;

    private static final int EMAIL_SIZE_LIMIT = 60;
    private static final int NAME_SIZE_LIMIT = 60;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String CODE_FIELD = "code";
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String START_DATE_FIELD = "startDate";
    private static final String EMAIL_FIELD = "email";
    private static final String NAME_FIELD = "name";

    private static final FieldDef<ValidStudent, Integer> CODE_FIELD_DEF =
            FieldDef.steps(ValidStudent.class, Integer.class)
                    .fieldName(CODE_FIELD)
                    .tableField(STUDENT.CODE)
                    .getter(validStudent -> generateCode());

    private static final List<FieldDef<ValidStudent, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDef.steps(ValidStudent.class, Integer.class)
                    .fieldName(ID_FIELD)
                    .tableField(STUDENT.ID)
                    .getter(student -> student.validStudentDTO.getId())
                    .primaryKey(),

            FieldDef.steps(ValidStudent.class, Integer.class)
                    .fieldName(CUSTOMER_ID_FIELD)
                    .tableField(STUDENT.CUSTOMER_ID)
                    .getter(student -> student.validStudentDTO.getCustomerId()),

            FieldDef.steps(ValidStudent.class, java.sql.Date.class)
                    .fieldName(START_DATE_FIELD)
                    .tableField(STUDENT.START_DATE)
                    .getter(student -> student.validStudentDTO.getStartDate(),
                            date -> Time.convert(date).toSqlDate()),

            FieldDef.steps(ValidStudent.class, String.class)
                    .fieldName(EMAIL_FIELD)
                    .tableField(STUDENT.EMAIL)
                    .getter(student -> student.validStudentDTO.getEmail())
                    .mandatory(),

            FieldDef.steps(ValidStudent.class, String.class)
                    .fieldName(NAME_FIELD)
                    .tableField(STUDENT.NAME)
                    .getter(student -> student.validStudentDTO.getName())
                    .mandatory()
    );

}
