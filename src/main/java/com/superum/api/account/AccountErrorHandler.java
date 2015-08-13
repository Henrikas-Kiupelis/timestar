package com.superum.api.account;

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
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Account format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(AccountNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Account; " + e.getMessage());
    }

}
