package com.superum.helper.field.steps;

/**
 * Defines optional parameters the field may have
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public interface ExtraStep<T, F> {

    /**
     * Sets this field as primary key; do not set more than one field as primary key to avoid nondeterministic results!
     */
    FieldDef<T, F> primaryKey();

    /**
     * Sets this field as mandatory for some operation; this operation isn't formalized, so you can tag any fields
     * as mandatory for whatever reason so you can later filter the list of fields
     */
    FieldDef<T, F> mandatory();

}
