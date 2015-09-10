package com.superum.api.v2.grouping;

import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.student.StudentNotFoundException;
import com.superum.exception.DatabaseException;
import com.superum.helper.jooq.CommandsForMany;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.QueriesForMany;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.GroupOfStudentsRecord;
import timestar_v2.tables.records.StudentRecord;

import static timestar_v2.Tables.STUDENT;

@Service
@Transactional
public class ValidGroupingCommandServiceImpl implements ValidGroupingCommandService {

    @Override
    public void create(ValidGroupingDTO validGroupingDTO, int partitionId) {
        ValidGrouping grouping = new ValidGrouping(validGroupingDTO);
        validateIdsInDB(grouping, partitionId);

        if (defaultGroupingQueries.existsPrimary(grouping.primaryValue(), partitionId))
            throw new DuplicateGroupingException("Grouping for this group already exists; please use POST instead!");

        if (groupingCommands.create(grouping, partitionId) == 0)
            throw new DatabaseException("Couldn't create grouping: " + grouping);
    }

    @Override
    public void update(ValidGroupingDTO validGroupingDTO, int partitionId) {
        ValidGrouping grouping = new ValidGrouping(validGroupingDTO);
        validateIdsInDB(grouping, partitionId);

        if (!defaultGroupingQueries.existsPrimary(grouping.primaryValue(), partitionId))
            throw new GroupingNotFoundException("Grouping for this group doesn't exist yet; please use PUT instead!");

        if (groupingCommands.update(grouping, partitionId) == 0)
            throw new DatabaseException("Couldn't update grouping: " + grouping);
    }

    @Override
    public void deleteForGroup(int groupId, int partitionId) {
        if (!defaultGroupQueries.exists(groupId, partitionId))
            throw new GroupNotFoundException("Couldn't find group with id: " + groupId);

        if (!defaultGroupingQueries.existsPrimary(groupId, partitionId))
            throw new GroupingNotFoundException("Grouping doesn't exist for group with id: " + groupId);

        if (groupingCommands.deletePrimary(groupId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete grouping for group with id: " + groupId);
    }

    @Override
    public void deleteForStudent(int studentId, int partitionId) {
        if (!defaultStudentQueries.exists(studentId, partitionId))
            throw new StudentNotFoundException("Couldn't find group with id: " + studentId);

        if (!defaultGroupingQueries.existsSecondary(studentId, partitionId))
            throw new GroupingNotFoundException("Grouping doesn't exist for student with id: " + studentId);

        if (groupingCommands.deleteSecondary(studentId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete grouping for student with id: " + studentId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupingCommandServiceImpl(CommandsForMany<Integer, Integer> groupingCommands,
                                           DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                           DefaultQueries<StudentRecord, Integer> defaultStudentQueries,
                                           QueriesForMany<Integer, Integer> defaultGroupingQueries) {
        this.groupingCommands = groupingCommands;
        this.defaultGroupQueries = defaultGroupQueries;
        this.defaultStudentQueries = defaultStudentQueries;
        this.defaultGroupingQueries = defaultGroupingQueries;
    }

    // PRIVATE

    private final CommandsForMany<Integer, Integer> groupingCommands;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final DefaultQueries<StudentRecord, Integer> defaultStudentQueries;
    private final QueriesForMany<Integer, Integer> defaultGroupingQueries;

    private void validateIdsInDB(ValidGrouping grouping, int partitionId) {
        if (!defaultGroupQueries.exists(grouping.primaryValue(), partitionId))
            throw new GroupNotFoundException("Couldn't find group with id: " + grouping.primaryValue());

        Condition condition = grouping.secondaryValuesEq(STUDENT.ID)
                .and(defaultStudentQueries.partitionId(partitionId));

        if (defaultStudentQueries.countForCondition(condition) != grouping.secondaryValues().count())
            throw new StudentNotFoundException("Couldn't find some of students with ids: " +
                    grouping.secondaryValues().join(", "));
    }
}
