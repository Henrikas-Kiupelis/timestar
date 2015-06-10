package com.superum.db.customer;

import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

	public Customer addNewCustomer(Customer customer);
	
	public Customer findCustomer(int id);
	
	public Customer updateCustomer(Customer customer);
	
	public Customer deleteCustomer(int id);

}
