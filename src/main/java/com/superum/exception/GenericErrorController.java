package com.superum.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericErrorController implements ErrorController {

    @RequestMapping(value = PATH)
    public ResponseEntity<String> error() {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    // PRIVATE

    private static final String PATH = "/error";

}