package com.github.farzan6118.petclinic.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 BAD_REQUEST
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex) {
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put(ex.getFieldName(), ex.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    // 400 BAD_REQUEST
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
            ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(ex.getConstraintViolations().stream().findFirst().get().getMessage());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    // 400 BAD_REQUEST
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        if (ex.getMessage() != null) {
            errorResponseDto.setMessage("format.error");
        } else {
            errorResponseDto.setMessage("format.error");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("file.size.passed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("Missing parameter: " + ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> {
                    String code = error.getCode();
                    if (code != null && code.startsWith("typeMismatch")) {
                        return "type.mismatch";
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(errors.getFirst());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingRequestHeaderException(
            MissingRequestHeaderException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("MISSING_REQUEST_HEADER: " + ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }


    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleJsonProcessingException(
            JsonProcessingException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("INVALID_JSON_FORMAT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
            AuthenticationException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }


    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationServiceExceptionException(
            AuthorizationServiceException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        log.warn("Unauthorized access: {}", ex.getMessage());
        errorResponseDto.setMessage("FORBIDDEN");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        log.warn("Access denied: {}", ex.getMessage());
        errorResponseDto.setMessage("ACCESS_DENIED");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            HttpRequestMethodNotSupportedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("METHOD_NOT_ALLOWED");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("DATA_INTEGRITY_VIOLATION");
        log.error("Database constraint violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("UNSUPPORTED_MEDIA_TYPE");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(errorResponseDto);
    }


    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleServiceUnavailableException(
            ServiceUnavailableException ex) {
//        Sentry.withScope(scope -> {
//            scope.setTag("exception_class", ex.getClass().toString());
//            scope.setExtra("exception_message", ex.getMessage());
//            scope.setExtra("exception_cause", String.valueOf(ex.getCause()));
//            Sentry.captureException(ex);
//        });
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        log.error("ServiceUnavailableException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
//        Sentry.withScope(scope -> {
//            scope.setTag("exception_class", ex.getClass().toString());
//            scope.setExtra("exception_message", ex.getMessage());
//            scope.setExtra("exception_cause", String.valueOf(ex.getCause()));
//            Sentry.captureException(ex);
//        });
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("internal.server.error");
        generateLogErrorWithDetails(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestPartException(Exception ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("missing.request.part");
        generateLogErrorWithDetails(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(NoResourceFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("resource.not.found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    private void generateLogErrorWithDetails(Exception ex) {
        Throwable rootCause = getRootCause(ex);
        StackTraceElement[] stackTraceElements = rootCause.getStackTrace();

        if (stackTraceElements == null || stackTraceElements.length == 0) {
            log.error("Exception occurred with no stack trace: Message: '{}', Cause: '{}'",
                    rootCause.getMessage(), rootCause.getCause(), rootCause);
            return;
        }

        StackTraceElement element = stackTraceElements[0];
        String fullClassName = element.getClassName();
        String simpleClassName = extractSimpleClassName(fullClassName);

        log.error("Exception occurred: message: '{}', fullClassName: '{}'," +
                        " simpleClassName: '{}', methodName: '{}', lineNumber: '{}'.",
                rootCause.getMessage(),
                fullClassName,
                simpleClassName,
                element.getMethodName(),
                element.getLineNumber());
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private String extractSimpleClassName(String fullClassName) {
        return (fullClassName != null && fullClassName.contains("."))
                ? fullClassName.substring(fullClassName.lastIndexOf('.') + 1)
                : fullClassName;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponseDto);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedOperationException(
            UnsupportedOperationException ex) {
//        Sentry.withScope(scope -> {
//            scope.setTag("exception_class", ex.getClass().toString());
//            scope.setExtra("exception_message", ex.getMessage());
//            scope.setExtra("exception_cause", String.valueOf(ex.getCause()));
//            Sentry.captureException(ex);
//        });
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(ex.getMessage());
        log.error("UnsupportedOperationException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

}
