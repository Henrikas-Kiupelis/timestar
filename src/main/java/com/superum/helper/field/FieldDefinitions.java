package com.superum.helper.field;

import com.superum.helper.field.core.MappedField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains all of the field definitions of the class, mostly for convenience
 * @param <T> type of class the field definitions belong to
 */
public class FieldDefinitions<T> {

    /**
     * @return mapped fields of a given object
     */
    public Fields<T> toFields(T t) {
        List<MappedField<?>> mappedFields = fieldDefinitions.stream()
                .map(fieldDef -> fieldDef.toField(t))
                .collect(Collectors.toList());
        return new Fields<>(mappedFields);
    }

    // CONSTRUCTORS

    public FieldDefinitions(List<FieldDef<T, ?>> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

    // PRIVATE

    private final List<FieldDef<T, ?>> fieldDefinitions;

}
