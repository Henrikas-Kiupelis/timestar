package com.superum.api.teacher;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.TeacherService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.jooq.DefaultQueryMaker;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.superum.api.teacher.FullTeacher.fullTeacherFields;
import static com.superum.db.generated.timestar.Keys.TEACHER_LANGUAGE_IBFK_1;
import static com.superum.db.generated.timestar.Tables.TEACHER;
import static com.superum.db.generated.timestar.Tables.TEACHER_LANGUAGE;

@Service
@Transactional
public class FullTeacherServiceImpl implements FullTeacherService {

    @Override
    public FullTeacher createTeacher(FullTeacher fullTeacher, PartitionAccount account) {
        if (fullTeacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        if (fullTeacher.hasId())
            throw new InvalidTeacherException("Provided teacher has its id set; please unset it or use /update instead!");

        if (!fullTeacher.canBeInserted())
            throw new InvalidTeacherException("Provided teacher does not have the following mandatory fields set: "
                    + fullTeacher.missingMandatoryFieldNames().collect(Collectors.joining(", ")));

        LOG.debug("Creating new teacher: {}", fullTeacher);

        Teacher insertedTeacher = teacherService.addTeacher(fullTeacher.toTeacher(), account);
        FullTeacher insertedFullTeacher = fullTeacher.withId(insertedTeacher.getId());
        teacherLanguagesService.addLanguagesToTeacher(insertedFullTeacher.toTeacherLanguages(), account.partitionId());
        LOG.debug("Teacher created: {}", insertedFullTeacher);

        return insertedFullTeacher;
    }

    @Override
    public FullTeacher readTeacher(int teacherId, int partitionId) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        LOG.debug("Reading teacher with id: {}", teacherId);

        FullTeacher fullTeacher = defaultQueries.readCustom(teacherId, partitionId, FullTeacher::valueOf,
                fullTeacherFields(), FullTeacherServiceImpl::teacher)
                .orElseThrow(() -> new TeacherNotFoundException("Couldn't find teacher with id " + teacherId));
        LOG.debug("Teacher retrieved: {}", fullTeacher);

        return fullTeacher;
    }

    @Override
    public FullTeacher updateTeacher(FullTeacher teacher, int partitionId) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        if (!teacher.hasId())
            throw new InvalidTeacherException("Provided teacher doesn't have its id set; please set it or use /create instead!");

        if (!teacher.canUpdateTeacher() && !teacher.canUpdateTeacherLanguages())
            throw new InvalidTeacherException("Provided teacher only has its id set; to update this teacher, set additional fields!");

        LOG.debug("Updating teacher: {}",  teacher);

        FullTeacher oldTeacher = readTeacher(teacher.getId(), partitionId);
        if (!teacher.hasEqualSetFields(oldTeacher)) {
            if (teacher.canUpdateTeacher())
                try {
                    if (defaultQueries.update(teacher, partitionId) == 0)
                        throw new DatabaseException("Couldn't update teacher: " + teacher);
                } catch (DataAccessException e) {
                    throw new DatabaseException("An unexpected error occurred when trying to update teacher: " + teacher, e);
                }

            if (teacher.canUpdateTeacherLanguages())
                teacherLanguagesService.updateLanguagesForTeacher(teacher.toTeacherLanguages(), partitionId);
        }

        LOG.debug("Teacher has been updated; before update: {}", oldTeacher);

        return oldTeacher;
    }

    @Override
    public FullTeacher deleteTeacher(int teacherId, PartitionAccount account) {
        if (teacherId <= 0)
            throw new InvalidRequestException("Teachers can only have positive ids, not " + teacherId);

        LOG.debug("Deleting teacher with id: {}", teacherId);

        FullTeacher deletedTeacher = readTeacher(teacherId, account.partitionId());
        teacherService.deleteTeacher(teacherId, account);
        LOG.debug("Teacher has been deleted: {}", deletedTeacher);

        return deletedTeacher;
    }

    @Override
    public List<FullTeacher> readTeachersAll(int page, int amount, int partitionId) {
        if (page < 0)
            throw new InvalidRequestException("Pages can only be positive, not " + (page + 1));

        if (amount <= 0 || amount > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + amount);

        LOG.debug("Reading all teachers; page {}, amount {}", page, amount);

        List<FullTeacher> teachers;
        try {
            teachers = defaultQueries.readAllCustom(page, amount, FullTeacher::valueOf,
                    fullTeacherFields(), select -> teachers(select, partitionId));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read all teachers", e);
        }
        LOG.debug("Teachers retrieved: {}", teachers);

        return teachers;
    }

    @Override
    public int countAll(int partitionId) {
        int count;
        try {
            count = defaultQueries.countAll(partitionId);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to count all teachers", e);
        }
        LOG.debug("There are a total of {} teachers", count);

        return count;
    }

    @Override
    public FullTeacher exists(FullTeacher teacher, int partitionId) {
        if (teacher == null)
            throw new InvalidRequestException("Teacher cannot be null");

        if (!teacher.hasAnyFieldsSet())
            throw new InvalidTeacherException("Provided teacher does not have any fields set, so checking if it exists is impossible");

        LOG.debug("Checking if a teacher exists: {}", teacher);

        int teacherId;
        try {
            if (teacher.hasOnlyId()) {
                teacherId = teacher.getId();
                if (!defaultQueries.exists(teacherId, partitionId))
                    throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);
            }

            teacherId = defaultQueries.exists(teacher, partitionId)
                    .orElseThrow(() -> new TeacherNotFoundException("Couldn't find this teacher: " + teacher));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to check if this teacher exists: " + teacher, e);
        }
        FullTeacher existingTeacher = readTeacher(teacherId, partitionId);
        LOG.debug("Found teacher: {}", existingTeacher);

        return existingTeacher;
    }

    // CONSTRUCTORS

    @Autowired
    public FullTeacherServiceImpl(TeacherService teacherService, TeacherLanguagesService teacherLanguagesService, DSLContext sql) {
        this.teacherService = teacherService;
        this.teacherLanguagesService = teacherLanguagesService;
        this.defaultQueries = DefaultQueryMaker.forTable(sql, TEACHER, TEACHER.ID, TEACHER.PARTITION_ID);
    }

    // PRIVATE

    private final TeacherService teacherService;
    private final TeacherLanguagesService teacherLanguagesService;
    private final DefaultQueryMaker.Queries<?, Integer> defaultQueries;

    private static <R extends Record> SelectWhereStep<R> teacher(SelectJoinStep<R> select) {
        return select.join(TEACHER_LANGUAGE).onKey(TEACHER_LANGUAGE_IBFK_1);
    }

    private static <R extends Record> SelectConditionStep<R> teachers(SelectJoinStep<R> select, int partitionId) {
        return teacher(select).where(TEACHER.PARTITION_ID.eq(partitionId));
    }

    private static final Logger LOG = LoggerFactory.getLogger(FullTeacherService.class);

}
