package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.db.lesson.attendance.code.LessonCodeService;

@Service
public class LessonServiceImpl implements LessonService {

	@Override
	public Lesson addLesson(Lesson lesson) {
		return lessonDAO.create(lesson);
	}

	@Override
	public Lesson findLesson(long id) {
		return lessonDAO.read(id);
	}

	@Override
	public Lesson updateLesson(Lesson lesson) {
		return lessonDAO.update(lesson);
	}

	@Override
	public Lesson deleteLesson(long id) {
		lessonAttendanceService.deleteAttendanceForLesson(id);
		lessonCodeService.deleteCodesForLesson(id);
		return lessonDAO.delete(id);
	}

	@Override
	public List<Lesson> findLessonsForTeacher(int teacherId, Date start, Date end) {
		return lessonDAO.readAllForTeacher(teacherId, start, end);
	}
	
	@Override
	public List<Lesson> findLessonsForGroup(int groupId, Date start, Date end) {
		return lessonDAO.readAllForGroup(groupId, start, end);
	}
	
	@Override
	public List<Lesson> findLessonsForCustomer(int customerId, Date start, Date end) {
		return lessonQueries.readAllForCustomer(customerId, start, end);
	}
	
	@Override
	public List<Lesson> deleteForTeacher(int teacherId) {
		List<Lesson> old = findLessonsForTeacher(teacherId, null, null);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(this::deleteLesson);
			
		return old;
	}

	@Override
	public List<Lesson> deleteForGroup(int groupId) {
		List<Lesson> old = findLessonsForGroup(groupId, null, null);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(this::deleteLesson);
			
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

}
