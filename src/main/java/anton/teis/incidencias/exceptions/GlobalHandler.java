package anton.teis.incidencias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNotFound(NotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Recurso no encontrado");
        response.put("mensaje", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());

        // Devuelve un JSON con status 404
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Ocurrió un error inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
