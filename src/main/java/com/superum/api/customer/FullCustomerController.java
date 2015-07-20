package com.superum.api.customer;

import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerService;
import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

/**
 * <pre>
 * API v2
 * Manages all requests for Customers
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly anyway)
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2")
public class FullCustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public FullCustomer insertCustomer(Principal user, @RequestBody @Valid FullCustomer customer) {
		int partitionId = PrincipalUtils.partitionId(user);
		return service.insert(customer, partitionId);
	}


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
	public FullCustomerController(CustomerService customerService, FullCustomerService service) {
		this.customerService = customerService;
		this.service = service;
	}
	
	// PRIVATE
	
	private final CustomerService customerService;
	private final FullCustomerService service;

}
