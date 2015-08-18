package com.superum.helper.field.steps;

import org.jooq.TableField;

/**
 * Defines the table field of this field
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public interface TableFieldStep<T, F> {

    /**
     * Sets the table field of this field
     */
    GetterStep<T, F> tableField(TableField<?, F> tableField);

}
