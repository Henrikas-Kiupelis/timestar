package com.superum.fields;

/**
 * <pre>
 * Field that can be mandatory or optional for some operation (i.e. insertion of an object in database)
 * </pre>
 */
public abstract class MaybeField<T> implements Field<T> {

    /**
     * <pre>
     * Returns true if this object is mandatory for some operation (i.e. insertion of an object in database),
     * false otherwise
     * </pre>
     */
    public final boolean isMandatory() {
        return mandatory == Mandatory.YES;
    }

    // CONSTRUCTORS

    public MaybeField(Mandatory mandatory) {
        if (mandatory == null)
            throw new IllegalArgumentException("Null not allowed for defining if a field is mandatory");
        this.mandatory = mandatory;
    }

    // PROTECTED

    protected final Mandatory mandatory;

}
