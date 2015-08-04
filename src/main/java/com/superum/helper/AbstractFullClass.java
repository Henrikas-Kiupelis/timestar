package com.superum.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.superum.fields.Field;
import com.superum.fields.MaybeField;
import com.superum.fields.NamedField;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * Abstract class to reduce the code duplication for objects
 *
 * Contains all
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractFullClass {

    /**
     * @return true if at least one field is set; false otherwise
     */
    @JsonIgnore
    public boolean hasAnyFieldsSet() {
        return allFields().anyMatch(Field::isSet);
    }

    /**
     * @return true if all the mandatory fields are set; false otherwise
     */
    @JsonIgnore
    public boolean canBeInserted() {
        return mandatoryFields().allMatch(Field::isSet);
    }

    /**
     * @return a list of names of mandatory fields that are not set
     */
    @JsonIgnore
    public List<String> missingMandatoryFieldNames() {
        return mandatoryFields()
                .filter(field -> !field.isSet())
                .map(NamedField::getFieldName)
                .collect(Collectors.toList());
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return fieldString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof AbstractFullClass))
            return false;

        AbstractFullClass other = (AbstractFullClass) o;

        return Objects.equals(this.allFields, other.allFields);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    // CONSTRUCTORS

    protected AbstractFullClass(List<NamedField> allFields) {
        this.allFields = allFields;

        // This class is immutable, and it will almost always be turned into a string at least once (logs); it makes sense to cache the value
        this.fieldString = allFields()
                .map(Field::toString)
                .collect(Collectors.joining(", "));

        // Caching for hashCode(), just like toString()
        this.hash = allFields.hashCode();
    }

    // PROTECTED

    /**
     * <pre>
     * Intended to be used by other methods to reduce the call chain
     * </pre>
     * @return a stream of all fields
     */
    protected Stream<NamedField> allFields() {
        return allFields.stream();
    }

    /**
     * <pre>
     * Intended to be used by other methods to reduce the filtering chain
     * </pre>
     * @return a stream of all mandatory fields
     */
    protected Stream<NamedField> mandatoryFields() {
        return allFields().filter(MaybeField::isMandatory);
    }

    // PRIVATE

    private final List<NamedField> allFields;
    private final String fieldString;
    private final int hash;

}
