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
				LOG.debug("Sent email to student {}: title - {}; body - {}", student, EMAIL_TITLE, fullBody);
			} catch (MessagingException e) {
				lessonCodeDAO.find(attendance.getLessonId(), code);
				throw new IllegalStateException("Failed to send mail! Code aborted.", e);
			}
		}
		
		return newAttendance;
	}

	@Override
	public LessonAttendance getAttendanceForLesson(long lessonId) {
		return attendanceDAO.read(lessonId);
	}

	@Override
	public LessonAttendance updateAttendanceForLesson(LessonAttendance attendance) {
		return attendanceDAO.update(attendance);
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(long lessonId) {
		return attendanceDAO.delete(lessonId);
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance) {
		return attendanceDAO.delete(attendance);
	}
	
	@Override
	public int deleteAttendanceForStudent(int studentId) {
		return attendanceDAO.deleteForStudent(studentId);
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
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonAttendanceServiceImpl.class);

}
