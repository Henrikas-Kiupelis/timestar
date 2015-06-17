package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface AttendanceDAO extends SimpleDAO<Attendance, Long> {

	Attendance delete(Attendance attendance);
	
}
