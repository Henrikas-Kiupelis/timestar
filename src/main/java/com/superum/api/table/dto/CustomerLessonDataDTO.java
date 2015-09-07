package com.superum.api.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for part of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of CustomerLessonDataDTO with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      customer            : customer of this row;
 *                            please refer to ValidCustomerDTO for details
 *      groups              : list of groups of the customer; if customer was null, the groups are student groups;
 *                            please refer to ValidGroupDTO for details
 *      teacherLessonData   : list of columns containing lesson information;
 *                            please refer to TeacherLessonDataDTO for details
 *      totalLessonData     : second to right column of the row; contains sums of the row;
 *                            please refer to TotalLessonDataDTO for details
 *      paymentData         : right column of the row; contains payment values for the customer;
 *                            please refer to PaymentDataDTO for details
 *
 * Example of JSON to expect:
 * {
 *      "customer": {...},
 *      "groups": [
 *          ...
 *      ],
 *      "teacherLessonData": [
 *          ...
 *      ],
 *      "totalLessonData": {...},
 *      "paymentData": {...}
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class CustomerLessonDataDTO {

    @JsonProperty("customer")
    public ValidCustomerDTO getCustomer() {
        return customer;
    }

    @JsonProperty("groups")
    public List<ValidGroupDTO> getGroups() {
        return groups;
    }

    @JsonProperty("teacherLessonData")
    public List<TeacherLessonDataDTO> getTeacherLessonData() {
        return teacherLessonData;
    }

    @JsonProperty("totalLessonData")
    public TotalLessonDataDTO getTotalLessonData() {
        return totalLessonData;
    }

    @JsonProperty("paymentData")
    public PaymentDataDTO getPaymentData() {
        return paymentData;
    }

    // CONSTRUCTORS

    public CustomerLessonDataDTO(ValidCustomerDTO customer, List<ValidGroupDTO> groups,
                                 List<TeacherLessonDataDTO> teacherLessonData, TotalLessonDataDTO totalLessonData,
                                 PaymentDataDTO paymentData) {
        this.customer = customer;
        this.groups = groups;
        this.teacherLessonData = teacherLessonData;
        this.totalLessonData = totalLessonData;
        this.paymentData = paymentData;
    }

    // PRIVATE

    private final ValidCustomerDTO customer;
    private final List<ValidGroupDTO> groups;
    private final List<TeacherLessonDataDTO> teacherLessonData;
    private final TotalLessonDataDTO totalLessonData;
    private final PaymentDataDTO paymentData;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CustomerLessonData")
                .add("Customer", customer)
                .add("Customer groups", groups)
                .add("Lesson data", teacherLessonData)
                .add("Total data", totalLessonData)
                .add("Payment data", paymentData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerLessonDataDTO)) return false;
        CustomerLessonDataDTO that = (CustomerLessonDataDTO) o;
        return Objects.equals(customer, that.customer) &&
                Objects.equals(groups, that.groups) &&
                Objects.equals(teacherLessonData, that.teacherLessonData) &&
                Objects.equals(totalLessonData, that.totalLessonData) &&
                Objects.equals(paymentData, that.paymentData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, groups, teacherLessonData, totalLessonData, paymentData);
    }

}
