package com.superum.helper.fields.primitives;

import com.superum.helper.fields.core.Mandatory;
import com.superum.helper.fields.core.NamedField;

public final class ByteField extends NamedField<Byte> {

    @Override
    public boolean isSet() {
        return value != 0;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    public byte byteValue() {
        return value;
    }

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ByteField))
            return false;

        ByteField other = (ByteField) o;

        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    // CONSTRUCTORS

    public ByteField(String fieldName, byte value, Mandatory mandatory) {
        super(fieldName, mandatory);
        this.value = value;
    }

    // PRIVATE

    private final byte value;

}
