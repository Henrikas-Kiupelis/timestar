package com.superum.api.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how Teacher specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class TeacherErrorHandler {

    @ExceptionHandler
    void handleInvalidCustomerException(InvalidTeacherException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Teacher format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(TeacherNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Teacher; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeCustomerDeleteException(UnsafeTeacherDeleteException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified Teacher because it is still being used; " + e.getMessage());
    }

    @ExceptionHandler
    void handleDuplicateTeacherException(DuplicateTeacherException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), "A teacher with specified email already exists; " + e.getMessage());
    }

}
