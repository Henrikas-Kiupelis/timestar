package com.superum.helper.field.core;

import eu.goodlike.neat.Null;
import org.jooq.*;

import java.util.Objects;

/**
 * <pre>
 * Generic implementation of MappedField
 *
 * Takes care of all non-value methods and Object method overrides
 * </pre>
 * @param <F> type of the value in this field
 */
public abstract class AbstractMappedField<F> implements MappedField<F> {

    @Override
    public final boolean isMandatory() {
        return isMandatory;
    }

    @Override
    public final boolean isPrimary() {
        return isPrimary;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final boolean equalsName(MappedField<?> field) {
        if (field == null)
            throw new IllegalArgumentException("Other field cannot be null");

        return name.equals(field.getName());
    }

    @Override
    public final boolean nameEquals(String name) {
        if (name == null)
            throw new IllegalArgumentException("Other name cannot be null");

        return this.name.equals(name);
    }

    @Override
    public final <R extends Record> InsertSetMoreStep<R> insert(InsertSetMoreStep<R> step) {
        if (field == null)
            throw new UnsupportedOperationException("This field does not support insert operations");

        if (step == null)
            throw new NullPointerException("Insert step cannot be null");

        return step.set(field, getValue());
    }

    @Override
    public final <R extends Record> UpdateSetMoreStep<R> update(UpdateSetFirstStep<R> step) {
        if (field == null)
            throw new UnsupportedOperationException("This field does not support update operations");

        if (step == null)
            throw new NullPointerException("Update step cannot be null");

        return step.set(field, getValue());
    }

    @Override
    public final <R extends Record> UpdateSetMoreStep<R> update(UpdateSetMoreStep<R> step) {
        if (field == null)
            throw new UnsupportedOperationException("This field does not support update operations");

        if (step == null)
            throw new NullPointerException("Update step cannot be null");

        return step.set(field, getValue());
    }

    // CONSTRUCTORS

    protected AbstractMappedField(String name, Field<F> field, boolean isMandatory, boolean isPrimary) {
        Null.check(name, isMandatory, isPrimary).ifAny("Name, Mandatory and Primary cannot be null for MappedFields");

        this.name = name;
        this.field = field;
        this.isMandatory = isMandatory;
        this.isPrimary = isPrimary;
    }

    // PRIVATE

    private final String name;
    private final Field<F> field;
    private final boolean isMandatory;
    private final boolean isPrimary;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return name + ": " + getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof MappedField))
            return false;

        MappedField<?> other = (MappedField<?>) o;

        return this.name.equals(other.getName())
                && Objects.equals(this.getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.getValue());
    }

}