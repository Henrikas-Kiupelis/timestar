package com.superum.api.v2.lesson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how lesson specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class LessonErrorHandler {

    @ExceptionHandler
    void handleInvalidLessonException(InvalidLessonException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid lesson format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid lesson format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleLessonNotFoundException(LessonNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot find the specified lesson;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified lesson; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeLessonDeleteException(UnsafeLessonDeleteException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot delete the specified lesson because it is still being used;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified lesson because it is still being used; " + e.getMessage());
    }

    @ExceptionHandler
    void handleOverlappingLessonException(OverlappingLessonException e, HttpServletResponse response) throws IOException {
        LOG.error("Provided lesson is overlapping;", e);
        response.sendError(HttpStatus.CONFLICT.value(), "Provided lesson is overlapping; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(LessonErrorHandler.class);

}
