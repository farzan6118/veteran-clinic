package com.github.farzan6118.petclinic.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ClinicBadRequestException extends BaseAppException {

    public ClinicBadRequestException(String logMessage) {
        super(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.name(),
                logMessage
        );
    }

    public ClinicBadRequestException(String userMessage, String logMessage) {
        super(
                HttpStatus.BAD_REQUEST,
                userMessage,
                logMessage
        );
    }

    public ClinicBadRequestException() {
        super(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.name()
        );
    }
}
