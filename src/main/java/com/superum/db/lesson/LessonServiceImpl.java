package com.superum.db.lesson;

import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

	@Override
	public Lesson addLesson(Lesson lesson, int partitionId) {
		LOG.debug("Creating new Lesson: {}", lesson);
		
		if (lessonDAO.isOverlapping(lesson, partitionId))
			throw new DatabaseException("This lesson cannot be added because it is overlapping with others!");
		
		Lesson newLesson = lessonDAO.create(lesson, partitionId);
		LOG.debug("New Lesson created: {}", newLesson);
		
		return newLesson;
	}

	@Override
	public Lesson findLesson(long id, int partitionId) {
		LOG.debug("Reading Lesson by ID: {}", id);
		
		Lesson lesson = lessonDAO.read(id, partitionId);
		LOG.debug("Lesson retrieved: {}", lesson);
		
		return lesson;
	}

	@Override
	public Lesson updateLesson(Lesson lesson, int partitionId) {
		LOG.debug("Updating lesson: {}", lesson);
		
		if (lessonDAO.isOverlapping(lesson, partitionId))
			throw new DatabaseException("This lesson cannot be updated because it is overlapping with others!");
		
		Lesson oldLesson = lessonDAO.update(lesson, partitionId);
		LOG.debug("Old Lesson retrieved: {}", oldLesson);
		
		return oldLesson;
	}

	@Override
	public Lesson deleteLesson(long id, int partitionId) {
		LOG.debug("Deleting Lesson by ID: {}", id);
		
		LessonAttendance deletedAttendance = lessonAttendanceService.deleteAttendanceForLesson(id, partitionId);
		LOG.debug("Deleted LessonAttendance: {}", deletedAttendance);
		
		Lesson deletedLesson = lessonDAO.delete(id, partitionId);
		LOG.debug("Deleted Lesson: {}", deletedLesson);
		
		return deletedLesson;
	}

	@Override
	public List<Lesson> findLessonsForTable(String table, int id, long start, long end, int partitionId) {
		LOG.debug("Reading Lessons for {} with ID {} at times from {} to {}", table, id, start, end);

        List<Lesson> lessons;
        switch (table) {
            case TEACHER_TABLE:
                lessons = lessonDAO.readAllForTeacher(id, start, end, partitionId);
                break;
            case CUSTOMER_TABLE:
                lessons = lessonQueries.readAllForCustomer(id, start, end, partitionId);
                break;
            case GROUP_TABLE:
                lessons = lessonDAO.readAllForGroup(id, start, end, partitionId);
                break;
            default:
                throw new AssertionError("This should never happen, because request mapping will " +
                        "filter out all other possibilities");
        }
		LOG.debug("Lessons retrieved: {}", lessons);
		
		return lessons;
	}

	@Override
	public List<Lesson> deleteForTeacher(int teacherId, int partitionId) {
		LOG.debug("Deleting Lessons for Teacher with ID: {}", teacherId);
		
		List<Lesson> old = findLessonsForTable(TEACHER_TABLE, teacherId, 0, Long.MAX_VALUE, partitionId);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(lessonId -> deleteLesson(lessonId, partitionId));
		LOG.debug("Deleted Lessons: {}", old);
			
		return old;
	}

	@Override
	public List<Lesson> deleteForGroup(int groupId, int partitionId) {
		LOG.debug("Deleting Lessons for Group with ID: {}", groupId);
		
		List<Lesson> old = findLessonsForTable(GROUP_TABLE, groupId, 0, Long.MAX_VALUE, partitionId);
		
		old.stream()
			.mapToLong(Lesson::getId)
			.forEach(lessonId -> deleteLesson(lessonId, partitionId));
		LOG.debug("Deleted Lessons: {}", old);
			
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonServiceImpl(LessonDAO lessonDAO, LessonQueries lessonQueries, LessonAttendanceService lessonAttendanceService) {
		this.lessonDAO = lessonDAO;
		this.lessonQueries = lessonQueries;
		this.lessonAttendanceService = lessonAttendanceService;
	}

	// PRIVATE
	
	private final LessonDAO lessonDAO;
	private final LessonQueries lessonQueries;
	private final LessonAttendanceService lessonAttendanceService;

    private static final String TEACHER_TABLE = "teacher";
    private static final String CUSTOMER_TABLE = "customer";
    private static final String GROUP_TABLE = "group";

	private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);

}
