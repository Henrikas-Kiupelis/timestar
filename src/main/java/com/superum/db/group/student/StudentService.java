package com.superum.db.group.student;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StudentService {

	public Student addStudent(Student student);
	
	public Student findStudent(int id);
	
	public Student updateStudent(Student student);
	
	public Student deleteStudent(int id);
	
	public List<Student> findStudentsForCustomer(int customerId);
	
	public List<Student> findStudentsForGroup(int groupId);

	public List<Student> findStudentsForLesson(long lessonId);
	
}
