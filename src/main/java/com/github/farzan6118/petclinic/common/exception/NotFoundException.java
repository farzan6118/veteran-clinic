package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseAppException {

    public NotFoundException(String message, String logMessage) {
        super(HttpStatus.NOT_FOUND, message, logMessage);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message, null);
    }
}