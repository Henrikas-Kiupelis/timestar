package com.superum.db.customer;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

	Customer addCustomer(Customer customer, int partitionId);
	
	Customer findCustomer(int id, int partitionId);
	
	Customer updateCustomer(Customer customer, int partitionId);
	
	Customer deleteCustomer(int id, int partitionId);
	
	List<Customer> findCustomersForTeacher(int teacherId, int partitionId);

	List<Customer> getAllCustomers(int partitionId);

}
