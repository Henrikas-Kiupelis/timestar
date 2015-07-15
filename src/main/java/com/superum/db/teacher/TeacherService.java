package com.superum.db.teacher;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TeacherService {

	Teacher addTeacher(Teacher teacher, int partitionId);
	
	Teacher findTeacher(int id, int partitionId);
	
	Teacher updateTeacher(Teacher teacher, int partitionId);
	
	Teacher deleteTeacher(int id, int partitionId);

	List<Teacher> getAllTeachers(int partitionId);

}
