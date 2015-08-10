package com.superum.db.teacher;

import com.superum.helper.PartitionAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {

	Teacher addTeacher(Teacher teacher, PartitionAccount account);
	
	Teacher findTeacher(int id, int partitionId);
	
	Teacher updateTeacher(Teacher teacher, int partitionId);
	
	Teacher deleteTeacher(int id, PartitionAccount account);

	List<Teacher> getAllTeachers(int partitionId);

}
