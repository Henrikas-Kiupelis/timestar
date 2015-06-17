package com.superum.db.lesson.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Override
	public Attendance addAttendanceToLesson(Attendance attendance) {
		return attendanceDAO.create(attendance);
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
	public AttendanceServiceImpl(AttendanceDAO attendanceDAO) {
		this.attendanceDAO = attendanceDAO;
	}

	// PRIVATE
	
	private final AttendanceDAO attendanceDAO;

}
