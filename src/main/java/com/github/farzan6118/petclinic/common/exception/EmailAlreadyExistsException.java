package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseAppException {
    public EmailAlreadyExistsException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }
}
