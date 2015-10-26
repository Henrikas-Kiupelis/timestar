package com.superum.api.exception;

/**
 * This exception is thrown when an request is made, but it is not authorized due to exceptional conditions. i.e.
 * attempting to change a password of a different user
 */
public class UnauthorizedRequestException extends RuntimeException {

    public UnauthorizedRequestException() {
        super();
    }

    public UnauthorizedRequestException(String message) {
        super(message);
    }

    public UnauthorizedRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedRequestException(Throwable cause) {
        super(cause);
    }

}
