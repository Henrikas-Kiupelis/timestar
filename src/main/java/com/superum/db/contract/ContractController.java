package com.superum.db.contract;

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
public class ContractController {

	@RequestMapping(value = "/contract/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Contract addContract(@RequestBody @Valid Contract contract) {
		return contractService.addContract(contract);
	}
	
	@RequestMapping(value = "/contract/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Contract findContract(@PathVariable int id) {
		return contractService.findContract(id);
	}
	
	@RequestMapping(value = "/contract/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Contract updateContract(@RequestBody @Valid Contract contract) {
		return contractService.updateContract(contract);
	}
	
	@RequestMapping(value = "/contract/delete/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Contract deleteContract(@PathVariable int id) {
		return contractService.deleteContract(id);
	}
	
	@RequestMapping(value = "/contract/teacher/{teacherId}/customer/{customerId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Contract findContractForTeacherAndCustomer(@PathVariable int teacherId, @PathVariable int customerId) {
		return contractService.findContractForTeacherAndCustomer(teacherId, customerId);
	}
	
	@RequestMapping(value = "/contract/teacher/{teacherId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public List<Contract> findContractsForTeacher(@PathVariable int teacherId) {
		return contractService.findContractsForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/contract/customer/{customerId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public List<Contract> findContractsForCustomer(@PathVariable int customerId) {
		return contractService.findContractsForCustomer(customerId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}
	
	// PRIVATE
	
	private final ContractService contractService;

}
