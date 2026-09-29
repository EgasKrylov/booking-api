package com.github.egorkrylov.bookingapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SeriesValidator implements ConstraintValidator<ValidSeries, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null || value.isBlank()) {
            return true;
        }

        return value.length() == 4;
    }
}
