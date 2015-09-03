package com.superum.api.teacher;

import com.google.common.primitives.Chars;
import com.superum.db.account.Account;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.AccountType;
import com.superum.db.generated.timestar.tables.records.TeacherRecord;
import com.superum.db.partition.PartitionService;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import com.superum.helper.Random;
import com.superum.helper.jooq.CommandsForMany;
import com.superum.helper.jooq.DefaultCommands;
import com.superum.helper.jooq.DefaultQueries;
import com.superum.helper.jooq.ForeignQueries;
import com.superum.helper.mail.GMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Arrays;

import static com.superum.db.generated.timestar.Tables.TEACHER;

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

        if (teacherLanguagesCommands.create(validTeacherLanguages, account.partitionId()) == 0)
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

        if (!validTeacher.updateFields().findAny().isPresent() && !validTeacherLanguages.hasLanguages())
            throw new InvalidTeacherException("Provided teacher only has its id set; to update this teacher, set additional fields!");

        if (!defaultTeacherQueries.exists(validTeacher.getId(), partitionId))
            throw new TeacherNotFoundException("Couldn't find teacher with id " + validTeacher.getId());

        if (fullTeacherDTO.getEmail() != null &&
                defaultTeacherQueries.existsForKey(partitionId, TEACHER.EMAIL, fullTeacherDTO.getEmail()))
            throw new DuplicateTeacherException("Cannot update teacher using this email, because it is duplicate: "
                    + fullTeacherDTO.getEmail());

        if (validTeacher.updateFields().findAny().isPresent() && defaultTeacherCommands.update(validTeacher, partitionId) == 0)
            throw new DatabaseException("Couldn't update teacher: " + validTeacher);

        if (validTeacherLanguages.hasLanguages() && teacherLanguagesCommands.update(validTeacherLanguages, partitionId) == 0)
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

        teacherLanguagesCommands.deletePrimary(teacherId, account.partitionId());

        if (defaultTeacherCommands.delete(teacherId, account.partitionId()) == 0)
            throw new DatabaseException("Couldn't delete teacher with id: " + teacherId);

        accountDAO.delete(account.usernameFor(deletedTeacher.getEmail()));
    }

    /**
     * This method is public so it can be tested; should not be accessible by other classes because it is not in the
     * interface, and can only be called by casting the interface to this class
     */
    public void createAccount(FullTeacherDTO fullTeacherDTO, PartitionAccount account) {
        String name = partitionService.findPartition(account.partitionId()).getName();

        char[] randomPassword = Random.password(true, true, false, PASSWORD_LENGTH);
        StringBuilder message = new StringBuilder()
                .append(EMAIL_BODY);
        for (char ch : randomPassword)
            message.append(ch);
        LOG.debug("Random password generated");

        try {
            String fullTitle = EMAIL_TITLE + name;
            mail.send(fullTeacherDTO.getEmail(), fullTitle, message.toString());
            LOG.debug("Sent email to teacher '{}': title - '{}'; body - '{}'", fullTeacherDTO, fullTitle, EMAIL_BODY + "[PROTECTED}");
        } catch (MessagingException e) {
            defaultTeacherCommands.delete(fullTeacherDTO.getId(), account.partitionId());
            throw new IllegalStateException("Failed to send mail! Creation aborted.", e);
        }

        String securePassword = encoder.encode(Chars.join("", randomPassword));
        Arrays.fill(randomPassword, '?');
        LOG.debug("Password encoded and erased from memory");

        String accountName = account.usernameFor(fullTeacherDTO.getEmail());
        Account teacherAccount = accountDAO.create(new Account(fullTeacherDTO.getId(), accountName,
                AccountType.TEACHER.name(), securePassword.toCharArray()));
        LOG.debug("New Teacher Account created: {}", teacherAccount);
    }

    // CONSTRUCTORS

    @Autowired
    public ValidTeacherCommandServiceImpl(DefaultCommands<TeacherRecord, Integer> defaultTeacherCommands,
                                          CommandsForMany<Integer, String> teacherLanguagesCommands,
                                          DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries,
                                          ForeignQueries<Integer> foreignTeacherQueries,
                                          PasswordEncoder encoder, GMail mail, AccountDAO accountDAO,
                                          PartitionService partitionService,
                                          ValidTeacherQueryService validTeacherQueryService) {
        this.defaultTeacherCommands = defaultTeacherCommands;
        this.teacherLanguagesCommands = teacherLanguagesCommands;
        this.defaultTeacherQueries = defaultTeacherQueries;
        this.foreignTeacherQueries = foreignTeacherQueries;

        this.encoder = encoder;
        this.mail = mail;
        this.accountDAO = accountDAO;
        this.partitionService = partitionService;

        this.validTeacherQueryService = validTeacherQueryService;
    }

    // PRIVATE

    private final DefaultCommands<TeacherRecord, Integer> defaultTeacherCommands;
    private final CommandsForMany<Integer, String> teacherLanguagesCommands;
    private final DefaultQueries<TeacherRecord, Integer> defaultTeacherQueries;
    private final ForeignQueries<Integer> foreignTeacherQueries;

    private final PasswordEncoder encoder;
    private final GMail mail;
    private final AccountDAO accountDAO;
    private final PartitionService partitionService;

    private final ValidTeacherQueryService validTeacherQueryService;

    /**
     * To avoid long pauses when sending e-mails/generating passwords, accounts are created on a separate thread
     */
    private void createAccountAsync(FullTeacherDTO fullTeacherDTO, PartitionAccount account) {
        new Thread(() -> createAccount(fullTeacherDTO, account)).start();
    }

    private static final int PASSWORD_LENGTH = 7;

    private static final String EMAIL_TITLE = "Your password for ";
    private static final String EMAIL_BODY = "Password: ";

    private static final Logger LOG = LoggerFactory.getLogger(ValidTeacherCommandService.class);

}
