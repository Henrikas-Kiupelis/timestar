package com.superum.db.group;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GroupService {

	Group addGroup(Group group);
	
	Group findGroup(int id);
	
	Group updateGroup(Group group);
	
	Group deleteGroup(int id);
	
	List<Group> findGroupsForCustomer(int customerId);
	
	List<Group> findGroupsForTeacher(int teacherId);
	
	List<Group> findGroupsForCustomerAndTeacher(int customerId, int teacherId);
	
}
