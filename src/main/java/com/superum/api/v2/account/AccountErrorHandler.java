package com.superum.api.v2.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how Account specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class AccountErrorHandler {

    @ExceptionHandler
    void handleInvalidCustomerException(InvalidAccountException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid Account format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Account format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(AccountNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot find the specified Account;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Account; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(AccountErrorHandler.class);

}
