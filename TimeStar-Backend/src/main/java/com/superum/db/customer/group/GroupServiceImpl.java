package com.superum.db.customer.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

	@Override
	public Group addNewGroup(Group group) {
		return groupDAO.create(group);
	}

	@Override
	public Group findGroup(int id) {
		return groupDAO.read(id);
	}

	@Override
	public Group updateGroup(Group group) {
		return groupDAO.update(group);
	}

	@Override
	public Group deleteGroup(int id) {
		return groupDAO.delete(id);
	}

	@Override
	public List<Group> findGroupsForCustomer(int customerId) {
		return groupDAO.readAllFor(customerId);
	}

	// CONSTRUCTORS

	@Autowired
	public GroupServiceImpl(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	// PRIVATE
	
	private final GroupDAO groupDAO;

}
