package com.superum.api.v2.group;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.exception.DatabaseException;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.CustomerRecord;
import timestar_v2.tables.records.GroupOfStudentsRecord;
import timestar_v2.tables.records.TeacherRecord;

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

        if (group.hasNonExistentCustomerId(id -> !defaultCustomerQueries.exists(id, partitionId)))
            throw new CustomerNotFoundException("Couldn't find customer id for group: " + group);

        if (group.hasNonExistentTeacherId(id -> !defaultTeacherQueries.exists(id, partitionId)))
            throw new TeacherNotFoundException("Couldn't find teacher id for group: " + group);

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

        if (group.hasNonExistentCustomerId(id -> !defaultCustomerQueries.exists(id, partitionId)))
            throw new CustomerNotFoundException("Couldn't find customer id for group: " + group);

        if (group.hasNonExistentTeacherId(id -> !defaultTeacherQueries.exists(id, partitionId)))
            throw new TeacherNotFoundException("Couldn't find teacher id for group: " + group);

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
    public ValidGroupCommandServiceImpl(DefaultCommands<GroupOfStudentsRecord, Integer> defaultGroupCommands,
                                        DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries,
                                        ForeignQueries<Integer> foreignGroupQueries,
                                        DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries,
                                        DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries) {
        this.defaultGroupCommands = defaultGroupCommands;
        this.defaultGroupQueries = defaultGroupQueries;
        this.foreignGroupQueries = foreignGroupQueries;
        this.defaultCustomerQueries = defaultCustomerQueries;
        this.defaultTeacherQueries = defaultTeacherQueries;
    }

    // PRIVATE

    private final DefaultCommands<GroupOfStudentsRecord, Integer> defaultGroupCommands;
    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;
    private final ForeignQueries<Integer> foreignGroupQueries;

    private final DefaultQueries<CustomerRecord, Integer> defaultCustomerQueries;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;

}
