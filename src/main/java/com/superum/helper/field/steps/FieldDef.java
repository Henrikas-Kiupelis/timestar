package com.superum.helper.field.steps;

import com.superum.helper.field.FieldDefinition;
import com.superum.helper.field.core.MappedField;

/**
 * <pre>
 * Defines how a certain field properties can be used to turn it into a mapped field given an object
 *
 * These definitions serve two purposes:
 * 1) Statically define field's properties, such as:
 *      a) name;
 *      b) what database field it maps to;
 *      c) is it mandatory for some operation;
 *      d) is it the primary key;
 *      e) how to extract the value of this field for a given object;
 * 2) Dynamically create mapped fields, which can be used in a plethora of situations:
 *      a) using all fields of a class as a collection/stream/sequence;
 *      b) checking and filtering based around subsets of all the fields;
 *      c) automatically create operations such as sql queries for JOOQ;
 *
 * Only relevant fields should be defined, i.e. fields that are used in SQL write operations, equality comparisons
 * and so on
 * </pre>
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public interface FieldDef<T, F> extends ExtraStep<T, F> {

    /**
     * @return mapped field created using this field definition from a certain object
     */
    MappedField<F> toField(T t);

    /**
     * Convenience method to access FieldDefinition.steps()
     */
    static <T, F> FieldNameStep<T, F> steps(Class<T> containerClass, Class<F> fieldClass) {
        return FieldDefinition.steps(containerClass, fieldClass);
    }

}
