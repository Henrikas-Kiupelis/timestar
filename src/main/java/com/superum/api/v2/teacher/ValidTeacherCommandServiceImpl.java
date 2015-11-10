package com.superum.api.v2.teacher;

import com.superum.api.v2.account.ValidAccountService;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import eu.goodlike.libraries.jooq.CommandsMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.TeacherRecord;

import static timestar_v2.Tables.TEACHER;

@Service
@Transactional
public class ValidTeacherCommandServiceImpl implements ValidTeacherCommandService {

    @Override
    public FullTeacherDTO create(FullTeacherDTO fullTeacherDTO, PartitionAccount account) {
        ValidTeacher validTeacher = new ValidTeacher(fullTeacherDTO);
        ValidTeacherLanguages validTeacherLanguages = ValidTeacherLanguages.fromDTO(fullTeacherDTO);

        if (validTeacher.hasId())
            throw new InvalidTeacherException("Provided teacher has its id set; please unset it or use POST instead!");

        if (!validTeacher.canBeInserted() || !validTeacherLanguages.hasLanguages())
            throw new InvalidTeacherException("Provided teacher does not have the following mandatory fields set: "
                    + validTeacher.missingMandatoryFieldNames().join(", ")
                    + (validTeacherLanguages.hasLanguages() ? "" : ", languages"));

        if (defaultTeacherQueries.existsForKey(account.partitionId(), TEACHER.EMAIL, fullTeacherDTO.getEmail()))
            throw new DuplicateTeacherException("Cannot create teacher using this email, because it is duplicate: "
                    + fullTeacherDTO.getEmail());

        Integer teacherId = defaultTeacherCommands.create(validTeacher, account.partitionId(),
                record -> record.getValue(TEACHER.ID))
                .orElseThrow(() -> new DatabaseException("Couldn't return teacher after inserting it: " + validTeacher));

        validTeacherLanguages = validTeacherLanguages.withId(teacherId);

        if (teacherLanguageCommands.create(validTeacherLanguages.primaryValue(),
                validTeacherLanguages.secondaryValues().toList()) == 0)
            throw new DatabaseException("Couldn't create teacher languages: " + validTeacherLanguages);

        FullTeacherDTO insertedTeacher = validTeacherQueryService.readById(teacherId, account.partitionId());

        createAccountAsync(insertedTeacher, account);

        return insertedTeacher;
    }

    @Override
    public void update(FullTeacherDTO fullTeacherDTO, int partitionId) {
        ValidTeacher validTeacher = new ValidTeacher(fullTeacherDTO);
        ValidTeacherLanguages validTeacherLanguages = ValidTeacherLanguages.fromDTO(fullTeacherDTO);

        if (!validTeacher.hasId())
            throw new InvalidTeacherException("Provided teacher doesn't have its id set; please set it or use PUT instead!");

        if (!validTeacher.updateFields().filter(field -> field.notNameEquals("updatedAt")).findAny().isPresent()
                && !validTeacherLanguages.hasLanguages())
            throw new InvalidTeacherException("Provided teacher only has its id set; to update this teacher, set additional fields!");

        if (!defaultTeacherQueries.exists(validTeacher.getId(), partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + validTeacher.getId());

        if (fullTeacherDTO.getEmail() != null &&
                defaultTeacherQueries.existsForKey(partitionId, TEACHER.EMAIL, fullTeacherDTO.getEmail()))
            throw new DuplicateTeacherException("Cannot update teacher using this email, because it is duplicate: "
                    + fullTeacherDTO.getEmail());

        if (validTeacher.updateFields().findAny().isPresent() && defaultTeacherCommands.update(validTeacher, partitionId) == 0)
            throw new DatabaseException("Couldn't update teacher: " + validTeacher);

        if (validTeacherLanguages.hasLanguages() && teacherLanguageCommands.update(validTeacherLanguages.primaryValue(),
                validTeacherLanguages.secondaryValues().toList()) == 0)
            throw new DatabaseException("Couldn't update teacher languages: " + validTeacherLanguages);
    }

    @Override
    public void delete(int teacherId, PartitionAccount account) {
        if (!defaultTeacherQueries.exists(teacherId, account.partitionId()))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + teacherId);

        if (foreignTeacherQueries.isUsed(teacherId))
            throw new UnsafeTeacherDeleteException("Cannot delete teacher with id " + teacherId +
                    " while it still has entries in other tables");

        FullTeacherDTO deletedTeacher = validTeacherQueryService.readById(teacherId, account.partitionId());

        teacherLanguageCommands.deleteLeft(teacherId);

        if (defaultTeacherCommands.delete(teacherId, account.partitionId()) == 0)
            throw new DatabaseException("Couldn't delete teacher with id: " + teacherId);

        validAccountService.deleteAccount(deletedTeacher, account);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidTeacherCommandServiceImpl(DefaultCommands<TeacherRecord, Integer> defaultTeacherCommands,
                                          CommandsMany<Integer, String> teacherLanguageCommands,
                                          DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries,
                                          ForeignQueries<Integer> foreignTeacherQueries,
                                          ValidAccountService validAccountService,
                                          ValidTeacherQueryService validTeacherQueryService) {
        this.defaultTeacherCommands = defaultTeacherCommands;
        this.teacherLanguageCommands = teacherLanguageCommands;
        this.defaultTeacherQueries = defaultTeacherQueries;
        this.foreignTeacherQueries = foreignTeacherQueries;
        this.validTeacherQueryService = validTeacherQueryService;
        this.validAccountService = validAccountService;
    }

    // PRIVATE

    private final DefaultCommands<TeacherRecord, Integer> defaultTeacherCommands;
    private final CommandsMany<Integer, String> teacherLanguageCommands;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;
    private final ForeignQueries<Integer> foreignTeacherQueries;
    private final ValidTeacherQueryService validTeacherQueryService;
    private final ValidAccountService validAccountService;

    /**
     * To avoid long pauses when sending e-mails/generating passwords, accounts are created on a separate thread
     */
    private void createAccountAsync(FullTeacherDTO fullTeacherDTO, PartitionAccount account) {
        new Thread(() -> validAccountService.createAccount(fullTeacherDTO, account)).start();
    }

}
