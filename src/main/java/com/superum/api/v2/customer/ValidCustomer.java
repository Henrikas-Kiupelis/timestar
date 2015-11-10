package com.superum.api.v2.customer;

import com.superum.helper.field.MappedClass;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.libraries.joda.time.Time;
import eu.goodlike.v2.validate.Validate;
import org.jooq.lambda.Seq;

import java.util.Arrays;
import java.util.List;

import static com.superum.api.core.CommonValidators.*;
import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;
import static timestar_v2.Tables.CUSTOMER;

/**
 * <pre>
 * Domain object for customers
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidCustomer extends MappedClass<ValidCustomer, Integer> {

    @Override
    public Seq<MappedField<?>> updateFields() {
        return super.updateFields().filter(field -> field.notNameEquals("createdAt"));
    }

    // CONSTRUCTORS

    public ValidCustomer(ValidCustomerDTO validCustomerDTO) {
        OPTIONAL_JSON_ID.ifInvalid(validCustomerDTO.getId())
                .thenThrow(id -> new InvalidCustomerException("Customer id must be positive, not: " + id));

        OPTIONAL_JSON_STRING.ifInvalid(validCustomerDTO.getName())
                .thenThrow(name -> new InvalidCustomerException("Customer name must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + name));

        OPTIONAL_JSON_STRING.ifInvalid(validCustomerDTO.getPhone())
                .thenThrow(phone -> new InvalidCustomerException("Customer phone must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + phone));

        OPTIONAL_JSON_STRING.ifInvalid(validCustomerDTO.getWebsite())
                .thenThrow(website -> new InvalidCustomerException("Customer website must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + website));

        OPTIONAL_JSON_STRING_BLANK_ABLE.ifInvalid(validCustomerDTO.getPicture())
                .thenThrow(picture -> new InvalidCustomerException("Customer picture must not exceed " +
                        DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + picture));

        Validate.string().isNull().or().isNoLargerThan(COMMENT_SIZE_LIMIT).ifInvalid(validCustomerDTO.getComment())
                .thenThrow(comment -> new InvalidCustomerException("Customer comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + comment));

        this.validCustomerDTO = validCustomerDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    // PRIVATE

    private final ValidCustomerDTO validCustomerDTO;

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
                            date -> Time.convert(date).toSqlDate())
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
                    .getter(customer -> customer.validCustomerDTO.getComment()),

            FieldDef.steps(ValidCustomer.class, Long.class)
                    .fieldName("createdAt").tableField(CUSTOMER.CREATED_AT)
                    .getter(teacher -> System.currentTimeMillis()),

            FieldDef.steps(ValidCustomer.class, Long.class)
                    .fieldName("updatedAt").tableField(CUSTOMER.UPDATED_AT)
                    .getter(teacher -> System.currentTimeMillis())

    );

}
