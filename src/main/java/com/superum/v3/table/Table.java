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
