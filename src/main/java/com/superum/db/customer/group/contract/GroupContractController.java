package com.superum.db.customer.group.contract;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class GroupContractController {

	@RequestMapping(value = "/group/contract/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public GroupContract addContract(@RequestBody @Valid GroupContract contract) {
		return contractService.addContract(contract);
	}
	
	@RequestMapping(value = "/group/contract/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public GroupContract findContract(@PathVariable int id) {
		return contractService.findContract(id);
	}
	
	@RequestMapping(value = "/group/contract/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public GroupContract updateContract(@RequestBody @Valid GroupContract contract) {
		return contractService.updateContract(contract);
	}
	
	@RequestMapping(value = "/group/contract/delete/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public GroupContract deleteContract(@PathVariable int id) {
		return contractService.deleteContract(id);
	}
	
	@RequestMapping(value = "/group/contract/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public List<GroupContract> findContractsForGroup(@PathVariable int groupId) {
		return contractService.findContractsForGroup(groupId);
	}
	
	@RequestMapping(value = "/group/contract/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public List<GroupContract> findContractsForCustomer(@PathVariable int customerId) {
		return contractService.findContractsForCustomer(customerId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public GroupContractController(GroupContractService contractService) {
		this.contractService = contractService;
	}
	
	// PRIVATE
	
	private final GroupContractService contractService;

}
