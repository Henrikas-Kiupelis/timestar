package com.superum.db.group;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GroupService {

	Group addGroup(Group group, int partitionId);
	
	Group findGroup(int id, int partitionId);
	
	Group updateGroup(Group group, int partitionId);
	
	Group deleteGroup(int id, int partitionId);
	
	List<Group> findGroupsForCustomer(int customerId, int partitionId);
	
	List<Group> findGroupsForTeacher(int teacherId, int partitionId);
	
	List<Group> findGroupsForCustomerAndTeacher(int customerId, int teacherId, int partitionId);
	
}
