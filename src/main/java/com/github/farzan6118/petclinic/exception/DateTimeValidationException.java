package com.github.farzan6118.petclinic.exception;

import org.springframework.http.HttpStatus;

public class DateTimeValidationException extends BaseAppException {
    public DateTimeValidationException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public DateTimeValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}
