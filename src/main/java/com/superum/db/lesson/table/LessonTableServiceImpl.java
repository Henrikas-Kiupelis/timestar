package com.superum.db.lesson.table;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.lesson.table.core.LessonTable;

@Service
public class LessonTableServiceImpl implements LessonTableService {

	@Override
	public LessonTable lessonData(Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableServiceImpl(LessonTableQueries lessonTableQueries) {
		this.lessonTableQueries = lessonTableQueries;
	}

	// PRIVATE

	private final LessonTableQueries lessonTableQueries;
	
}
