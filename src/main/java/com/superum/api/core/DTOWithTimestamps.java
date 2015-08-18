package com.superum.api.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import org.joda.time.Instant;

/**
 * Abstract DTO; used by other DTO classes to avoid explicit timestamp serialization
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public abstract class DTOWithTimestamps {

    @JsonProperty(CREATED_AT_FIELD)
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(UPDATED_AT_FIELD)
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // CONSTRUCTORS

    protected DTOWithTimestamps(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PROTECTED

    protected static final String CREATED_AT_FIELD = "createdAt";
    protected static final String UPDATED_AT_FIELD = "updatedAt";

    // PRIVATE

    private final Instant createdAt, updatedAt;

}
