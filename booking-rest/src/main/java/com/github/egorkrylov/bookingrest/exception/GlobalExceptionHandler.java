package com.github.egorkrylov.bookingrest.exception;

import com.github.egorkrylov.bookingapicontract.dto.ErrorResponse;
import com.github.egorkrylov.bookingapicontract.exception.ConflictException;
import com.github.egorkrylov.bookingapicontract.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final String BASE_PROBLEM_URI = "https://api.example.com/problems/";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        BASE_PROBLEM_URI + "resource-not-found",
                        "Ресурс не найден",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handelValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                )).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        400,
                        BASE_PROBLEM_URI + "validation-error",
                        "Ошибка валидации",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        fieldErrors
                ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        409,
                        BASE_PROBLEM_URI + "conflict",
                        "Конфликт",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        500,
                        BASE_PROBLEM_URI + "internal-error",
                        "Внутреняя ошибка сервера",
                        "Произошла непредвиденная ошибка. Обратитесь к поддержке.",
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

}
