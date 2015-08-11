package com.superum.helper.field.core;

import org.jooq.*;

/**
 * Defines how a value from a specific object field maps to a table record
 * @param <F> type of the value in this field
 */
public interface MappedField<F> {

    /**
     * @return the value of this field; can be null
     */
    F getValue();

    /**
     * @return true if this field is set; false otherwise; for most objects this is equivalent to
     * getValue() != null; however, for primitives a default value check should be done instead, i.e.
     * getValue() != 0
     */
    boolean isSet();

    /**
     * @return true if this field was set as the primary field, false otherwise; there is only one of
     * such fields per table
     */
    boolean isPrimary();

    /**
     * @return true if this field was set as a mandatory field, false otherwise; this can apply to all or none
     * of the fields
     */
    boolean isMandatory();

    /**
     * @return the name of this field
     */
    String getName();

    /**
     * @return true if the name of this field is the same as the given field, false otherwise
     */
    boolean equalsName(MappedField<?> field);

    /**
     * @return true if the name of this field is equal to the given string, false otherwise
     */
    boolean nameEquals(String name);

    /**
     * <pre>
     * This method takes a JOOQ query insertion step and returns as if
     *      step.set(field, value);
     * was called; this allows to use a stream/sequence of fields to reduce/foldLeft into a complete
     * insertion step
     * </pre>
     */
    <R extends Record> InsertSetMoreStep<R> insert(InsertSetMoreStep<R> step);

    /**
     * <pre>
     * This method takes a JOOQ query update first step and returns as if
     *      step.set(field, value);
     * was called; this allows to use the first element of a sequence of fields to begin the complete
     * update step
     * </pre>
     */
    <R extends Record> UpdateSetMoreStep<R> update(UpdateSetFirstStep<R> step);

    /**
     * <pre>
     * This method takes a JOOQ query update step and returns as if
     *      step.set(field, value);
     * was called; this allows to use a stream/sequence of fields to reduce/foldLeft into a complete
     * update step
     * </pre>
     */
    <R extends Record> UpdateSetMoreStep<R> update(UpdateSetMoreStep<R> step);

    /**
     * @return JOOQ condition of field.eq(value);
     */
    Condition eq();

    // REVERSE BOOLEAN FUNCTIONS

    default boolean isNotSet() {
        return !isSet();
    }

    default boolean isNotPrimary() {
        return !isPrimary();
    }

    default boolean isNotMandatory() {
        return !isMandatory();
    }

    default boolean notEqualsName(MappedField<?> field) {
        return !equalsName(field);
    }

    default boolean notNameEquals(String name) {
        return !nameEquals(name);
    }

}