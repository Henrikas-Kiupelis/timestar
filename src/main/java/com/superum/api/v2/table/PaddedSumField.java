package com.superum.api.v2.table;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static org.jooq.impl.DSL.sum;
import static timestar_v2.Tables.*;

@Component
public class PaddedSumField {

    /**
     * <pre>
     * Explanation:
     *  if hourly wage is used, let's say X euros per hour, then the amount that must be paid for is
     *      cost = (T / 60) * X, where T is the amount of minutes the lesson lasted;
     *  if academic wage is used, let's say Y euros per academic hour, then the amount that must be paid for is
     *      cost = (T / 45) * Y, where T is the amount of minutes the lesson lasted;
     *  also, the table field will evaluate a sum of these costs:
     *      SUM for all i : cost(i); cost(i) = (Ti / [45|60]) * [Y|X]
     *  normally this would be fine, but we are using BigDecimal fields, which have limited accuracy (in our case,
     *  4 digits after the comma); also, notice that:
     *      45 = 3 * 3 * 5
     *      60 = 2 * 2 * 3 * 5
     *  both numbers have a prime factor of 3, which will result in inaccurate divisions when Ti is not divisible by 3
     *  (i.e. 50 / 3 = 16 + 2/3) and thus lose accuracy; the best way to avoid this is to do the division after all
     *  other operations (i.e. SUM) have taken place, because then the accuracy will only have a single chance to be
     *  lost; this proves to be difficult to achieve in our situation, because the amount that we need to divide by
     *  is conditional, based on which wage is used; the problem, however, can be solved by using LCM (least common
     *  multiple) of the divisors 45 and 60; from the previous factoring we can see that GCD(45, 60) is 3 * 5 = 15;
     *  then LCM(45, 60) = 60 * 45 / 15 = 60 * 3 = 180; using this we can evaluate
     *      cost * 180 = T * X / 60 * 180 = T * X * 3
     *      cost * 180 = T * Y / 45 * 180 = T * Y * 4
     *  then
     *      SUM for all i : cost(i) ->
     *          180 * SUM for all i : 180 * cost(i) ->
     *              180 * SUM for all i : Ti * [3X|4Y]
     *  this formula allows us to evaluate the sum times an integer without using any division for every cost(i), and
     *  then divide the resulting "padded" sum by said integer to get the actual sum we are looking for;
     *  while we could do the division operation directly in the SQL statement, the rounding mechanism used by SQL is
     *  a little too simplistic; I prefer using ROUND_HALF_EVEN, which supposedly minimizes the error;
     *  good enough for me!
     *  </pre>
     * @return SQL field, to be used in a SELECT statement; it pads the resulting cost like this:
     *  1) if hourly wage is used, then the resulting cost is multiplied by 3;
     *  2) if academic wage is used, then the resulting cost is multiplied by 4;
     */
    public Field<BigDecimal> field() {
        return sum(DSL.when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE),
                TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(3))
                .when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE).not(),
                        TEACHER.ACADEMIC_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(4)))
                .as(COST_FIELD);
    }

    /**
     * @return value of field, without the padding
     */
    public BigDecimal valueForRecord(Record record) {
        return record == null ? null : record.getValue(COST_FIELD, BigDecimal.class).divide(PADDING, ROUND_HALF_EVEN);
    }

    // LCM(45, 60) = 60 * 45 / GCD(45, 60) = 60 * 45 / 15 = 60 * 3 = 180
    private static final int PADDING_INT = 180;
    private static final BigDecimal PADDING = BigDecimal.valueOf(PADDING_INT);

    private static final String COST_FIELD = "cost * " + PADDING_INT;

}
