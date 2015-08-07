package com.superum.api.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how Customer specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class CustomerErrorHandler {

    @ExceptionHandler
    void handleInvalidCustomerException(InvalidCustomerException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Customer format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(CustomerNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Customer; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeCustomerDeleteException(UnsafeCustomerDeleteException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified Customer because it is still being used; " + e.getMessage());
    }

}
