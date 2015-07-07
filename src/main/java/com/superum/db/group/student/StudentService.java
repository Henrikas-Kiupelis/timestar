package com.superum.db.group.student;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StudentService {

	Student addStudent(Student student);
	
	Student findStudent(int id);
	
	Student updateStudent(Student student);
	
	Student deleteStudent(int id);
	
	List<Student> findStudentsForCustomer(int customerId);
	
	List<Student> findStudentsForGroup(int groupId);

	List<Student> findStudentsForLesson(long lessonId);

	List<Student> deleteForCustomer(int customerId);

	List<Student> deleteForGroup(int groupId);
	
}
