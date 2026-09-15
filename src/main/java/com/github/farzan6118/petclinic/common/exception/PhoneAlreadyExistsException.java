package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class PhoneAlreadyExistsException extends BaseAppException {
    public PhoneAlreadyExistsException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }
}
