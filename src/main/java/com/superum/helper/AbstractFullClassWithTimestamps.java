package com.superum.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.superum.fields.NamedField;
import org.joda.time.Instant;

import java.util.List;

public abstract class AbstractFullClassWithTimestamps extends AbstractFullClass {

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

    protected AbstractFullClassWithTimestamps(List<NamedField> allFields, Instant createdAt, Instant updatedAt) {
        super(allFields);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PRIVATE

    private final Instant createdAt;
    private final Instant updatedAt;

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

}
