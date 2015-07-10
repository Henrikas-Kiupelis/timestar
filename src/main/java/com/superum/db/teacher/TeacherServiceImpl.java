package com.superum.db.teacher;

import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.superum.config.Gmail;
import com.superum.db.account.Account;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.AccountType;
import com.superum.db.account.roles.AccountRoles;
import com.superum.db.account.roles.AccountRolesDAO;
import com.superum.db.teacher.contract.TeacherContract;
import com.superum.db.teacher.contract.TeacherContractService;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.utils.RandomUtils;
import com.superum.utils.StringUtils;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Override
	public Teacher addTeacher(Teacher teacher) {
		LOG.debug("Creating new Teacher: {}");
		
		Teacher newTeacher = teacherDAO.create(teacher);
		LOG.debug("New Teacher created: {}", newTeacher);
		
		char[] randomPassword = RandomUtils.randomPassword(PASSWORD_LENGTH);
		StringBuilder message = new StringBuilder()
			.append(EMAIL_BODY);
		for (char ch : randomPassword)
			message.append(ch);
		LOG.debug("Random password generated");
			
		try {
			mail.send(newTeacher.getEmail(), EMAIL_TITLE, message.toString());
			LOG.debug("Sent email to teacher '{}': title - '{}'; body - '{}'", teacher, EMAIL_TITLE, EMAIL_BODY + "[PROTECTED}");
		} catch (MessagingException e) {
			teacherDAO.delete(newTeacher.getId());
			throw new IllegalStateException("Failed to send mail! Creation aborted.", e);
		}
		
		String securePassword = encoder.encode(StringUtils.toStr(randomPassword));
		StringUtils.erase(randomPassword);
		LOG.debug("Password encoded and erased from memory");
		
		Account teacherAccount = accountDAO.create(new Account(newTeacher.getId(), newTeacher.getEmail(), AccountType.TEACHER.name(), securePassword.toCharArray()));
		LOG.debug("New Teacher Account created: {}", teacherAccount);
		
		AccountRoles teacherRoles = accountRolesDAO.create(new AccountRoles(newTeacher.getEmail(), AccountType.TEACHER.roleNames()));
		LOG.debug("Roles added to Account: {}", teacherRoles);
		
		return newTeacher;
	}
	
	@Override
	public Teacher findTeacher(int id) {
		LOG.debug("Reading teacher by ID: {}", id);
		
		Teacher teacher = teacherDAO.read(id);
		LOG.debug("Teacher retrieved: {}", teacher);
		
		return teacher;
	}
	
	@Override
	public Teacher updateTeacher(Teacher teacher) {
		LOG.debug("Updating Teacher: {}", teacher);
		
		Teacher oldTeacher = teacherDAO.update(teacher);
		LOG.debug("Old Teacher retrieved: {}", oldTeacher);
		
		return oldTeacher;
	}
	
	@Override
	public Teacher deleteTeacher(int id) {
		LOG.debug("Deleting Teacher by ID: {}", id);
		
		TeacherContract deletedContract = teacherContractService.deleteContract(id);
		LOG.debug("Deleted TeacherContract: {}", deletedContract);
		
		TeacherLanguages deletedLanguages = teacherLanguagesService.deleteLanguagesForTeacher(id);
		LOG.debug("Deleted TeacherLanguages: {}", deletedLanguages);
		
		Teacher deletedTeacher = teacherDAO.delete(id);
		LOG.debug("Deleted Teacher: {}", deletedTeacher);
		
		String username = deletedTeacher.getEmail();
		AccountRoles deletedAccountRoles = accountRolesDAO.delete(username);
		LOG.debug("Deleted AccountRoles: {}", deletedAccountRoles);
		
		Account deletedAccount = accountDAO.delete(username);
		LOG.debug("Deleted Account: {}", deletedAccount);
		
		return deletedTeacher;
	}
	
	@Override
	public List<Teacher> getAllTeachers() {
		LOG.debug("Reading all Teachers");
		
		List<Teacher> allTeachers = teacherDAO.readAll();
		LOG.debug("Teachers retrieved: {}", allTeachers);
		
		return allTeachers;
	}

	// CONSTRUCTORS
	
	@Autowired
	public TeacherServiceImpl(TeacherDAO teacherDAO, PasswordEncoder encoder, Gmail mail, AccountDAO accountDAO, AccountRolesDAO accountRolesDAO, 
			TeacherContractService teacherContractService, TeacherLanguagesService teacherLanguagesService) {
		this.teacherDAO = teacherDAO;
		this.encoder = encoder;
		this.mail = mail;
		this.accountDAO = accountDAO;
		this.accountRolesDAO = accountRolesDAO;
		this.teacherContractService = teacherContractService;
		this.teacherLanguagesService = teacherLanguagesService;
	}
	
	// PRIVATE
	
	private final TeacherDAO teacherDAO;
	private final PasswordEncoder encoder;
	private final Gmail mail;
	private final AccountDAO accountDAO;
	private final AccountRolesDAO accountRolesDAO;
	private final TeacherContractService teacherContractService;
	private final TeacherLanguagesService teacherLanguagesService;
	
	private static final int PASSWORD_LENGTH = 7;
	
	private static final String EMAIL_TITLE = "Your COTEM password";
	private static final String EMAIL_BODY = "Password: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);

}
