package com.lbd.app.tournament.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.lbd.app.tournament.dto.ErrorResponseDTO;

class GlobalExceptionHandlerTest {

    private static class DummyController {
        @SuppressWarnings("unused")
        void endpoint(Long stageId) {
        }

        @SuppressWarnings("unused")
        void endpointWithValidation(String name) {
        }
    }

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldBuildNotFoundErrorResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/matches/stages/99");

        ErrorResponseDTO response = handler.handleNotFound(
                new ResourceNotFoundException("Stage not found with id: 99"),
                request).getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.status());
        assertEquals("Not Found", response.error());
        assertEquals("Stage not found with id: 99", response.message());
        assertEquals("/api/matches/stages/99", response.path());
    }

    @Test
    void shouldBuildBadRequestErrorResponseForTypeMismatch() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/matches/stages/abc");

        Method method;
        try {
            method = DummyController.class.getDeclaredMethod("endpoint", Long.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        MethodParameter parameter = new MethodParameter(method, 0);

        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "stageId", parameter, null);

        ErrorResponseDTO response = handler.handleTypeMismatch(ex, request).getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Bad Request", response.error());
        assertEquals("Invalid value for parameter 'stageId'.", response.message());
        assertEquals("/api/matches/stages/abc", response.path());
    }

    @Test
    void shouldBuildBadRequestErrorResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/matches/stages/1");

        ErrorResponseDTO response = handler.handleBadRequest(
                new BadRequestException("Invalid request."),
                request).getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Bad Request", response.error());
        assertEquals("Invalid request.", response.message());
        assertEquals("/api/matches/stages/1", response.path());
    }

    @Test
    void shouldBuildInternalServerErrorResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/matches/stages/1");

        ErrorResponseDTO response = handler.handleUnhandled(
                new RuntimeException("boom"),
                request).getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
        assertEquals("Internal Server Error", response.error());
        assertEquals("An unexpected error occurred. Please contact support.", response.message());
        assertEquals("/api/matches/stages/1", response.path());
    }

    @Test
    void shouldBuildValidationErrorResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/user");

        Method method;
        try {
            method = DummyController.class.getDeclaredMethod("endpointWithValidation", String.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "must not be blank"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
        ErrorResponseDTO response = handler.handleValidation(ex, request).getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Bad Request", response.error());
        assertEquals("Validation failed.", response.message());
        assertEquals("/api/user", response.path());
    }
}


