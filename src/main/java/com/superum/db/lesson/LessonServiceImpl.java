package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return lessonDAO.delete(id);
	}

	@Override
	public List<Lesson> findLessonsFor(TableBinder table, int id, Date start, Date end) {
		return lessonDAO.readAllFrom(table, id, start, end);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonServiceImpl(LessonDAO lessonDAO, LessonQueries lessonQueries) {
		this.lessonDAO = lessonDAO;
		this.lessonQueries = lessonQueries;
	}

	// PRIVATE
	
	private final LessonDAO lessonDAO;
	private final LessonQueries lessonQueries;

}
