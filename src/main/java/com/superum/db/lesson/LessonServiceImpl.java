package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.exception.DatabaseException;
import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.db.lesson.attendance.code.LessonCodeService;

@Service
public class LessonServiceImpl implements LessonService {

	@Override
	public Lesson addLesson(Lesson lesson) {
		LOG.debug("Creating new Lesson: {}", lesson);
		
		if (lessonDAO.isOverlapping(lesson))
			throw new DatabaseException("This lesson cannot be added because it is overlapping with others!");
		
		Lesson newLesson = lessonDAO.create(lesson);
		LOG.debug("New Lesson created: {}", newLesson);
		
		return newLesson;
	}

	@Override
	public Lesson findLesson(long id) {
		LOG.debug("Reading Lesson by ID: {}");
		
		Lesson lesson = lessonDAO.read(id);
		LOG.debug("Lesson retrieved: {}", lesson);
		
		return lesson;
	}

	@Override
	public Lesson updateLesson(Lesson lesson) {
		LOG.debug("Updating lesson: {}");
		
		if (lessonDAO.isOverlapping(lesson))
			throw new DatabaseException("This lesson cannot be updated because it is overlapping with others!");
		
		Lesson oldLesson = lessonDAO.update(lesson);
		LOG.debug("Old Lesson retrieved: {}", oldLesson);
		
		return oldLesson;
	}

	@Override
	public Lesson deleteLesson(long id) {
		LOG.debug("Deleting Lesson by ID: {}", id);
		
		LessonAttendance deletedAttendance = lessonAttendanceService.deleteAttendanceForLesson(id);
		LOG.debug("Deleted LessonAttendance: {}", deletedAttendance);
		
		int deletedCodeCount = lessonCodeService.deleteCodesForLesson(id);
		LOG.debug("Deleted {} LessonCodes", deletedCodeCount);
		
		Lesson deletedLesson = lessonDAO.delete(id);
		LOG.debug("Deleted Lesson: {}", deletedLesson);
		
		return deletedLesson;
	}

	@Override
	public List<Lesson> findLessonsForTeacher(int teacherId, Date start, Date end) {
		LOG.debug("Reading Lessons for Teacher with ID '{}' on dates from {} to {}", teacherId, start, end);
		
		List<Lesson> lessonsForTeacher = lessonDAO.readAllForTeacher(teacherId, start, end);
		LOG.debug("Lessons retrieved: {}", lessonsForTeacher);
		
		return lessonsForTeacher;
	}
	
	@Override
	public List<Lesson> findLessonsForGroup(int groupId, Date start, Date end) {
		LOG.debug("Reading Lessons for Group with ID '{}' on dates from {} to {}", groupId, start, end);
		
		List<Lesson> lessonsForGroup = lessonDAO.readAllForGroup(groupId, start, end);
		LOG.debug("Lessons retrieved: {}", lessonsForGroup);
		
		return lessonsForGroup;
	}
	
	@Override
	public List<Lesson> findLessonsForCustomer(int customerId, Date start, Date end) {
		LOG.debug("Reading Lessons for Customer with ID '{}' on dates from {} to {}", customerId, start, end);
		
		List<Lesson> lessonsForCustomer = lessonQueries.readAllForCustomer(customerId, start, end);
		LOG.debug("Lessons retrieved: {}", lessonsForCustomer);
		
		return lessonsForCustomer;
	}
	
	@Override
	public List<Lesson> deleteForTeacher(int teacherId) {
		LOG.debug("Deleting Lessons for Teacher with ID: {}", teacherId);
		
		List<Lesson> old = findLessonsForTeacher(teacherId, null, null);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(this::deleteLesson);
		LOG.debug("Deleted Lessons: {}", old);
			
		return old;
	}

	@Override
	public List<Lesson> deleteForGroup(int groupId) {
		LOG.debug("Deleting Lessons for Group with ID: {}", groupId);
		
		List<Lesson> old = findLessonsForGroup(groupId, null, null);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(this::deleteLesson);
		LOG.debug("Deleted Lessons: {}", old);
			
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonServiceImpl(LessonDAO lessonDAO, LessonQueries lessonQueries, LessonAttendanceService lessonAttendanceService, LessonCodeService lessonCodeService) {
		this.lessonDAO = lessonDAO;
		this.lessonQueries = lessonQueries;
		this.lessonAttendanceService = lessonAttendanceService;
		this.lessonCodeService = lessonCodeService;
	}

	// PRIVATE
	
	private final LessonDAO lessonDAO;
	private final LessonQueries lessonQueries;
	private final LessonAttendanceService lessonAttendanceService;
	private final LessonCodeService lessonCodeService;
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);

}
