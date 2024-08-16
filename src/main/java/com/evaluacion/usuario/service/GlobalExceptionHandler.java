package com.evaluacion.usuario.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarResponseStatusException(ResponseStatusException ex)
    {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", ex.getReason());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
}

