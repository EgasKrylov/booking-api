package com.github.egorkrylov.bookingapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SeriesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSeries {

    String message() default "Некорректная серия паспорта. Серия должна состоять из 4 цифр";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
