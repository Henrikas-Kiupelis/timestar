package com.superum.db.customer;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

	Customer addCustomer(Customer customer);
	
	Customer findCustomer(int id);
	
	Customer updateCustomer(Customer customer);
	
	Customer deleteCustomer(int id);
	
	List<Customer> findCustomersForTeacher(int teacherId);

}
