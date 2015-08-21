package com.superum.api.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * <pre>
 * Data Transport Object for part of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of PaymentDataDTO with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      paymentDate         : date string; refers to the time when the next payment should happen
 *      cost                : the amount that needs to be paid for teacher/by customer up to the payment date
 *
 * Example of JSON to expect:
 * {
 *      "paymentDate": "2015-08-21",
 *      "cost": 150.2597
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PaymentDataDTO {

    @JsonProperty("paymentDate")
    public String getPaymentDateString() {
        return paymentDate.toString();
    }
    @JsonIgnore
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    @JsonProperty("cost")
    public BigDecimal getCost() {
        return cost;
    }

    // CONSTRUCTORS

    public PaymentDataDTO(LocalDate paymentDate, BigDecimal cost) {
        this.paymentDate = paymentDate;
        this.cost = cost;
    }

    // PRIVATE

    private final LocalDate paymentDate;
    private final BigDecimal cost;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("PaymentData")
                .add("Payment date", paymentDate)
                .add("Cost", cost)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof PaymentDataDTO && EQUALS.equals(this, (PaymentDataDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentDate, cost);
    }

    private static final Equals<PaymentDataDTO> EQUALS = new Equals<>(Arrays.asList(
            PaymentDataDTO::getPaymentDate, PaymentDataDTO::getCost));
}
