package com.superum.db.teacher;

import com.google.common.primitives.Chars;
import com.superum.config.Gmail;
import com.superum.db.account.Account;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.AccountType;
import com.superum.db.partition.PartitionService;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.utils.PrincipalUtils;
import com.superum.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Override
	public Teacher addTeacher(Teacher teacher, int partitionId) {
		LOG.debug("Creating new Teacher: {}");
		
		Teacher newTeacher = teacherDAO.create(teacher, partitionId);
		LOG.debug("New Teacher created: {}", newTeacher);
		
		createAccount(newTeacher, partitionId);

		return newTeacher;
	}
	
	@Override
	public Teacher findTeacher(int id, int partitionId) {
		LOG.debug("Reading teacher by ID: {}", id);
		
		Teacher teacher = teacherDAO.read(id, partitionId);
		LOG.debug("Teacher retrieved: {}", teacher);
		
		return teacher;
	}
	
	@Override
	public Teacher updateTeacher(Teacher teacher, int partitionId) {
		LOG.debug("Updating Teacher: {}", teacher);
		
		Teacher oldTeacher = teacherDAO.update(teacher, partitionId);
		LOG.debug("Old Teacher retrieved: {}", oldTeacher);
		
		return oldTeacher;
	}
	
	@Override
	public Teacher deleteTeacher(int id, int partitionId) {
		LOG.debug("Deleting Teacher by ID: {}", id);
		
		TeacherLanguages deletedLanguages = teacherLanguagesService.deleteLanguagesForTeacher(id, partitionId);
		LOG.debug("Deleted TeacherLanguages: {}", deletedLanguages);
		
		Teacher deletedTeacher = teacherDAO.delete(id, partitionId);
		LOG.debug("Deleted Teacher: {}", deletedTeacher);
		
		String username = PrincipalUtils.makeName(deletedTeacher.getEmail(), partitionId);
		Account deletedAccount = accountDAO.delete(username);
		LOG.debug("Deleted Account: {}", deletedAccount);
		
		return deletedTeacher;
	}
	
	@Override
	public List<Teacher> getAllTeachers(int partitionId) {
		LOG.debug("Reading all Teachers");
		
		List<Teacher> allTeachers = teacherDAO.readAll(partitionId);
		LOG.debug("Teachers retrieved: {}", allTeachers);
		
		return allTeachers;
	}

	// CONSTRUCTORS
	
	@Autowired
	public TeacherServiceImpl(TeacherDAO teacherDAO, PasswordEncoder encoder, Gmail mail, AccountDAO accountDAO,
			TeacherLanguagesService teacherLanguagesService, PartitionService partitionService) {
		this.teacherDAO = teacherDAO;
		this.encoder = encoder;
		this.mail = mail;
		this.accountDAO = accountDAO;
		this.teacherLanguagesService = teacherLanguagesService;
		this.partitionService = partitionService;
	}
	
	// PRIVATE
	
	private final TeacherDAO teacherDAO;
	private final PasswordEncoder encoder;
	private final Gmail mail;
	private final AccountDAO accountDAO;
	private final TeacherLanguagesService teacherLanguagesService;
	private final PartitionService partitionService;

    private void createAccount(Teacher newTeacher, int partitionId) {
        // To avoid long pauses when sending e-mails/generating passwords, accounts are created on a separate thread
        new Thread(() -> {
            String name = partitionService.findPartition(partitionId).getName();

            char[] randomPassword = RandomUtils.randomPassword(PASSWORD_LENGTH);
            StringBuilder message = new StringBuilder()
                    .append(EMAIL_BODY);
            for (char ch : randomPassword)
                message.append(ch);
            LOG.debug("Random password generated");

            try {
                String fullTitle = EMAIL_TITLE + name;
                mail.send(newTeacher.getEmail(), fullTitle, message.toString());
                LOG.debug("Sent email to teacher '{}': title - '{}'; body - '{}'", newTeacher, fullTitle, EMAIL_BODY + "[PROTECTED}");
            } catch (MessagingException e) {
                teacherDAO.delete(newTeacher.getId(), partitionId);
                throw new IllegalStateException("Failed to send mail! Creation aborted.", e);
            }

            String securePassword = encoder.encode(Chars.join("", randomPassword));
            Arrays.fill(randomPassword, '?');
            randomPassword = null;
            LOG.debug("Password encoded and erased from memory");

            String accountName = PrincipalUtils.makeName(newTeacher.getEmail(), partitionId);
            Account teacherAccount = accountDAO.create(new Account(newTeacher.getId(), accountName, AccountType.TEACHER.name(), securePassword.toCharArray()));
            LOG.debug("New Teacher Account created: {}", teacherAccount);
		}).start();
    }
	
	private static final int PASSWORD_LENGTH = 7;
	
	private static final String EMAIL_TITLE = "Your password for ";
	private static final String EMAIL_BODY = "Password: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);

}
