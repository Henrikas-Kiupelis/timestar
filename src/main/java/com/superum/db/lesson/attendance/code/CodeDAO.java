package com.superum.db.lesson.attendance.code;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.superum.db.lesson.attendance.Attendance;

@Repository
public interface CodeDAO {

	int find(long lessonId, int code);
	
	Map<Integer, Integer> add(Attendance attendance);
	
}
