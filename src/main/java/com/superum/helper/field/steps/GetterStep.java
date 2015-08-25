package com.superum.helper.field.steps;

import java.util.function.Function;

/**
 * Defines a way to get the value of this field from a given object
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public interface GetterStep<T, F> {

    /**
     * Sets this field's value to getter.apply(t) for any T t
     */
    FieldDef<T, F> getter(Function<T, F> getter);

    /**
     * <pre>
     * Sets this field's value to converter.apply(getter.apply(t)) for any T t; useful when the object in the class
     * is of different type than the table field, i.e. org.joda.time.LocalDate vs java.sql.Date
     *
     * Converter method does not need to do a null check; it is implicitly done when chaining functions
     * </pre>
     */
    <U> FieldDef<T, F> getter(Function<T, U> getter, Function<U, F> converter);

}
