package com.superum.helper.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.superum.helper.NullChecker;
import com.superum.helper.field.core.MappedField;
import org.jooq.lambda.Seq;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <pre>
 * Abstract class to reduce the code duplication for objects
 *
 * Contains all methods that deal with Fields that are generic
 * </pre>
 * @param <T> type of the class that extends this class
 * @param <ID> type of the primary key of extending class
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MappedClass<T extends MappedClass<T, ID>, ID> implements Defined<T, ID> {

    /**
     * <pre>
     * Should be extended to:
     *     return this;
     *
     * It is needed for some methods which use Function of T and thus cannot use "this" reference to apply it
     * </pre>
     */
    protected abstract T thisObject();

    /**
     * @return sequence of all fields
     */
    public final Seq<MappedField<?>> allFields() {
        return fields.getFields();
    }

    /**
     * @return the primary field of this class; there should be only one of these at most;
     * @throws UnsupportedOperationException if no primary field was defined for this class
     */
    @SuppressWarnings("unchecked")
    public MappedField<ID> primaryField() {
        return (MappedField<ID>)allFields().filter(MappedField::isPrimary).findAny()
                .orElseThrow(() -> new UnsupportedOperationException("This class does not have any primary key mapped"));
    }

    /**
     * @return sequence of all fields that were not set as primary
     */
    public Seq<MappedField<?>> nonPrimaryFields() {
        return allFields().filter(MappedField::isNotPrimary);
    }

    /**
     * @return sequence of all mandatory fields
     */
    public final Seq<MappedField<?>> mandatoryFields() {
        return allFields().filter(MappedField::isMandatory);
    }

    /**
     * @return sequence of all set fields
     */
    public final Seq<MappedField<?>> allSetFields() {
        return allFields().filter(MappedField::isSet);
    }

    /**
     * @return sequence of fields that were not set as primary, but were set (as in have a value)
     */
    public final Seq<MappedField<?>> allNonPrimarySetFields() {
        return nonPrimaryFields().filter(MappedField::isSet);
    }

    /**
     * @return true if at least one field is set; false otherwise
     */
    public final boolean hasAnyFieldsSet() {
        return allFields().anyMatch(MappedField::isSet);
    }

    /**
     * @return true if all the mandatory fields are set; false otherwise
     */
    public final boolean canBeInserted() {
        return mandatoryFields().allMatch(MappedField::isSet);
    }

    /**
     * @return names of mandatory fields that are not set
     */
    public final Seq<String> missingMandatoryFieldNames() {
        return mandatoryFields()
                .filter(MappedField::isNotSet)
                .map(MappedField::getName);
    }

    /**
     * <pre>
     * Takes certain fields from this object, and another object, and compares their values;
     * The fields taken from this object are the fields from the fieldGetter, filtered to only the set fields;
     * The fields taken from given object are the fields from the fieldGetter, which are then filtered by their name
     * when comparing
     * </pre>
     * @throws IllegalArgumentException if any arguments are null
     */
    public boolean compare(T other, Function<? super T, Stream<MappedField<?>>> fieldGetter) {
        NullChecker.check(other, fieldGetter).notNull("Other object and field getter cannot be null");

        return fieldGetter.apply(thisObject())
                .filter(MappedField::isSet)
                .allMatch(field -> fieldGetter.apply(other)
                        .filter(field::equalsName)
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Two different " + thisObject().getClass().getSimpleName() + " objects should have the same fields!"))
                        .equals(field));
    }

    public boolean hasEqualSetFields(T other) {
        return compare(other, MappedClass::allSetFields);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return fields.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof MappedClass))
            return false;

        MappedClass other = (MappedClass) o;

        return Objects.equals(this.fields, other.fields);
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    // PROTECTED

    protected void registerFields(FieldDefinitions<T> fieldDefinitions) {
        fields = fieldDefinitions.toFields(thisObject());
    }

    // PRIVATE

    private Fields<T> fields;

}
