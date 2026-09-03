package com.github.farzan6118.petclinic.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseAppException {
    public ResourceNotFoundException(HttpStatus httpStatus, String message, String logMessage) {
        super(httpStatus, message, logMessage);
    }

    public ResourceNotFoundException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message, message);
    }
}
