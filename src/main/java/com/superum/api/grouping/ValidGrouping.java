package com.superum.api.grouping;

import com.google.common.base.MoreObjects;
import com.superum.helper.field.ManyDefined;
import eu.goodlike.validation.Validate;
import org.jooq.lambda.Seq;

import java.util.Objects;
import java.util.Set;

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

        Validate.Int(groupId).not().Null().moreThan(0)
                .ifInvalid(() -> new InvalidGroupingException("Group id for grouping must be set and positive, not " + groupId));

        Validate.collection(studentIds).not().Null().not().empty().forEach(Validate::Int,
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
