package ar.edu.utnfc.backend.ms_recursos.exceptions;


import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst().orElse("Validación inválida");
        log.warn("Validation error: {}", msg);
        return ResponseEntity.badRequest().body(
                new ErrorResponse(Instant.now(), 400, "Bad Request", msg, null));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleRSE(ResponseStatusException ex) {
        log.warn("RSE: {}", ex.getReason());
        HttpStatusCode statusCode = ex.getStatusCode();
        HttpStatus resolved = HttpStatus.resolve(statusCode.value());
        String reason = resolved != null ? resolved.getReasonPhrase() : statusCode.toString();
        return ResponseEntity.status(statusCode).body(
                new ErrorResponse(Instant.now(), statusCode.value(), reason, ex.getReason(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(Instant.now(), 500, "Internal Server Error",
                        "Ocurrió un error inesperado", null));
    }
}
