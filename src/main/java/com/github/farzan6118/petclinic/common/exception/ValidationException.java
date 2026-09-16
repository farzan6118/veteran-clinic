package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseAppException {

    public ValidationException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}