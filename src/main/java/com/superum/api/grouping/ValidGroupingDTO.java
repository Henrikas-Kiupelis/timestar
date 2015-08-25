package com.superum.api.grouping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * <pre>
 * Data Transport Object for grouping students into groups
 *
 * This object is used to de-serialize JSON that is coming into the back end; it should never be used in serialization;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When parsing an instance of ValidGroupingDTO with JSON, these fields are considered mandatory:
 *      FIELD_NAME     : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      groupId        : id of a group                                             1 <= groupId
 *      studentIds     : ids of the students that are in this group                any set of ints, for each id: 1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "groupId": 1,
 *      "studentIds": [
 *          1, 2, 3
 *      ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ValidGroupingDTO {

    @JsonProperty(ID_FIELD)
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty(STUDENTS_FIELD)
    public Set<Integer> getStudentIds() {
        return studentIds;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidGroupingDTO jsonInstance(@JsonProperty(ID_FIELD) Integer groupId,
                                                @JsonProperty(STUDENTS_FIELD) Set<Integer> studentIds) {
        return new ValidGroupingDTO(groupId, studentIds);
    }

    public ValidGroupingDTO(Integer groupId, Set<Integer> studentIds) {
        this.groupId = groupId;
        this.studentIds = studentIds;
    }

    // PRIVATE

    private final Integer groupId;
    private final Set<Integer> studentIds;

    private static final String ID_FIELD = "groupId";
    private static final String STUDENTS_FIELD = "studentIds";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidGrouping")
                .add(ID_FIELD, groupId)
                .add(STUDENTS_FIELD, studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidGroupingDTO && EQUALS.equals(this, (ValidGroupingDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, studentIds);
    }

    private static final Equals<ValidGroupingDTO> EQUALS = new Equals<>(Arrays.asList(
            ValidGroupingDTO::getGroupId, ValidGroupingDTO::getStudentIds));

}
