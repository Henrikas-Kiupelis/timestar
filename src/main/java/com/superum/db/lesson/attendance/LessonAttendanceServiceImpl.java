package com.superum.db.lesson.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonAttendanceServiceImpl implements LessonAttendanceService {

	@Override
	public LessonAttendance addAttendanceToLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Adding attendance: {}", attendance);
		
		LessonAttendance newAttendance = attendanceDAO.create(attendance, partitionId);
		LOG.debug("Attendance added: {}", newAttendance);

		return newAttendance;
	}

	@Override
	public LessonAttendance getAttendanceForLesson(long lessonId, int partitionId) {
		LOG.debug("Reading LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance attendance = attendanceDAO.read(lessonId, partitionId);
		LOG.debug("LessonAttendance retrieved: {}", attendance);
		
		return attendance;
	}

	@Override
	public LessonAttendance updateAttendanceForLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Updating LessonAttendance: {}", attendance);
		
		LessonAttendance oldAttendance = attendanceDAO.update(attendance, partitionId);
		LOG.debug("Old LessonAttendance retrieved: {}", oldAttendance);
		
		return oldAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(long lessonId, int partitionId) {
		LOG.debug("Deleting LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance deletedAttendance = attendanceDAO.delete(lessonId, partitionId);
		LOG.debug("Deleted LessonAttendance: {}", deletedAttendance);
		
		return deletedAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Deleting LessonAttendance: {}", attendance);
		
		LessonAttendance attendanceBeforeDeletion = attendanceDAO.delete(attendance, partitionId);
		LOG.debug("Attendance before deletion: {}", attendanceBeforeDeletion);
		
		return attendanceBeforeDeletion;
	}
	
	@Override
	public int deleteAttendanceForStudent(int studentId, int partitionId) {
		LOG.debug("Deleting LessonAttendance for Student with ID: {}", studentId);
		
		int deletedAttendanceAmount = attendanceDAO.deleteForStudent(studentId, partitionId);
		LOG.debug("Deleted {} LessonAttendances", deletedAttendanceAmount);
		
		return deletedAttendanceAmount;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceServiceImpl(LessonAttendanceDAO attendanceDAO) {
		this.attendanceDAO = attendanceDAO;
	}

	// PRIVATE
	
	private final LessonAttendanceDAO attendanceDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonAttendanceService.class);

}
