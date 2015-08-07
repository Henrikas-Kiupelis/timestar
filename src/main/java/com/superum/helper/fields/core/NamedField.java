package com.superum.helper.fields.core;

/**
 * MaybeField that has a name
 */
public abstract class NamedField<T> extends MaybeField<T> {

    /**
     * @return name of this Field
     */
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * @return true if the name of the other field is equal to the name of this field; false otherwise
     */
    public final boolean equalsName(NamedField other) {
        return this.fieldName.equals(other.fieldName);
    }

    @Override
    public final String toString() {
        return fieldName + ": " + getValue();
    }

    // CONSTRUCTORS

    public NamedField(String fieldName, Mandatory mandatory) {
        super(mandatory);
        if (fieldName == null)
            throw new IllegalArgumentException("Null not allowed for defining field name");
        this.fieldName = fieldName;
    }

    // PROTECTED

    protected final String fieldName;

}
