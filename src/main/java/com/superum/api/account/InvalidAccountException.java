package com.superum.api.account;

import com.superum.api.exception.InvalidRequestException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of Account
 * does not match any of the use cases
 */
public class InvalidAccountException extends InvalidRequestException {

	public InvalidAccountException() {
		super();
	}

	public InvalidAccountException(String message) {
		super(message);
	}

	public InvalidAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccountException(Throwable cause) {
		super(cause);
	}
	
}
