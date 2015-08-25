package com.superum.api.customer;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;
import com.superum.helper.time.JodaTimeZoneHandler;

import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Domain object for customers
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidCustomer extends MappedClass<ValidCustomer, Integer> {

    public ValidCustomer(ValidCustomerDTO validCustomerDTO) {
        validate(validCustomerDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidCustomerException("Customer id must be positive, not: "+
                        validCustomerDTO.getId()));

        validate(validCustomerDTO.getName()).Null().or().not().blank().fits(NAME_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer name must not exceed " +
                        NAME_SIZE_LIMIT + " chars or be blank: " + validCustomerDTO.getName()));

        validate(validCustomerDTO.getPhone()).Null().or().not().blank().fits(PHONE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer phone must not exceed " +
                        PHONE_SIZE_LIMIT + " chars or be blank: " + validCustomerDTO.getPhone()));

        validate(validCustomerDTO.getWebsite()).Null().or().not().blank().fits(WEBSITE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer website must not exceed " +
                        WEBSITE_SIZE_LIMIT + " chars or be blank: " + validCustomerDTO.getWebsite()));

        validate(validCustomerDTO.getPicture()).Null().or().fits(PICTURE_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer picture must not exceed " +
                        PICTURE_SIZE_LIMIT + " chars: " + validCustomerDTO.getPicture()));

        validate(validCustomerDTO.getComment()).Null().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Customer comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + validCustomerDTO.getComment()));

        this.validCustomerDTO = validCustomerDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final ValidCustomerDTO validCustomerDTO;

    private static final int NAME_SIZE_LIMIT = 30;
    private static final int PHONE_SIZE_LIMIT = 30;
    private static final int WEBSITE_SIZE_LIMIT = 30;
    private static final int PICTURE_SIZE_LIMIT = 100;
    private static final int COMMENT_SIZE_LIMIT = 500;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String START_DATE_FIELD = "startDate";
    private static final String NAME_FIELD = "name";
    private static final String PHONE_FIELD = "phone";
    private static final String WEBSITE_FIELD = "website";
    private static final String PICTURE_FIELD = "picture";
    private static final String COMMENT_FIELD = "comment";

    // FIELD DEFINITIONS

    private static final List<FieldDef<ValidCustomer, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDef.steps(ValidCustomer.class, Integer.class)
                    .fieldName(ID_FIELD).tableField(CUSTOMER.ID)
                    .getter(customer -> customer.validCustomerDTO.getId())
                    .primaryKey(),

            FieldDef.steps(ValidCustomer.class, java.sql.Date.class)
                    .fieldName(START_DATE_FIELD).tableField(CUSTOMER.START_DATE)
                    .getter(customer -> customer.validCustomerDTO.getStartDate(),
                            date -> JodaTimeZoneHandler.getDefault().from(date).toJavaSqlDate())
                    .mandatory(),

            FieldDef.steps(ValidCustomer.class, String.class)
                    .fieldName(NAME_FIELD).tableField(CUSTOMER.NAME)
                    .getter(customer -> customer.validCustomerDTO.getName())
                    .mandatory(),

            FieldDef.steps(ValidCustomer.class, String.class)
                    .fieldName(PHONE_FIELD).tableField(CUSTOMER.PHONE)
                    .getter(customer -> customer.validCustomerDTO.getPhone())
                    .mandatory(),

            FieldDef.steps(ValidCustomer.class, String.class)
                    .fieldName(WEBSITE_FIELD).tableField(CUSTOMER.WEBSITE)
                    .getter(customer -> customer.validCustomerDTO.getWebsite())
                    .mandatory(),

            FieldDef.steps(ValidCustomer.class, String.class)
                    .fieldName(PICTURE_FIELD).tableField(CUSTOMER.PICTURE)
                    .getter(customer -> customer.validCustomerDTO.getPicture()),

            FieldDef.steps(ValidCustomer.class, String.class)
                    .fieldName(COMMENT_FIELD).tableField(CUSTOMER.COMMENT)
                    .getter(customer -> customer.validCustomerDTO.getComment())

    );

}
