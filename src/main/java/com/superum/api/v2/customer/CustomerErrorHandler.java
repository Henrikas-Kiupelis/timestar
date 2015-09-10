package com.superum.api.v2.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        LOG.error("Invalid Customer format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Customer format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(CustomerNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot find the specified Customer;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Customer; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeCustomerDeleteException(UnsafeCustomerDeleteException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot delete the specified Customer because it is still being used;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified Customer because it is still being used; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(CustomerErrorHandler.class);

}
