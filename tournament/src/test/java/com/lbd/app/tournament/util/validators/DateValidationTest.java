package com.lbd.app.tournament.util.validators;


import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DateValidatorTest {

    private DateValidator dateValidator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        dateValidator = new DateValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenValueIsNull() {
        boolean result = dateValidator.isValid(null, context);
        assertTrue(result);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenValueIsEmptyOrBlank() {
        assertTrue(dateValidator.isValid("", context));
        assertTrue(dateValidator.isValid("   ", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2026-06-11",
            "2026-12-31",
            "2000-02-29"
    })
    void isValid_ShouldReturnTrue_WhenDateFormatIsValidIso(String validDate) {
        boolean result = dateValidator.isValid(validDate, context);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11-06-2026",
            "2026/06/11",
            "2026-13-01",
            "2026-02-30",
            "invalid-string",
            "2026-6-11"
    })
    void isValid_ShouldReturnFalse_WhenDateFormatIsInvalid(String invalidDate) {
        boolean result = dateValidator.isValid(invalidDate, context);
        assertFalse(result);
    }
}