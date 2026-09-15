package com.github.farzan6118.petclinic.common.exception;

import org.springframework.http.HttpStatus;

public class FailedToSendEmailException extends BaseAppException {
    public FailedToSendEmailException(String message, String logMessage) {
        super(HttpStatus.BAD_REQUEST, message, logMessage);
    }

    public FailedToSendEmailException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}
