package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.LessonTable;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public interface LessonTableService {

	LessonTable lessonData(int amount, int offset, Date start, Date end, int partitionId);
	
}
