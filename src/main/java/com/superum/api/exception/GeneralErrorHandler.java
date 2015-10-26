package com.superum.api.exception;

import org.joda.time.IllegalFieldValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how non-specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class GeneralErrorHandler {

    @ExceptionHandler
    void handleInvalidRequestException(InvalidRequestException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid request format, check your URL/JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid request format, check your URL/JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnauthorizedRequestException(UnauthorizedRequestException e, HttpServletResponse response) throws IOException {
        LOG.error("You are not authorized to use this request;", e);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not authorized to use this request; " + e.getMessage());
    }

    @ExceptionHandler
    void handleIllegalFieldValueException(IllegalFieldValueException e, HttpServletResponse response) throws Exception {
        LOG.error("Invalid date or time field, check your URL/JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid date or time field, check your URL/JSON; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(GeneralErrorHandler.class);

}
