package com.superum.api.customer;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request for a customer fails because the id used in the request
 * does not refer to any customers
 */
public class CustomerNotFoundException extends DatabaseException {

	public CustomerNotFoundException() {
		super();
	}

	public CustomerNotFoundException(String message) {
		super(message);
	}

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
