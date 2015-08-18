package com.superum.api.group;

import com.superum.db.generated.timestar.tables.records.GroupOfStudentsRecord;
import com.superum.exception.DatabaseException;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.jooq.*;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Tables.*;

@Service
@Transactional
public class ValidGroupCommandServiceImpl implements ValidGroupCommandService {

    @Override
    public ValidGroupDTO create(ValidGroupDTO validGroupDTO, int partitionId) {
        ValidGroup group = new ValidGroup(validGroupDTO);

        if (group.hasId())
            throw new InvalidGroupException("Provided group has its id set; please unset it or use POST instead!");

        if (!group.mandatoryFields().allMatch(MappedField::isSet))
            throw new InvalidGroupException("Provided group does not have the following mandatory fields set: "
                    + group.mandatoryFields().filter(MappedField::isNotSet).join(", "));

        return defaultGroupCommands.create(group, partitionId, ValidGroupDTO::valueOf)
                .orElseThrow(() -> new DatabaseException("Couldn't return group after inserting it: " + group));
    }

    @Override
    public void update(ValidGroupDTO validGroupDTO, int partitionId) {
        ValidGroup group = new ValidGroup(validGroupDTO);

        if (!group.hasId())
            throw new InvalidGroupException("Provided group doesn't have its id set; please set it or use PUT instead!");

        if (!group.updateFields().findAny().isPresent())
            throw new InvalidGroupException("Provided group only has its id set; to update this group, set additional fields!");

        if (!defaultGroupQueries.exists(group.getId(), partitionId))
            throw new GroupNotFoundException("Couldn't find group with id " + group.getId());

        if (defaultGroupCommands.update(group, partitionId) == 0)
            throw new DatabaseException("Couldn't update group: " + group);
    }

    @Override
    public void delete(int groupId, int partitionId) {
        if (!defaultGroupQueries.exists(groupId, partitionId))
            throw new GroupNotFoundException("Couldn't find group with id " + groupId);

        if (foreignGroupQueries.isUsed(groupId))
            throw new UnsafeGroupDeleteException("Cannot delete group with id " + groupId +
                    " while it still has entries in other tables");

        if (defaultGroupCommands.delete(groupId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete group with id: " + groupId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupCommandServiceImpl(DSLContext sql) {
        this.defaultGroupCommands = new DefaultCommandsImpl<>(sql, GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.ID, GROUP_OF_STUDENTS.PARTITION_ID);
        this.defaultGroupQueries = new DefaultQueriesImpl<>(sql, GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.ID, GROUP_OF_STUDENTS.PARTITION_ID);
        //noinspection Convert2Diamond
        this.foreignGroupQueries = new ForeignQueriesImpl<Integer>(sql, STUDENTS_IN_GROUPS.GROUP_ID, LESSON.GROUP_ID);
    }

    // PRIVATE

    private final DefaultCommands<GroupOfStudentsRecord, Integer> defaultGroupCommands;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final ForeignQueries<Integer> foreignGroupQueries;

}
