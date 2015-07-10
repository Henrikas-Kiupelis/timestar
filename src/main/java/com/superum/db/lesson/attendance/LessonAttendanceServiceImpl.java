package com.superum.db.lesson.attendance;

import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.config.Gmail;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentDAO;
import com.superum.db.lesson.attendance.code.LessonCode;
import com.superum.db.lesson.attendance.code.LessonCodeDAO;
import com.superum.db.lesson.attendance.code.LessonCodeService;

@Service
public class LessonAttendanceServiceImpl implements LessonAttendanceService {

	@Override
	public LessonAttendance addAttendanceToLesson(LessonAttendance attendance) {
		LOG.debug("Adding attendance: {}", attendance);
		
		LessonAttendance newAttendance = attendanceDAO.create(attendance);
		LOG.debug("Attendance added: {}", newAttendance);
		
		List<LessonCode> lessonCodes = lessonCodeService.add(newAttendance);
		LOG.debug("Codes for students generated: {}", lessonCodes);
		
		for (LessonCode lessonCode : lessonCodes) {
			int studentId = lessonCode.getStudentId();
			int code = lessonCode.getCode();
			Student student = studentDAO.read(studentId);
			try {
				String fullBody = EMAIL_BODY + code;
				mail.send(student.getEmail(), EMAIL_TITLE, fullBody);
				LOG.debug("Sent email to student '{}': title - '{}'; body - '{}'", student, EMAIL_TITLE, fullBody);
			} catch (MessagingException e) {
				lessonCodeDAO.find(attendance.getLessonId(), code);
				throw new IllegalStateException("Failed to send mail! Code aborted.", e);
			}
		}
		
		return newAttendance;
	}

	@Override
	public LessonAttendance getAttendanceForLesson(long lessonId) {
		LOG.debug("Reading LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance attendance = attendanceDAO.read(lessonId);
		LOG.debug("LessonAttendance retrieved: {}", attendance);
		
		return attendance;
	}

	@Override
	public LessonAttendance updateAttendanceForLesson(LessonAttendance attendance) {
		LOG.debug("Updating LessonAttendance: {}", attendance);
		
		LessonAttendance oldAttendance = attendanceDAO.update(attendance);
		LOG.debug("Old LessonAttendance retrieved: {}", oldAttendance);
		
		return oldAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(long lessonId) {
		LOG.debug("Deleting LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance deletedAttendance = attendanceDAO.delete(lessonId);
		LOG.debug("Deleted LessonAttendance: {}", deletedAttendance);
		
		return deletedAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance) {
		LOG.debug("Deleting LessonAttendance: {}", attendance);
		
		LessonAttendance attendanceBeforeDeletion = attendanceDAO.delete(attendance);
		LOG.debug("Attendance before deletion: {}", attendanceBeforeDeletion);
		
		return attendanceBeforeDeletion;
	}
	
	@Override
	public int deleteAttendanceForStudent(int studentId) {
		LOG.debug("Deleting LessonAttendance for Student with ID: {}", studentId);
		
		int deletedAttendanceAmount = attendanceDAO.deleteForStudent(studentId);
		LOG.debug("Deleted {} LessonAttendances", deletedAttendanceAmount);
		
		return deletedAttendanceAmount;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceServiceImpl(LessonAttendanceDAO attendanceDAO, LessonCodeService lessonCodeService, LessonCodeDAO lessonCodeDAO, StudentDAO studentDAO, Gmail mail) {
		this.attendanceDAO = attendanceDAO;
		this.lessonCodeService = lessonCodeService;
		this.lessonCodeDAO = lessonCodeDAO;
		this.studentDAO = studentDAO;
		this.mail = mail;
	}

	// PRIVATE
	
	private final LessonAttendanceDAO attendanceDAO;
	private final LessonCodeService lessonCodeService;
	private final LessonCodeDAO lessonCodeDAO;
	private final StudentDAO studentDAO;
	private final Gmail mail;
	
	private static final String EMAIL_TITLE = "Your COTEM lesson code";
	private static final String EMAIL_BODY = "Code: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonAttendanceService.class);

}
