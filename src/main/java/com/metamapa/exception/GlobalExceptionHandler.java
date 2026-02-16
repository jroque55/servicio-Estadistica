package com.metamapa.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // 500 genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarErrorGeneral(Exception e) {

        log.error("Error interno del servidor", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
    }

    // VALIDACIÓN → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        log.warn("Error de validación: {}", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body("Datos inválidos");
    }

    // JSON mal formado → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonError(Exception ex) {

        return ResponseEntity
                .badRequest()
                .body("JSON inválido");
    }

    // 🟡 404 negocio
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> manejarNoEncontrado(
            RecursoNoEncontradoException e) {

        log.warn(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

