package com.superum.helper.fields.core;

/**
 * <pre>
 * MaybeField that has a name
 * </pre>
 */
public abstract class NamedField<T> extends MaybeField<T> {

    /**
     * <pre>
     * Returns the name of this Field
     * </pre>
     */
    public final String getFieldName() {
        return fieldName;
    }

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
