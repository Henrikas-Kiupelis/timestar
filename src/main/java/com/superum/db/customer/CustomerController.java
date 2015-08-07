package com.superum.db.customer;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer addCustomer(Principal user, @RequestBody @Valid Customer customer) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.addCustomer(customer, partitionId);
	}
	
	@RequestMapping(value = "/customer/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer findCustomer(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.findCustomer(id, partitionId);
	}
	
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer updateCustomer(Principal user, @RequestBody @Valid Customer customer) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.updateCustomer(customer, partitionId);
	}
	
	@RequestMapping(value = "/customer/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer deleteCustomer(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.deleteCustomer(id, partitionId);
	}
	
	@RequestMapping(value = "/customer/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Customer> findCustomersForTeacher(Principal user, @PathVariable int teacherId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return customerService.findCustomersForTeacher(teacherId, partitionId);
	}
	
	@RequestMapping(value = "/customer/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
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
