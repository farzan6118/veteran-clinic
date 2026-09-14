package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class GenericValidationException extends BaseAppException {
    public GenericValidationException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public GenericValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}
