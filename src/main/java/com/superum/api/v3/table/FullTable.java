package com.superum.api.v3.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.v2.customer.ValidCustomerDTO;
import com.superum.api.v2.teacher.FullTeacherDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for lesson table; this version combines Table and TableReports into one
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
 *      teacherReports      : list of table reports for the teachers
 *                            please refer to TableReport for details
 *      customerReports     : list of table reports for the customers
 *                            please refer to TableReport for details
 *
 * Regarding fields: they are not sorted by ids; rather, the assumption is that some sort of grouping mechanism will
 * be used at front end side to put the fields into the table, fill in the blanks, and also do the summation;
 * Regarding reports: the same applies to the reports; they are also not sorted; they will, however, contain the blanks
 * because paymentDate must be transmitted even if there are no lessons to pay for
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
 *      ],
 *      "teacherReports": [
 *          ...
 *      ],
 *      "customerReports": [
 *          ...
 *      ]
 * }
 * </pre>
 */
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

    public FullTable(@JsonProperty(TEACHERS_FIELD) List<FullTeacherDTO> teachers,
                     @JsonProperty(CUSTOMERS_FIELD) List<ValidCustomerDTO> customers,
                     @JsonProperty(FIELDS_FIELD) List<TableField> fields,
                     @JsonProperty(TEACHER_REPORT_FIELD) List<TableReport> teacherReports,
                     @JsonProperty(CUSTOMER_REPORT_FIELD) List<TableReport> customerReports) {
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
        if (this == o) return true;
        if (!(o instanceof FullTable)) return false;
        FullTable fullTable = (FullTable) o;
        return Objects.equals(teachers, fullTable.teachers) &&
                Objects.equals(customers, fullTable.customers) &&
                Objects.equals(fields, fullTable.fields) &&
                Objects.equals(teacherReports, fullTable.teacherReports) &&
                Objects.equals(customerReports, fullTable.customerReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, customers, fields, teacherReports, customerReports);
    }

}
