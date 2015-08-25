package com.superum.api.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
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
 * When returning an instance of OptimizedLessonTableDTO with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      teachers            : list of teachers that are in the table;
 *                            please refer to FullTeacherDTO for details
 *      customerLessonData  : list of table rows;
 *                            please refer to CustomerLessonDataDTO for details
 *      totalLessonData     : second to bottom row of the table; contains sums of the columns;
 *                            please refer to TotalLessonDataDTO for details
 *      paymentData         : bottom row of the table; contains payment values for teachers;
 *                            please refer to PaymentDataDTO for details
 *
 * Example of JSON to expect:
 * {
 *      "teachers": [
 *          ...
 *      ],
 *      "customerLessonData": [
 *          ...
 *      ],
 *      "totalLessonData": [
 *          ...
 *      ],
 *      "paymentData":  [
 *          ...
 *      ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class OptimizedLessonTableDTO {

    @JsonProperty("teachers")
    public List<FullTeacherDTO> getTeachers() {
        return teachers;
    }

    @JsonProperty("customerLessonData")
    public List<CustomerLessonDataDTO> getCustomerLessonData() {
        return customerLessonData;
    }

    @JsonProperty("totalLessonData")
    public List<TotalLessonDataDTO> getTotalLessonData() {
        return totalLessonData;
    }

    @JsonProperty("paymentData")
    public List<PaymentDataDTO> getPaymentData() {
        return paymentData;
    }

    // CONSTRUCTORS

    public static OptimizedLessonTableDTO empty() {
        return new OptimizedLessonTableDTO(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public OptimizedLessonTableDTO(List<FullTeacherDTO> teachers,
                                   List<CustomerLessonDataDTO> customerLessonData,
                                   List<TotalLessonDataDTO> totalLessonData,
                                   List<PaymentDataDTO> paymentData) {
        this.teachers = teachers;
        this.customerLessonData = customerLessonData;
        this.totalLessonData = totalLessonData;
        this.paymentData = paymentData;
    }

    // PRIVATE

    private final List<FullTeacherDTO> teachers;
    private final List<CustomerLessonDataDTO> customerLessonData;
    private final List<TotalLessonDataDTO> totalLessonData;
    private final List<PaymentDataDTO> paymentData;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("OptimizedLessonTable")
                .add("Teachers", teachers)
                .add("Lesson data", customerLessonData)
                .add("Total data", totalLessonData)
                .add("Payment data", paymentData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptimizedLessonTableDTO && EQUALS.equals(this, (OptimizedLessonTableDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, customerLessonData, totalLessonData, paymentData);
    }

    private static final Equals<OptimizedLessonTableDTO> EQUALS = new Equals<>(Arrays.asList(
            OptimizedLessonTableDTO::getTeachers,
            OptimizedLessonTableDTO::getCustomerLessonData, OptimizedLessonTableDTO::getTotalLessonData,
            OptimizedLessonTableDTO::getPaymentData));

}
