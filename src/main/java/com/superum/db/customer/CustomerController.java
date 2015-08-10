package com.superum.db.customer;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer addCustomer(PartitionAccount account, @RequestBody @Valid Customer customer) {
		return customerService.addCustomer(customer, account.partitionId());
	}
	
	@RequestMapping(value = "/customer/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer findCustomer(PartitionAccount account, @PathVariable int id) {
		return customerService.findCustomer(id, account.partitionId());
	}
	
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer updateCustomer(PartitionAccount account, @RequestBody @Valid Customer customer) {
		return customerService.updateCustomer(customer, account.partitionId());
	}
	
	@RequestMapping(value = "/customer/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Customer deleteCustomer(PartitionAccount account, @PathVariable int id) {
		return customerService.deleteCustomer(id, account.partitionId());
	}
	
	@RequestMapping(value = "/customer/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Customer> findCustomersForTeacher(PartitionAccount account, @PathVariable int teacherId) {
		return customerService.findCustomersForTeacher(teacherId, account.partitionId());
	}
	
	@RequestMapping(value = "/customer/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Customer> getAllCustomers(PartitionAccount account) {
		return customerService.getAllCustomers(account.partitionId());
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	// PRIVATE
	
	private final CustomerService customerService;

}
