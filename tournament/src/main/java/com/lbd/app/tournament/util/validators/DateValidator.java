package com.lbd.app.tournament.util.validators;

import com.lbd.app.tournament.util.annotations.ValidLocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Objects;

public class DateValidator implements ConstraintValidator<ValidLocalDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.isBlank()) {
            return true;
        }
        try {
            LocalDate.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}