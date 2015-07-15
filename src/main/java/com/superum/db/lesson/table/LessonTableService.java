package com.superum.db.lesson.table;

import java.sql.Date;

import org.springframework.stereotype.Service;

import com.superum.db.lesson.table.core.LessonTable;

@Service
public interface LessonTableService {

	public LessonTable lessonData(int amount, int offset, Date start, Date end, int partitionId);
	
}
