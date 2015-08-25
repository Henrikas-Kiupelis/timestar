package com.superum.api.grouping;

import com.google.common.base.MoreObjects;
import com.superum.helper.field.ManyDefined;
import com.superum.helper.validation.Validator;
import org.jooq.lambda.Seq;

import java.util.Objects;
import java.util.Set;

import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Domain object for grouping students into groups
 *
 * This object should be used to validate DTO data and use it in a meaningful manner;
 * </pre>
 */
public class ValidGrouping implements ManyDefined<Integer, Integer> {

    @Override
    public Integer primaryValue() {
        return groupId;
    }

    @Override
    public Seq<Integer> secondaryValues() {
        return Seq.seq(studentIds);
    }

    // CONSTRUCTORS

    public ValidGrouping(ValidGroupingDTO validGroupingDTO) {
        this.groupId = validGroupingDTO.getGroupId();
        this.studentIds = validGroupingDTO.getStudentIds();

        validate(groupId).not().Null().moreThan(0)
                .ifInvalid(() -> new InvalidGroupingException("Group id for grouping must be set and positive, not " + groupId));

        validate(studentIds).not().Null().not().empty().forEach(Validator::validate,
                studentId -> studentId.not().Null().moreThan(0)
                        .ifInvalid(() -> new InvalidGroupingException("Student ids for grouping must be set and positive, not " +
                                studentId.value())))
                .ifInvalid(() -> new InvalidGroupingException("Grouping must have at least a single student!"));
    }

    // PRIVATE

    private final Integer groupId;
    private final Set<Integer> studentIds;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidGrouping")
                .add("groupId", groupId)
                .add("studentIds", studentIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidGrouping)) return false;
        ValidGrouping that = (ValidGrouping) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(studentIds, that.studentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, studentIds);
    }

}
