package com.superum.api.group;

import com.superum.exception.DatabaseException;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.jooq.DefaultQueryMaker;
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
            throw new InvalidGroupException("Provided group has its id set; please unset it or use PUT instead!");

        if (!group.mandatoryFields().allMatch(MappedField::isSet))
            throw new InvalidGroupException("Provided group does not have the following mandatory fields set: "
                    + group.mandatoryFields().filter(MappedField::isNotSet).join(", "));

        return defaultGroupQueries.create(group, partitionId, ValidGroupDTO::valueOf)
                .orElseThrow(() -> new DatabaseException("Couldn't return group after inserting it: " + group));
    }

    @Override
    public void update(ValidGroupDTO validGroupDTO, int partitionId) {
        ValidGroup group = new ValidGroup(validGroupDTO);

        if (!group.hasId())
            throw new InvalidGroupException("Provided group doesn't have its id set; please set it or use POST instead!");

        if (!group.updateFields().findAny().isPresent())
            throw new InvalidGroupException("Provided group only has its id set; to update this group, set additional fields!");

        if (!defaultGroupQueries.exists(group.getId(), partitionId))
            throw new GroupNotFoundException("Couldn't find group with id " + group.getId());

        if (defaultGroupQueries.update(group, partitionId) == 0)
            throw new DatabaseException("Couldn't update group: " + group);
    }

    @Override
    public void delete(int groupId, int partitionId) {
        if (!defaultGroupQueries.exists(groupId, partitionId))
            throw new GroupNotFoundException("Couldn't find group with id " + groupId);

        if (foreignGroupQueries.isUsed(groupId))
            throw new UnsafeGroupDeleteException("Cannot delete group with " + groupId +
                    " while it still has entries in other tables");

        if (defaultGroupQueries.delete(groupId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete group with id: " + groupId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidGroupCommandServiceImpl(DSLContext sql) {
        defaultGroupQueries = DefaultQueryMaker.forTable(sql, GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.ID, GROUP_OF_STUDENTS.PARTITION_ID);
        foreignGroupQueries = DefaultQueryMaker.forOtherTables(sql, STUDENTS_IN_GROUPS.GROUP_ID, LESSON.GROUP_ID);
    }

    // PRIVATE

    private final DefaultQueryMaker.Queries<?, Integer> defaultGroupQueries;
    private final DefaultQueryMaker.ForeignQueries<Integer> foreignGroupQueries;

}
