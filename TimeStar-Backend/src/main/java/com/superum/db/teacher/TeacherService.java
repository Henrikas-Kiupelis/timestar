package com.superum.db.teacher;

import org.springframework.stereotype.Service;

@Service
public interface TeacherService {

	public Teacher addTeacher(Teacher teacher);
	
	public Teacher findTeacher(int id);
	
	public Teacher updateTeacher(Teacher teacher);
	
	public Teacher deleteTeacher(int id);

}
