package com.superum.helper.field;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.lambda.Seq;

/**
 * Allows defining a class which holds many-to-many relationship
 * @param <Primary> one of the fields, which is usually less prevalent when creating; i.e. when creating
 *                 group-to-student relationship, group would be primary, because it is more likely that a lot of
 *                 students join a group, rather than a student joins a lot of groups
 * @param <Secondary> one of the fields, which is usually less prevalent when creating; using the above example,
 *                   student would be secondary
 */
public interface ManyDefined<Primary, Secondary> {

    /**
     * @return the value of the primary field
     */
    Primary primaryValue();

    /**
     * @return sequence of values of secondary field
     */
    Seq<Secondary> secondaryValues();

    default Condition secondaryValuesEq(Field<Secondary> field) {
        return secondaryValues()
                .map(field::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("There should be at least one secondary value at all times"));
    }

}
