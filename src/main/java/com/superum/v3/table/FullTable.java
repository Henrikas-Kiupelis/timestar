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
public class FullTable {

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

    @JsonProperty(TEACHER_REPORT_FIELD)
    public List<TableReport> getTeacherReports() {
        return teacherReports;
    }

    @JsonProperty(CUSTOMER_REPORT_FIELD)
    public List<TableReport> getCustomerReports() {
        return customerReports;
    }

    // CONSTRUCTORS

    public static FullTable empty() {
        return new FullTable(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    public FullTable(List<FullTeacherDTO> teachers, List<ValidCustomerDTO> customers, List<TableField> fields,
                     List<TableReport> teacherReports, List<TableReport> customerReports) {
        this.teachers = teachers;
        this.customers = customers;
        this.fields = fields;
        this.teacherReports = teacherReports;
        this.customerReports = customerReports;
    }

    // PRIVATE

    private final List<FullTeacherDTO> teachers;
    private final List<ValidCustomerDTO> customers;
    private final List<TableField> fields;
    private final List<TableReport> teacherReports;
    private final List<TableReport> customerReports;

    // FIELD NAMES

    private static final String TEACHERS_FIELD = "teachers";
    private static final String CUSTOMERS_FIELD = "customers";
    private static final String FIELDS_FIELD = "fields";
    private static final String TEACHER_REPORT_FIELD = "teacherReports";
    private static final String CUSTOMER_REPORT_FIELD = "customerReports";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("FullTable")
                .add(TEACHERS_FIELD, teachers)
                .add(CUSTOMERS_FIELD, customers)
                .add(FIELDS_FIELD, fields)
                .add(FIELDS_FIELD, fields)
                .add(FIELDS_FIELD, fields)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof FullTable && EQUALS.equals(this, (FullTable) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, customers, fields, teacherReports, customerReports);
    }

    private static final Equals<FullTable> EQUALS = new Equals<>(Arrays.asList(FullTable::getTeachers,
            FullTable::getCustomers, FullTable::getFields, FullTable::getTeacherReports, FullTable::getCustomerReports));


}
