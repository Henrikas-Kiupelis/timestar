package com.superum.db.lesson.attendance;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.config.Gmail;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentDAO;
import com.superum.db.lesson.attendance.code.CodeDAO;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Override
	public Attendance addAttendanceToLesson(Attendance attendance) {
		Attendance newAttendance = attendanceDAO.create(attendance);
		
		Map<Integer, Integer> codesForStudent = codeDAO.add(newAttendance);
		for (Map.Entry<Integer, Integer> codeForStudent : codesForStudent.entrySet()) {
			int studentId = codeForStudent.getKey();
			int code = codeForStudent.getValue();
			Student student = studentDAO.read(studentId);
			try {
				mail.send(student.getEmail(), "Your COTEM lesson code", "Code: " + code);
			} catch (MessagingException e) {
				codeDAO.find(attendance.getLessonId(), code);
				throw new IllegalStateException("Failed to send mail! Code aborted.", e);
			}
		}
		
		return newAttendance;
	}

	@Override
	public Attendance getAttendanceForLesson(long lessonId) {
		return attendanceDAO.read(lessonId);
	}

	@Override
	public Attendance updateAttendanceForLesson(Attendance attendance) {
		return attendanceDAO.update(attendance);
	}

	@Override
	public Attendance deleteAttendanceForLesson(long lessonId) {
		return attendanceDAO.delete(lessonId);
	}

	@Override
	public Attendance deleteAttendanceForLesson(Attendance attendance) {
		return attendanceDAO.delete(attendance);
	}

	// CONSTRUCTORS

	@Autowired
	public AttendanceServiceImpl(AttendanceDAO attendanceDAO, CodeDAO codeDAO, StudentDAO studentDAO, Gmail mail) {
		this.attendanceDAO = attendanceDAO;
		this.codeDAO = codeDAO;
		this.studentDAO = studentDAO;
		this.mail = mail;
	}

	// PRIVATE
	
	private final AttendanceDAO attendanceDAO;
	private final CodeDAO codeDAO;
	private final StudentDAO studentDAO;
	private final Gmail mail;

}
