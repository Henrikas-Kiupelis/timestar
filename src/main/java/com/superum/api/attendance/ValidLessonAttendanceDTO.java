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

/**
 * <pre>
 * Data Transport Object for lesson attendance
 *
 * This object is used to de-serialize JSON that is coming into the back end; it should never be used in serialization;
 * it should contain minimal logic, if any at all; a good example would be conversion between a choice of optional
 * JSON fields;
 *
 * When parsing an instance of ValidLessonAttendanceDTO with JSON, these fields are considered mandatory:
 *      FIELD_NAME     : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      lessonId       : id of a lesson                                            1 <= lessonId
 *      studentIds     : ids of the students that are attending this lesson        any set of ints, for each id: 1 <= id
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "lessonId": 1,
 *      "studentIds": [
 *          1, 2, 3
 *      ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidLessonAttendanceDTO {

    @JsonProperty(ID_FIELD)
    public Long getLessonId() {
        return lessonId;
    }

    @JsonProperty(STUDENTS_FIELD)
    public Set<Integer> getStudentIds() {
        return studentIds;
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidLessonAttendanceDTO jsonInstance(@JsonProperty(ID_FIELD) Long lessonId,
                                                        @JsonProperty(STUDENTS_FIELD) Set<Integer> studentIds) {
        return new ValidLessonAttendanceDTO(lessonId, studentIds);
    }

    public ValidLessonAttendanceDTO(Long lessonId, Set<Integer> studentIds) {
        this.lessonId = lessonId;
        this.studentIds = studentIds;
    }

    // PRIVATE

    private final Long lessonId;
    private final Set<Integer> studentIds;

    private static final String ID_FIELD = "lessonId";
    private static final String STUDENTS_FIELD = "studentIds";

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidLessonAttendance")
                .add(ID_FIELD, lessonId)
                .add(STUDENTS_FIELD, studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ValidLessonAttendanceDTO && EQUALS.equals(this, (ValidLessonAttendanceDTO) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, studentIds);
    }

    private static final Equals<ValidLessonAttendanceDTO> EQUALS = new Equals<>(Arrays.asList(
            ValidLessonAttendanceDTO::getLessonId, ValidLessonAttendanceDTO::getStudentIds));

}
