package com.superum.helper.fields.core;

/**
 * <pre>
 * Represents a field in an object which was made from JSON
 *
 * This field can be set or not, and it has a value (if it is not set, it has a default value)
 * </pre>
 */
public interface Field<T> {

    /**
     * <pre>
     * Returns true if the field is set (not equal to default value, such as null/0/false), false otherwise
     * </pre>
     */
    boolean isSet();

    /**
     * <pre>
     * Returns the value of this field
     * </pre>
     */
    T getValue();

}
