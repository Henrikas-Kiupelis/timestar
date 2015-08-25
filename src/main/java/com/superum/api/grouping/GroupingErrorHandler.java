package com.superum.api.grouping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how grouping specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class GroupingErrorHandler {

    @ExceptionHandler
    void handleInvalidGroupingException(InvalidGroupingException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid grouping format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleGroupingNotFoundException(GroupingNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified grouping; " + e.getMessage());
    }

    @ExceptionHandler
    void handleDuplicateGroupingException(DuplicateGroupingException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), "Specified grouping already exists; " + e.getMessage());
    }

}
