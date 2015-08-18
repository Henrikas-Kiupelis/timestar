package com.superum.helper.field.steps;

/**
 * Defines the name of the field; can be used to identify the field; does NOT have to match the JSON field name
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public interface FieldNameStep<T, F> {

    /**
     * Sets the name of this field; does NOT have to match the JSON field name
     */
    TableFieldStep<T, F> fieldName(String name);

}
