package com.github.farzan6118.petclinic.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ErrorResponseDto> handle(BaseAppException ex, HttpServletRequest request) {

        if (ex.getLogMessage() != null) {
            log.warn(ex.getLogMessage());
        }

        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponseDto(
                        ex.getStatus().value(),
                        ex.getStatus().getReasonPhrase(),
                        ex.getUserMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String message = errors.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining(", "));

        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {

        log.warn(
                "HTTP exception. status={}, reason={}, path={}",
                ex.getStatusCode(),
                ex.getReason(),
                request.getRequestURI()
        );

        ErrorResponseDto response = new ErrorResponseDto(
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        log.error(
                "Unexpected runtime exception. path={}",
                request.getRequestURI(),
                ex
        );

        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Something went wrong",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error(
                "Unexpected exception. path={}",
                request.getRequestURI(),
                ex
        );

        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Something went wrong",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
