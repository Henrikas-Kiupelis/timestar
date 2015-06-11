package com.superum.db.customer.group;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GroupService {

	Group addGroup(Group group);
	
	Group findGroup(int id);
	
	Group updateGroup(Group group);
	
	Group deleteGroup(int id);
	
	List<Group> findGroupsForCustomer(int customerId);
	
}
