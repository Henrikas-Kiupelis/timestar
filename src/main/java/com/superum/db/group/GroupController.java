package com.superum.db.group;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class GroupController {

	@RequestMapping(value = "/group/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Group addGroup(Principal user, @RequestBody @Valid Group group) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.addGroup(group, partitionId);
	}
	
	@RequestMapping(value = "/group/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Group findGroup(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.findGroup(id, partitionId);
	}
	
	@RequestMapping(value = "/group/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Group updateGroup(Principal user, @RequestBody @Valid Group group) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.updateGroup(group, partitionId);
	}
	
	@RequestMapping(value = "/group/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Group deleteGroup(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.deleteGroup(id, partitionId);
	}
	
	@RequestMapping(value = "/group/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Group> findGroupsForCustomer(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.findGroupsForCustomer(customerId, partitionId);
	}
	
	@RequestMapping(value = "/group/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Group> findGroupsForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.findGroupsForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/group/customer/{customerId:[\\d]+}/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Group> findGroupsForCustomerAndTeacher(Principal user, @PathVariable int customerId, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return groupService.findGroupsForCustomerAndTeacher(customerId, teacherId, partitionId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	// PRIVATE
	
	private final GroupService groupService;

}
