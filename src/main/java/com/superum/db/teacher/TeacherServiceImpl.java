package com.superum.db.teacher;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.superum.config.Gmail;
import com.superum.db.account.Account;
import com.superum.db.account.AccountDAO;
import com.superum.db.account.AccountType;
import com.superum.db.account.roles.AccountRoles;
import com.superum.db.account.roles.AccountRolesDAO;
import com.superum.db.teacher.contract.TeacherContractService;
import com.superum.db.teacher.lang.TeacherLanguagesService;
import com.superum.utils.RandomUtils;
import com.superum.utils.StringUtils;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Override
	public Teacher addTeacher(Teacher teacher) {
		Teacher newTeacher = teacherDAO.create(teacher);
		
		char[] randomPassword = RandomUtils.randomPassword(PASSWORD_LENGTH);
		StringBuilder message = new StringBuilder()
			.append("Password: ");
		for (char ch : randomPassword)
			message.append(ch);
			
		try {
			mail.send(newTeacher.getEmail(), "Your COTEM password", message.toString());
		} catch (MessagingException e) {
			teacherDAO.delete(newTeacher.getId());
			throw new IllegalStateException("Failed to send mail! Creation aborted.", e);
		}
		
		String securePassword = encoder.encode(StringUtils.toStr(randomPassword));
		StringUtils.erase(randomPassword);
		accountDAO.create(new Account(newTeacher.getId(), newTeacher.getEmail(), AccountType.TEACHER.name(), securePassword.toCharArray()));
		accountRolesDAO.create(new AccountRoles(newTeacher.getEmail(), AccountType.TEACHER.roleNames()));
		
		return newTeacher;
	}
	
	@Override
	public Teacher findTeacher(int id) {
		return teacherDAO.read(id);
	}
	
	@Override
	public Teacher updateTeacher(Teacher teacher) {
		return teacherDAO.update(teacher);
	}
	
	@Override
	public Teacher deleteTeacher(int id) {
		teacherContractService.deleteContract(id);
		teacherLanguagesService.deleteLanguagesForTeacher(id);
		
		Teacher deletedTeacher = teacherDAO.delete(id);
		
		String username = deletedTeacher.getEmail();
		accountRolesDAO.delete(username);
		accountDAO.delete(username);
		
		return deletedTeacher;
	}
	
	@Override
	public List<Teacher> getAllTeachers() {
		return teacherDAO.readAll();
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

}
