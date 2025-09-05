package com.coffeeteam.fitbyte.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

public class IsoDateValidator implements ConstraintValidator<IsoDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true; // Use @NotEmpty for null/empty
        try {
            LocalDateTime.parse(value); // Adjust if you want ZonedDateTime or OffsetDateTime
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}