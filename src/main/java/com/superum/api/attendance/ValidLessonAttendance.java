package com.superum.api.attendance;

import com.google.common.base.MoreObjects;
import com.superum.helper.field.ManyDefined;
import com.superum.helper.validation.Validator;
import org.jooq.lambda.Seq;

import java.util.Objects;
import java.util.Set;

import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Domain object for lesson attendance
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidLessonAttendance implements ManyDefined<Long, Integer> {

    @Override
    public Long primaryValue() {
        return id;
    }

    @Override
    public Seq<Integer> secondaryValues() {
        return Seq.seq(studentIds);
    }

    // CONSTRUCTORS

    public ValidLessonAttendance(ValidLessonAttendanceDTO validLessonAttendanceDTO) {
        this.id = validLessonAttendanceDTO.getId();
        this.studentIds = validLessonAttendanceDTO.getStudentIds();

        validate(id).not().Null().moreThan(0)
                .ifInvalid(() -> new InvalidLessonAttendanceException("Lesson id for attendance must be set and positive, not " + id));

        validate(studentIds).not().Null().not().empty().forEach(Validator::validate,
                studentId -> studentId.not().Null().moreThan(0)
                        .ifInvalid(() -> new InvalidLessonAttendanceException("Student ids for attendance must be set and positive, not " +
                                studentId.value())))
                .ifInvalid(() -> new InvalidLessonAttendanceException("Attendance must have at least a single student!"));
    }

    // PRIVATE

    private final Long id;
    private final Set<Integer> studentIds;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidLessonAttendance")
                .add("id", id)
                .add("studentIds", studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidLessonAttendance)) return false;
        ValidLessonAttendance that = (ValidLessonAttendance) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(studentIds, that.studentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentIds);
    }

}
