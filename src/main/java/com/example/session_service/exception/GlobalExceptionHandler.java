package com.example.session_service.exception;

import com.example.session_service.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handlerException(Exception e) {
        AuthResponse response = AuthResponse.builder()
                .status(Integer.valueOf(0))
                .message(e.getMessage())
                .tokenSet(Collections.emptySet())
                .build();
        return ResponseEntity.unprocessableContent().body(response);

    }
}
