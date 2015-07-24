package com.superum.fields;

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

    @Override
    public final String toString() {
        return fieldName + ": " + getValue();
    }

    // CONSTRUCTORS

    public NamedField(String fieldName, Mandatory mandatory) {
        super(mandatory);
        this.fieldName = fieldName;
    }

    // PROTECTED

    protected final String fieldName;

}
