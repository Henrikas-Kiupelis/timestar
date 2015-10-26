package com.superum.api.v2.student;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.libraries.joda.time.Time;
import eu.goodlike.random.Random;
import org.jooq.lambda.Seq;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_ID;
import static com.superum.api.core.CommonValidators.OPTIONAL_JSON_STRING;
import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static timestar_v2.Tables.STUDENT;

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

    public boolean hasNonExistentCustomerId(Predicate<Integer> customerIdCheck) {
        return validStudentDTO.getCustomerId() != null && customerIdCheck.test(validStudentDTO.getCustomerId());
    }

    public static int generateCode() {
        return Random.getFast().numberWithDigits(6);
    }

    // CONSTRUCTORS

    public ValidStudent(ValidStudentDTO validStudentDTO) {
        OPTIONAL_JSON_ID.ifInvalid(validStudentDTO.getId())
                .thenThrow(id -> new InvalidStudentException("Student id must be positive, not: " + id));

        OPTIONAL_JSON_ID.ifInvalid(validStudentDTO.getCustomerId())
                .thenThrow(id -> new InvalidStudentException("Customer id for student must be positive, not: " + id));

        OPTIONAL_JSON_STRING.ifInvalid(validStudentDTO.getEmail())
                .thenThrow(email -> new InvalidStudentException("Student email must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars, be blank or be of invalid format: " + email));

        OPTIONAL_JSON_STRING.ifInvalid(validStudentDTO.getName())
                .thenThrow(name -> new InvalidStudentException("Student name must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + name));

        this.validStudentDTO = validStudentDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final ValidStudentDTO validStudentDTO;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String CODE_FIELD = "code";
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String START_DATE_FIELD = "startDate";
    private static final String EMAIL_FIELD = "email";
    private static final String NAME_FIELD = "name";

    // FIELD DEFINITIONS

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
