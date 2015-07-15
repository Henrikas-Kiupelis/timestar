package com.superum.db.group.student;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StudentService {

	Student addStudent(Student student, int partitionId);
	
	Student findStudent(int id, int partitionId);
	
	Student updateStudent(Student student, int partitionId);
	
	Student deleteStudent(int id, int partitionId);
	
	List<Student> findStudentsForCustomer(int customerId, int partitionId);
	
	List<Student> findStudentsForGroup(int groupId, int partitionId);

	List<Student> findStudentsForLesson(long lessonId, int partitionId);

	List<Student> deleteForCustomer(int customerId, int partitionId);

	List<Student> deleteForGroup(int groupId, int partitionId);
	
}
