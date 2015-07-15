package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface LessonDAO extends SimpleDAO<Lesson, Long> {

	List<Lesson> readAllForTeacher(int teacherId, Date start, Date end);
	
	List<Lesson> readAllForGroup(int groupId, Date start, Date end);
	
	boolean isOverlapping(Lesson lesson);
	
}
