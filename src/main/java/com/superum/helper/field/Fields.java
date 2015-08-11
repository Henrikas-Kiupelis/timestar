package com.superum.helper.field;

import com.superum.helper.field.core.MappedField;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains all of the mapped fields of the class, mostly for convenience
 * @param <T> type of class the fields belong to
 */
public class Fields<T> {

    /**
     * @return sequence of all mapped fields, to be filtered and used by other methods
     */
    public Seq<MappedField<?>> getFields() {
        return Seq.seq(fields);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return fieldString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Fields))
            return false;

        Fields other = (Fields) o;

        return Objects.equals(this.fields, other.fields);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    // CONSTRUCTORS

    public Fields(List<MappedField<?>> fields) {
        this.fields = fields;

        // This class is immutable, and it will almost always be turned into a string at least once (logs); it makes sense to cache the value
        this.fieldString = fields.stream()
                .map(MappedField::toString)
                .collect(Collectors.joining(", "));

        // Caching for hashCode(), just like toString()
        this.hash = fields.hashCode();
    }

    // PRIVATE

    private final List<MappedField<?>> fields;

    private String fieldString;
    private int hash;

}
