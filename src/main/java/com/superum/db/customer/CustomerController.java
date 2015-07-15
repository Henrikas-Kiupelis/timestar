package com.superum.db.customer;

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
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer addCustomer(Principal user, @RequestBody @Valid Customer customer) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.addCustomer(customer, partitionId);
	}
	
	@RequestMapping(value = "/customer/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer findCustomer(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.findCustomer(id, partitionId);
	}
	
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer updateCustomer(Principal user, @RequestBody @Valid Customer customer) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.updateCustomer(customer, partitionId);
	}
	
	@RequestMapping(value = "/customer/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer deleteCustomer(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.deleteCustomer(id, partitionId);
	}
	
	@RequestMapping(value = "/customer/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Customer> findCustomersForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.findCustomersForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/customer/all", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Customer> getAllCustomers(Principal user) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.getAllCustomers(partitionId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	// PRIVATE
	
	private final CustomerService customerService;

}
