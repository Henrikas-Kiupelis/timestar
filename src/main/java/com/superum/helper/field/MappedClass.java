package com.superum.helper.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.steps.FieldDef;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @return true if ID field is set, false otherwise
     */
    public boolean hasId() {
        return primaryField().isSet();
    }

    /**
     * @return ID field value
     */
    public ID getId() {
        return primaryField().getValue();
    }

    @Override
    public final MappedField<ID> primaryField() {
        return primaryField;
    }

    @Override
    public Seq<MappedField<?>> createFields() {
        return nonPrimaryFields();
    }

    @Override
    public Seq<MappedField<?>> updateFields() {
        return nonPrimaryFields().filter(MappedField::isSet);
    }

    public final Seq<MappedField<?>> allFields() {
        return Seq.seq(fields);
    }

    public final Seq<MappedField<?>> nonPrimaryFields() {
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
    public final Seq<MappedField<?>> setFields() {
        return allFields().filter(MappedField::isSet);
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

    // PROTECTED

    protected void registerFields(List<FieldDef<T, ?>> fieldDefinitions, T thisObject) {
        this.fields = fieldDefinitions.stream().map(fieldDef -> fieldDef.toField(thisObject)).collect(Collectors.toList());

        // This class is immutable, and it will almost always be turned into a string at least once (logs); it makes sense to cache the value
        this.fieldString = Seq.seq(fields).map(MappedField::toString).join(", ");

        // Caching for hashCode(), just like toString()
        this.hash = fields.hashCode();

        @SuppressWarnings("unchecked")
        MappedField<ID> primaryField = (MappedField<ID>) Seq.seq(fields).filter(MappedField::isPrimary).findAny().get();
        this.primaryField = primaryField;
    }

    // PRIVATE

    private List<MappedField<?>> fields;

    private String fieldString;
    private int hash;

    private MappedField<ID> primaryField;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return fieldString;
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
        return hash;
    }

}
