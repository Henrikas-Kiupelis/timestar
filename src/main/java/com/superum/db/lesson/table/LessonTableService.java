package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.LessonTable;
import org.springframework.stereotype.Service;

@Service
public interface LessonTableService {

	LessonTable lessonData(int amount, int offset, long start, long end, int partitionId);
	
}
