package com.superum.db.group;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;
import com.superum.db.lesson.Lesson;
import com.superum.db.lesson.LessonService;

@Service
public class GroupServiceImpl implements GroupService {

	@Override
	public Group addGroup(Group group) {
		LOG.debug("Creating new Group: {}", group);
		
		Group newGroup = groupDAO.create(group);
		LOG.debug("New Group created: {}", newGroup);
		
		return newGroup;
	}

	@Override
	public Group findGroup(int id) {
		LOG.debug("Reading Group by ID: {}", id);
		
		Group group = groupDAO.read(id);
		LOG.debug("Group retrieved: {}", group);
		
		return group;
	}

	@Override
	public Group updateGroup(Group group) {
		LOG.debug("Updating Group: {}", group);
		
		Group oldGroup = groupDAO.update(group);
		LOG.debug("Old Group retrieved: {}", oldGroup);
		
		return oldGroup;
	}

	@Override
	public Group deleteGroup(int id) {
		LOG.debug("Deleting Group by ID: {}", id);
		
		List<Student> deletedStudents = studentService.deleteForGroup(id);
		LOG.debug("Deleted Students for Group: {}", deletedStudents);
		
		List<Lesson> deletedLessons = lessonService.deleteForGroup(id);
		LOG.debug("Deleted Lessons for Group: {}", deletedLessons);
		
		Group deletedGroup = groupDAO.delete(id);
		LOG.debug("Deleted Group: {}", deletedGroup);
		
		return deletedGroup;
	}

	@Override
	public List<Group> findGroupsForCustomer(int customerId) {
		LOG.debug("Reading Groups for Customer with ID: {}", customerId);
		
		List<Group> groupsForCustomer = groupQueries.readAllForCustomer(customerId);
		LOG.debug("Groups retrieved: {}", groupsForCustomer);
		
		return groupsForCustomer;
	}
	
	@Override
	public List<Group> findGroupsForTeacher(int teacherId) {
		LOG.debug("Reading Groups for Teacher with ID: {}", teacherId);
		
		List<Group> groupsForTeacher = groupDAO.readAllForTeacher(teacherId);
		LOG.debug("Groups retrieved: {}", groupsForTeacher);
		
		return groupsForTeacher;
	}
	
	@Override
	public List<Group> findGroupsForCustomerAndTeacher(int customerId, int teacherId) {
		LOG.debug("Reading Groups for Customer with ID '{}' and Teacher with ID '{}'", customerId, teacherId);
		
		List<Group> groupsForCustomerAndTeacher = groupQueries.readAllForCustomerAndTeacher(customerId, teacherId);
		LOG.debug("Groups retrieved: {}", groupsForCustomerAndTeacher);
		
		return groupsForCustomerAndTeacher;
	}

	// CONSTRUCTORS

	@Autowired
	public GroupServiceImpl(GroupDAO groupDAO, GroupQueries groupQueries, StudentService studentService, LessonService lessonService) {
		this.groupDAO = groupDAO;
		this.groupQueries = groupQueries;
		this.studentService = studentService;
		this.lessonService = lessonService;
	}

	// PRIVATE
	
	private final GroupDAO groupDAO;
	private final GroupQueries groupQueries;
	private final StudentService studentService;
	private final LessonService lessonService;
	
	private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
	
}
