package com.github.farzan6118.petclinic.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void applicationExceptionsKeepStatusAndExposeUserMessage() {
        MockHttpServletRequest request = request();
        ConflictException exception = new ConflictException("That email is already registered", "duplicate email");

        var response = handler.handleApplicationException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().status());
        assertEquals("That email is already registered", response.getBody().message());
        assertEquals("/owners", response.getBody().path());
    }

    @Test
    void integrityErrorsDoNotExposeDatabaseDetails() {
        MockHttpServletRequest request = request();
        var response = handler.handleDataIntegrityViolation(
                new org.springframework.dao.DataIntegrityViolationException("private database detail"), request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Request conflicts with existing data", response.getBody().message());
    }

    @Test
    void unexpectedErrorsReturnGenericServerMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/visits");

        var response = handler.handleException(new IllegalStateException("private implementation detail"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().message());
        assertEquals("/visits", response.getBody().path());
    }

    private MockHttpServletRequest request() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/owners");
        return request;
    }
}
