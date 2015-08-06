package com.superum.db.group.studentsgroup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.helper.utils.StringUtils;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
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
        return "StudentsInGroup" +  StringUtils.toString(
                "Group ID: " + groupId,
                "Students: " + studentIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof StudentsInGroup))
            return false;

        StudentsInGroup other = (StudentsInGroup)o;

        return groupId == other.groupId &&
                Objects.equals(studentIds, other.studentIds);
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

}
