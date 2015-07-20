package com.superum.api.customer;

import com.superum.exception.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when the a request for a Customer fails because the id used in the request
 * does not refer to any Customers
 * </pre>
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Cannot find the specified Customer")
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
