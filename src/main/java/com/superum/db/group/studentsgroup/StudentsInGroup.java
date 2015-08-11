package com.superum.db.group.studentsgroup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StudentsInGroup {

    @JsonProperty("groupId")
    public int getGroupId() {
        return groupId;
    }

    @JsonProperty("studentIds")
    public List<Integer> getStudentIds() {
        return studentIds;
    }

    // OBJECT OVERRIDES


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("StudentsInGroup")
                .add("Group id", groupId)
                .add("Students", studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof StudentsInGroup && EQUALS.equals(this, (StudentsInGroup) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, studentIds);
    }

    // CONSTRUCTORS

    @JsonCreator
    public StudentsInGroup(@JsonProperty("groupId") int groupId,
                           @JsonProperty("studentIds") List<Integer> studentIds) {
        this.groupId = groupId;
        this.studentIds = studentIds;
    }

    // PRIVATE

    @Min(value = 1, message = "The group id must be set")
    private final int groupId;

    private final List<Integer> studentIds;

    private static final Equals<StudentsInGroup> EQUALS = new Equals<>(Arrays.asList(StudentsInGroup::getGroupId,
            StudentsInGroup::getStudentIds));

}
