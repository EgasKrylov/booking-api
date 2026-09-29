package com.github.egorkrylov.bookingapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = NumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumber {

    String message() default "Некорректный номер паспорта. Номер должен состоять из 6 цифр";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

