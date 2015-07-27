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
     * @return true if the
     */
    public boolean compare(Entity partial, Entity other) {
        return fieldGetter.apply(partial)
                .filter(Field::isSet)
                .allMatch(field -> fieldGetter.apply(other)
                        .filter(field::equalsName)
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Two different " + partial.getClass().getSimpleName() + " objects should have the same fields!"))
                        .equals(field));
    }

    // CONSTRUCTORS

    public SetFieldComparator(Function<Entity, Stream<NamedField>> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    // PRIVATE

    private final Function<Entity, Stream<NamedField>> fieldGetter;

}
