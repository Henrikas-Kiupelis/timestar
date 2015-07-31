package com.superum.db.group;

import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;
import com.superum.db.lesson.Lesson;
import com.superum.db.lesson.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

	@Override
	public Group addGroup(Group group, int partitionId) {
		LOG.debug("Creating new Group: {}", group);
		
		Group newGroup = groupDAO.create(group, partitionId);
		LOG.debug("New Group created: {}", newGroup);
		
		return newGroup;
	}

	@Override
	public Group findGroup(int id, int partitionId) {
		LOG.debug("Reading Group by ID: {}", id);
		
		Group group = groupDAO.read(id, partitionId);
		LOG.debug("Group retrieved: {}", group);
		
		return group;
	}

	@Override
	public Group updateGroup(Group group, int partitionId) {
		LOG.debug("Updating Group: {}", group);
		
		Group oldGroup = groupDAO.update(group, partitionId);
		LOG.debug("Old Group retrieved: {}", oldGroup);
		
		return oldGroup;
	}

	@Override
	public Group deleteGroup(int id, int partitionId) {
		LOG.debug("Deleting Group by ID: {}", id);
		
		List<Student> deletedStudents = studentService.deleteForGroup(id, partitionId);
		LOG.debug("Deleted Students for Group: {}", deletedStudents);
		
		List<Lesson> deletedLessons = lessonService.deleteForGroup(id, partitionId);
		LOG.debug("Deleted Lessons for Group: {}", deletedLessons);
		
		Group deletedGroup = groupDAO.delete(id, partitionId);
		LOG.debug("Deleted Group: {}", deletedGroup);
		
		return deletedGroup;
	}

	@Override
	public List<Group> findGroupsForCustomer(int customerId, int partitionId) {
		LOG.debug("Reading Groups for Customer with ID: {}", customerId);
		
		List<Group> groupsForCustomer = groupDAO.readAllForCustomer(customerId, partitionId);
		LOG.debug("Groups retrieved: {}", groupsForCustomer);
		
		return groupsForCustomer;
	}
	
	@Override
	public List<Group> findGroupsForTeacher(int teacherId, int partitionId) {
		LOG.debug("Reading Groups for Teacher with ID: {}", teacherId);
		
		List<Group> groupsForTeacher = groupDAO.readAllForTeacher(teacherId, partitionId);
		LOG.debug("Groups retrieved: {}", groupsForTeacher);
		
		return groupsForTeacher;
	}
	
	@Override
	public List<Group> findGroupsForCustomerAndTeacher(int customerId, int teacherId, int partitionId) {
		LOG.debug("Reading Groups for Customer with ID '{}' and Teacher with ID '{}'", customerId, teacherId);
		
		List<Group> groupsForCustomerAndTeacher = groupDAO.readAllForCustomerAndTeacher(customerId, teacherId, partitionId);
		LOG.debug("Groups retrieved: {}", groupsForCustomerAndTeacher);
		
		return groupsForCustomerAndTeacher;
	}

	@Override
	public List<Group> all(int partitionId) {
        LOG.debug("Reading all Groups");

        List<Group> all = groupDAO.all(partitionId);
        LOG.debug("Groups retrieved: {}", all);

        return all;
    }

	// CONSTRUCTORS

	@Autowired
	public GroupServiceImpl(GroupDAO groupDAO, StudentService studentService, LessonService lessonService) {
		this.groupDAO = groupDAO;
		this.studentService = studentService;
		this.lessonService = lessonService;
	}

	// PRIVATE
	
	private final GroupDAO groupDAO;
	private final StudentService studentService;
	private final LessonService lessonService;
	
	private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
	
}
