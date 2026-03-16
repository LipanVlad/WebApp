package com.example.webApp.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorMessage> buildErrorMessage(String message, HttpStatus status){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTime(LocalDateTime.now());
        errorMessage.setStatus(status.value());
        errorMessage.setError(status.getReasonPhrase());
        errorMessage.setMessage(message);
        return ResponseEntity.status(status).body(errorMessage);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorMessage> UserException(AppException e){
        return buildErrorMessage(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationError (MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");
        return buildErrorMessage(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalError(Exception e) {
        return buildErrorMessage("An unexpected error has occured, please refresh", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
