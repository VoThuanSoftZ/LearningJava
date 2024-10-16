package com.softz.identity.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
    private int min;
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(value == null) return false;
        var age = ChronoUnit.YEARS.between(value, LocalDate.now());
        return age >= min;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        min = constraintAnnotation.min();
    }
}
