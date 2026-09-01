package com.github.farzan6118.petclinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseAppException extends RuntimeException {

    private final HttpStatus status;
    private final String userMessage;
    private final String logMessage;

    protected BaseAppException(
            HttpStatus status,
            String userMessage,
            String logMessage
    ) {
        super(logMessage);
        this.status = status;
        this.userMessage = userMessage;
        this.logMessage = logMessage;
    }
}
