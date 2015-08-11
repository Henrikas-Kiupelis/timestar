package com.superum.helper.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import org.joda.time.Instant;

/**
 * <pre>
 * Abstract class to reduce the code duplication for objects
 *
 * Contains methods for serializing createdAt and updatedAt, which are common in many objects
 * </pre>
 */
public abstract class MappedClassWithTimestamps<T extends MappedClassWithTimestamps<T, ID>, ID> extends MappedClass<T, ID> {

    @JsonProperty(CREATED_AT_FIELD)
    @JsonSerialize(using = InstantSerializer.class)
    public final Instant getCreatedAt() {
        return createdAt;
    }
    @JsonProperty(UPDATED_AT_FIELD)
    @JsonSerialize(using = InstantSerializer.class)
    public final Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return super.toString() + ", createdAt: " + createdAt + ", updatedAt: " + updatedAt;
    }

    // CONSTRUCTORS

    protected MappedClassWithTimestamps(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PRIVATE

    private final Instant createdAt;
    private final Instant updatedAt;

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

}
