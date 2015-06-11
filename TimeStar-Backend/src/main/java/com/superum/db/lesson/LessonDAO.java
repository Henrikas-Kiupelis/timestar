package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface LessonDAO extends SimpleDAO<Lesson, Long> {

	List<Lesson> readAllFrom(TableBinder table, int id, Date start, Date end);
	
}
