package com.taurupro.marketplace.web.exception;

import com.taurupro.marketplace.domain.exception.IdempotencyConflictException;
import com.taurupro.marketplace.domain.exception.RequestStillProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdempotencyConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflict(IdempotencyConflictException ex) {
        // 422: misma key, distinto body → error del cliente
        return ResponseEntity.unprocessableEntity()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RequestStillProcessingException.class)
    public ResponseEntity<Map<String, String>> handleProcessing(RequestStillProcessingException ex) {
        // 409: pedir reintento con backoff
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("Retry-After", "2")
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>>  handleException(MethodArgumentNotValidException ex){
        List<Error> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error->errors.add(new Error(error.getField(),error.getDefaultMessage())));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error>  handleException(Exception ex){
        Error error = new Error("unknown-error",ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}