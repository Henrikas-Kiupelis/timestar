package com.superum.api.v2.lesson;

import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.exception.DatabaseException;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.GroupOfStudentsRecord;
import timestar_v2.tables.records.LessonRecord;

@Service
@Transactional
public class ValidLessonCommandServiceImpl implements ValidLessonCommandService {

    @Override
    public ValidLessonDTO create(ValidLessonDTO validLessonDTO, int partitionId) {
        ValidLesson lesson = new ValidLesson(validLessonDTO);

        if (lesson.hasId())
            throw new InvalidLessonException("Provided lesson has its id set; please unset it or use POST instead!");

        if (!lesson.mandatoryFields().allMatch(MappedField::isSet))
            throw new InvalidLessonException("Provided lesson does not have the following mandatory fields set: "
                    + lesson.mandatoryFields().filter(MappedField::isNotSet).join(", "));

        if (lesson.hasNonExistentGroupId(id -> !defaultGroupQueries.exists(id, partitionId)))
            throw new GroupNotFoundException("Couldn't find group id for lesson: " + lesson);

        if (lesson.isOverlapping(sql, partitionId))
            throw new OverlappingLessonException("This teacher already has a lesson during this time, cannot create!");

        return defaultLessonCommands.create(lesson, partitionId, ValidLessonDTO::valueOf)
                .orElseThrow(() -> new DatabaseException("Couldn't return lesson after inserting it: " + lesson));
    }

    @Override
    public void update(ValidLessonDTO validLessonDTO, int partitionId) {
        ValidLesson lesson = new ValidLesson(validLessonDTO);

        if (!lesson.hasId())
            throw new InvalidLessonException("Provided lesson doesn't have its id set; please set it or use PUT instead!");

        if (!lesson.updateFields().findAny().isPresent())
            throw new InvalidLessonException("Provided lesson only has its id set; to update this lesson, set additional fields!");

        if (!defaultLessonQueries.exists(lesson.getId(), partitionId))
            throw new LessonNotFoundException("Couldn't find lesson with id " + lesson.getId());

        if (lesson.hasNonExistentGroupId(id -> !defaultGroupQueries.exists(id, partitionId)))
            throw new GroupNotFoundException("Couldn't find group id for lesson: " + lesson);

        if (lesson.isOverlapping(sql, partitionId))
            throw new OverlappingLessonException("This teacher already has a lesson during this time, cannot update!");

        if (defaultLessonCommands.update(lesson, partitionId) == 0)
            throw new DatabaseException("Couldn't update lesson: " + lesson);
    }

    @Override
    public void delete(long lessonId, int partitionId) {
        if (!defaultLessonQueries.exists(lessonId, partitionId))
            throw new LessonNotFoundException("Couldn't find lesson with id " + lessonId);

        if (foreignLessonQueries.isUsed(lessonId))
            throw new UnsafeLessonDeleteException("Cannot delete lesson with id " + lessonId +
                    " while it still has entries in other tables");

        if (defaultLessonCommands.delete(lessonId, partitionId) == 0)
            throw new DatabaseException("Couldn't delete lesson with id: " + lessonId);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidLessonCommandServiceImpl(DSLContext sql, DefaultCommands<LessonRecord, Long> defaultLessonCommands,
                                         DefaultQueries<LessonRecord, Long> defaultLessonQueries,
                                         ForeignQueries<Long> foreignLessonQueries,
                                         DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries) {
        this.sql = sql;
        this.defaultLessonCommands = defaultLessonCommands;
        this.defaultLessonQueries = defaultLessonQueries;
        this.foreignLessonQueries = foreignLessonQueries;
        this.defaultGroupQueries = defaultGroupQueries;
    }

    // PRIVATE

    private final DSLContext sql;
    private final DefaultCommands<LessonRecord, Long> defaultLessonCommands;
    private final DefaultQueries<LessonRecord, Long> defaultLessonQueries;
    private final ForeignQueries<Long> foreignLessonQueries;

    private final DefaultQueries<GroupOfStudentsRecord, Integer> defaultGroupQueries;

}
