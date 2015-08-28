package com.superum.v3.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.teacher.FullTeacherDTO;
import com.superum.helper.Equals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for lesson table
 *
 * This object is responsible for serialization of the lesson table; the table is a read-only construct, therefore
 * de-serialization logic is not necessary
 *
 * When returning an instance of Table with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      teachers            : list of teachers that are in the table;
 *                            please refer to FullTeacherDTO for details
 *      customers           : list of customers that are in the table;
 *                            please refer to ValidCustomerDTO for details
 *      fields              : list of table fields
 *                            please refer to TableField for details
 *
 * Regarding fields: they are not sorted by ids; rather, the assumption is that some sort of grouping mechanism will
 * be used at front end side to put the fields into the table, fill in the blanks, and also do the summation;
 *
 * Example of JSON to expect:
 * {
 *      "teachers": [
 *          ...
 *      ],
 *      "customers": [
 *          ...
 *      ],
 *      "fields": [
 *          ...
 *      ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Table {

    @JsonProperty(TEACHERS_FIELD)
    public List<FullTeacherDTO> getTeachers() {
        return teachers;
    }

    @JsonProperty(CUSTOMERS_FIELD)
    public List<ValidCustomerDTO> getCustomers() {
        return customers;
    }

    @JsonProperty(FIELDS_FIELD)
    public List<TableField> getFields() {
        return fields;
    }

    // CONSTRUCTORS

    public static Table empty() {
        return new Table(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public Table(List<FullTeacherDTO> teachers, List<ValidCustomerDTO> customers, List<TableField> fields) {
        this.teachers = teachers;
        this.customers = customers;
        this.fields = fields;
    }

    // PRIVATE

    private final List<FullTeacherDTO> teachers;
    private final List<ValidCustomerDTO> customers;
    private final List<TableField> fields;

    // FIELD NAMES

    private static final String TEACHERS_FIELD = "teachers";
    private static final String CUSTOMERS_FIELD = "customers";
    private static final String FIELDS_FIELD = "fields";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Table")
                .add(TEACHERS_FIELD, teachers)
                .add(CUSTOMERS_FIELD, customers)
                .add(FIELDS_FIELD, fields)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Table && EQUALS.equals(this, (Table) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, customers, fields);
    }

    private static final Equals<Table> EQUALS = new Equals<>(Arrays.asList(Table::getTeachers, Table::getCustomers,
            Table::getFields));

}
