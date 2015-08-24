package com.superum.api.attendance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidLessonAttendanceDTO {

    @JsonProperty(ID_FIELD)
    public Long getId() {
        return id;
    }

    @JsonProperty(STUDENTS_FIELD)
    public Set<Integer> getStudentIds() {
        return studentIds;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidLessonAttendanceDTO jsonInstance(@JsonProperty(ID_FIELD) Long id,
                                                        @JsonProperty(STUDENTS_FIELD) Set<Integer> studentIds) {
        return new ValidLessonAttendanceDTO(id, studentIds);
    }

    public ValidLessonAttendanceDTO(Long id, Set<Integer> studentIds) {
        this.id = id;
        this.studentIds = studentIds;
    }

    // PRIVATE

    private final Long id;
    private final Set<Integer> studentIds;

    private static final String ID_FIELD = "id";
    private static final String STUDENTS_FIELD = "studentIds";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidLessonAttendance")
                .add(ID_FIELD, id)
                .add(STUDENTS_FIELD, studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidLessonAttendanceDTO && EQUALS.equals(this, (ValidLessonAttendanceDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentIds);
    }

    private static final Equals<ValidLessonAttendanceDTO> EQUALS = new Equals<>(Arrays.asList(
            ValidLessonAttendanceDTO::getId, ValidLessonAttendanceDTO::getStudentIds));

}
