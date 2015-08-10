package com.superum.db.group;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class GroupController {

	@RequestMapping(value = "/group/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Group addGroup(PartitionAccount account, @RequestBody @Valid Group group) {
		return groupService.addGroup(group, account.partitionId());
	}
	
	@RequestMapping(value = "/group/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Group findGroup(PartitionAccount account, @PathVariable int id) {
		return groupService.findGroup(id, account.partitionId());
	}
	
	@RequestMapping(value = "/group/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Group updateGroup(PartitionAccount account, @RequestBody @Valid Group group) {
		return groupService.updateGroup(group, account.partitionId());
	}
	
	@RequestMapping(value = "/group/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Group deleteGroup(PartitionAccount account, @PathVariable int id) {
		return groupService.deleteGroup(id, account.partitionId());
	}
	
	@RequestMapping(value = "/group/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Group> findGroupsForCustomer(PartitionAccount account, @PathVariable int customerId) {
		return groupService.findGroupsForCustomer(customerId, account.partitionId());
	}
	
	@RequestMapping(value = "/group/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Group> findGroupsForTeacher(PartitionAccount account, @PathVariable int teacherId) {
		return groupService.findGroupsForTeacher(teacherId, account.partitionId());
	}
	
	@RequestMapping(value = "/group/customer/{customerId:[\\d]+}/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Group> findGroupsForCustomerAndTeacher(PartitionAccount account, @PathVariable int customerId, @PathVariable int teacherId) {
		return groupService.findGroupsForCustomerAndTeacher(customerId, teacherId, account.partitionId());
	}

	@RequestMapping(value = "/group/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Group> all(PartitionAccount account) {
		return groupService.all(account.partitionId());
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	// PRIVATE
	
	private final GroupService groupService;

}
