package com.superum.v3.table;

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
 * Data Transport Object for the report part of lesson table
 *
 * This object is responsible for serialization; the table is a read-only construct, therefore de-serialization
 * logic is not necessary
 *
 * When returning an instance of TableReport with JSON, these fields will be present:
 *      FIELD_NAME          : FIELD_DESCRIPTION
 *      id                  : id of a teacher or customer
 *      paymentDate         : date string; refers to the time when the next payment should happen
 *      cost                : the amount that needs to be paid for teacher/by customer up to the payment date
 *
 * Example of JSON to expect:
 * {
 *      "id": 1,
 *      "paymentDate": "2015-08-28",
 *      "cost": 150.2597
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TableReport {

    @JsonProperty(ID_FIELD)
    public int getId() {
        return id;
    }

    @JsonProperty(PAYMENT_DATE_FIELD)
    public String getPaymentDateString() {
        return paymentDate.toString();
    }
    @JsonIgnore
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    @JsonProperty(COST_FIELD)
    public BigDecimal getCost() {
        return cost;
    }

    // CONSTRUCTORS

    public TableReport(int id, LocalDate paymentDate, BigDecimal cost) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.cost = cost;
    }

    // PRIVATE

    private final int id;
    private final LocalDate paymentDate;
    private final BigDecimal cost;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String PAYMENT_DATE_FIELD = "paymentDate";
    private static final String COST_FIELD = "cost";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TableReport")
                .add(ID_FIELD, id)
                .add(PAYMENT_DATE_FIELD, paymentDate)
                .add(COST_FIELD, cost)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof TableReport && EQUALS.equals(this, (TableReport) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentDate, cost);
    }

    private static final Equals<TableReport> EQUALS = new Equals<>(Arrays.asList(TableReport::getId,
            TableReport::getPaymentDate, TableReport::getCost));

}
