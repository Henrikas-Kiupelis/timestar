package com.superum.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * Handles how non-specific exceptions are translated into HTTP responses
 * </pre>
 */
@ControllerAdvice
public class GeneralErrorHandler {

    @ExceptionHandler
    void handleInvalidCustomerException(InvalidRequestException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid request format, check your URL/JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(UnauthorizedRequestException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Couldn't authenticate user; " + e.getMessage());
    }

}
