package com.superum.api.customer;

import com.superum.api.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of Customer
 * does not match any of the use cases
 * </pre>
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Customer format, check your JSON")
public class InvalidCustomerException extends InvalidRequestException {

	public InvalidCustomerException() {
		super();
	}

	public InvalidCustomerException(String message) {
		super(message);
	}

	public InvalidCustomerException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCustomerException(Throwable cause) {
		super(cause);
	}
	
}
