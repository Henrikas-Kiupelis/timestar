package com.superum.fields;

import java.util.Objects;

/**
 * <pre>
 * NamedField intended for use with non-primitives
 * </pre>
 */
public class SimpleField<T> extends NamedField<T> {

    @Override
    public boolean isSet() {
        return value != null;
    }

    @Override
    public T getValue() {
        return value;
    }

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SimpleField))
            return false;

        SimpleField other = (SimpleField) o;

        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    // CONSTRUCTORS

    public static <T> SimpleField<T> empty(String fieldName, Mandatory mandatory) {
        return new SimpleField<>(fieldName, null, mandatory);
    }

    public SimpleField(String fieldName, T value, Mandatory mandatory) {
        super(fieldName, mandatory);
        this.value = value;
    }

    // PROTECTED

    protected final T value;

}
