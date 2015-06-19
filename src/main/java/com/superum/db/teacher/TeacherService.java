package com.superum.db.teacher;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TeacherService {

	Teacher addTeacher(Teacher teacher);
	
	Teacher findTeacher(int id);
	
	Teacher updateTeacher(Teacher teacher);
	
	Teacher deleteTeacher(int id);

	List<Teacher> getAllTeachers();

}
