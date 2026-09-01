package com.lbd.app.tournament.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BadRequestExceptionTest {

    @Test
    void shouldCreateBadRequestExceptionWithMessage() {
        BadRequestException ex = new BadRequestException("Invalid request");
        assertEquals("Invalid request", ex.getMessage());
    }
}

