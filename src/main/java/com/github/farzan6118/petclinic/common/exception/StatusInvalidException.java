package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class StatusInvalidException extends BaseAppException {
    public StatusInvalidException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public StatusInvalidException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}
