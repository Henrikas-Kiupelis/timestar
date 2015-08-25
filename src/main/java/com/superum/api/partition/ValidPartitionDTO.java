package com.superum.api.partition;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import com.superum.db.partition.Partition;
import com.superum.helper.Equals;

import java.util.Arrays;
import java.util.Objects;

import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Describes a partition, used to manage the database split
 *
 * When creating an instance of ValidPartitionDTO with JSON, these fields are required:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      id          : id of this partition                                      -99999999 <= id <= 999999999
 *                    this is used in authentication process, as prefix for
 *                    an username: "partitionId.username"
 *      name        : name of this partition                                    any String, max 10 chars
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "id": "0",
 *      "name": "test"
 * }
 *
 * When returning an instance of ValidPartitionDTO with JSON, no additional fields will be present;
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ValidPartitionDTO {

    @JsonProperty(ID_FIELD)
    public Integer getId() {
        return id;
    }

    @JsonProperty(NAME_FIELD)
    public String getName() {
        return name;
    }

    @JsonIgnore
    public Partition toPartition() {
        return new Partition(id, name);
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidPartitionDTO jsonInstance(@JsonProperty(ID_FIELD) Integer id,
                                              @JsonProperty(NAME_FIELD) String name) {
        return new ValidPartitionDTO(id, name);
    }

    public ValidPartitionDTO(Integer id, String name) {
        validate(id).not().Null().between(MIN_PARTITION_ID, MAX_PARTITION_ID)
                .ifInvalid(() -> new InvalidPartitionException("Partition id cannot be null, and must be between " +
                        MIN_PARTITION_ID + " and " + MAX_PARTITION_ID + ", not: " + id));

        validate(name).not().Null().not().blank().fits(PARTITION_NAME_LIMIT)
                .ifInvalid(() -> new InvalidPartitionException("Partition name cannot be null, blank and must fit " +
                        PARTITION_NAME_LIMIT + " chars"));

        this.id = id;
        this.name = name;
    }

    public static ValidPartitionDTO from(Partition partition) {
        return new ValidPartitionDTO(partition.getId(), partition.getName());
    }

    // PRIVATE

    private final Integer id;
    private final String name;

    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";

    private static final int MIN_PARTITION_ID = -99999999;
    private static final int MAX_PARTITION_ID = 999999999;

    private static final int PARTITION_NAME_LIMIT = 10;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidPartition")
                .add(ID_FIELD, id)
                .add(NAME_FIELD, name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidPartitionDTO && EQUALS.equals(this, (ValidPartitionDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    private static final Equals<ValidPartitionDTO> EQUALS = new Equals<>(Arrays.asList(ValidPartitionDTO::getId,
            ValidPartitionDTO::getName));

}
