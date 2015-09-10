package com.superum.api.v2.group;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.validation.Validate;

import java.util.Arrays;
import java.util.List;

import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static timestar_v2.Tables.GROUP_OF_STUDENTS;

/**
 * <pre>
 * Domain object for groups
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidGroup extends MappedClass<ValidGroup, Integer> {

    // CONSTRUCTORS

    public ValidGroup(ValidGroupDTO validGroupDTO) {
        Validate.Int(validGroupDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidGroupException("Group id must be positive, not: " + validGroupDTO.getId()));

        Validate.Int(validGroupDTO.getCustomerId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidGroupException("Group's customer id must be positive, not: " + validGroupDTO.getCustomerId()));

        Validate.Int(validGroupDTO.getTeacherId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidGroupException("Group's teacher id must be positive, not: " + validGroupDTO.getTeacherId()));

        Validate.string(validGroupDTO.getLanguageLevel()).Null().or().not().blank().fits(DEFAULT_VARCHAR_FIELD_SIZE)
                .ifInvalid(() -> new InvalidGroupException("Group's language level must not be blank or exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + validGroupDTO.getLanguageLevel()));

        Validate.string(validGroupDTO.getName()).Null().or().not().blank().fits(DEFAULT_VARCHAR_FIELD_SIZE)
                .ifInvalid(() -> new InvalidGroupException("Group's name must not be blank or exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + validGroupDTO.getName()));

        this.validGroupDTO = validGroupDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final ValidGroupDTO validGroupDTO;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String USE_HOURLY_WAGE_FIELD = "usesHourlyWage";
    private static final String LANGUAGE_LEVEL_FIELD = "languageLevel";
    private static final String NAME_FIELD = "name";

    // FIELD DEFINITIONS

    private static final List<FieldDef<ValidGroup, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDef.steps(ValidGroup.class, Integer.class)
                    .fieldName(ID_FIELD)
                    .tableField(GROUP_OF_STUDENTS.ID)
                    .getter(group -> group.validGroupDTO.getId())
                    .primaryKey(),

            FieldDef.steps(ValidGroup.class, Integer.class)
                    .fieldName(CUSTOMER_ID_FIELD)
                    .tableField(GROUP_OF_STUDENTS.CUSTOMER_ID)
                    .getter(group -> group.validGroupDTO.getCustomerId()),

            FieldDef.steps(ValidGroup.class, Integer.class)
                    .fieldName(TEACHER_ID_FIELD)
                    .tableField(GROUP_OF_STUDENTS.TEACHER_ID)
                    .getter(group -> group.validGroupDTO.getTeacherId())
                    .mandatory(),

            FieldDef.steps(ValidGroup.class, Boolean.class)
                    .fieldName(USE_HOURLY_WAGE_FIELD)
                    .tableField(GROUP_OF_STUDENTS.USE_HOURLY_WAGE)
                    .getter(group -> group.validGroupDTO.getUsesHourlyWage())
                    .mandatory(),

            FieldDef.steps(ValidGroup.class, String.class)
                    .fieldName(LANGUAGE_LEVEL_FIELD)
                    .tableField(GROUP_OF_STUDENTS.LANGUAGE_LEVEL)
                    .getter(group -> group.validGroupDTO.getLanguageLevel())
                    .mandatory(),

            FieldDef.steps(ValidGroup.class, String.class)
                    .fieldName(NAME_FIELD)
                    .tableField(GROUP_OF_STUDENTS.NAME)
                    .getter(group -> group.validGroupDTO.getName())
                    .mandatory()
    );

}
