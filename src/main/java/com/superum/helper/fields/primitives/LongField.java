package com.superum.helper.fields.primitives;

import com.superum.helper.fields.core.Mandatory;
import com.superum.helper.fields.core.NamedField;

public final class LongField extends NamedField<Long> {

    @Override
    public boolean isSet() {
        return value != 0;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public long longValue() {
        return value;
    }

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof LongField))
            return false;

        LongField other = (LongField) o;

        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return (int)value;
    }

    // CONSTRUCTORS

    public LongField(String fieldName, long value, Mandatory mandatory) {
        super(fieldName, mandatory);
        this.value = value;
    }

    // PRIVATE

    private final long value;

}
