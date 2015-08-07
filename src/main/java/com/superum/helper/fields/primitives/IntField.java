package com.superum.helper.fields.primitives;

import com.superum.helper.fields.core.Mandatory;
import com.superum.helper.fields.core.NamedField;

/**
 * NamedField intended for use with ints
 */
public final class IntField extends NamedField<Integer> {

    @Override
    public boolean isSet() {
        return value != 0;
    }

    /**
     * @deprecated use intValue() instead; if you need a boxed version, use SimpleField{@code<Integer>} instead
     */
    @Override
    @Deprecated
    public Integer getValue() {
        return value;
    }

    /**
     * @return the primitive int value of this field
     */
    public int intValue() {
        return value;
    }

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof IntField))
            return false;

        IntField other = (IntField) o;

        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    // CONSTRUCTORS

    public IntField(String fieldName, int value, Mandatory mandatory) {
        super(fieldName, mandatory);
        this.value = value;
    }

    // PRIVATE

    private final int value;

}
