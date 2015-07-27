package com.superum.helper;

import com.superum.fields.Field;
import com.superum.fields.NamedField;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <pre>
 * Allows to check if one object's set fields are the same in another object
 * </pre>
 * @param <Entity> a class that has the NamedFields to compare
 */
public class SetFieldComparator<Entity> {

    /**
     * @return true if the partial entity set fields given by getter are equal to the given entity's fields
     *
     * @throws IllegalArgumentException if any arguments are null
     */
    public boolean compare(Entity other, Function<Entity, Stream<NamedField>> fieldGetter) {
        if (other == null)
            throw new IllegalArgumentException("The other entity cannot be null");

        if (fieldGetter == null)
            throw new IllegalArgumentException("Field getter cannot be null");

        return fieldGetter.apply(partial)
                .filter(Field::isSet)
                .allMatch(field -> fieldGetter.apply(other)
                        .filter(field::equalsName)
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Two different " + partial.getClass().getSimpleName() + " objects should have the same fields!"))
                        .equals(field));
    }

    // CONSTRUCTORS

    public SetFieldComparator(Entity partial) {
        if (partial == null)
            throw new IllegalArgumentException("Partial entity cannot be null");

        this.partial = partial;
    }

    // PRIVATE

    private final Entity partial;

}
