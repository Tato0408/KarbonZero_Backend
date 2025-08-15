package project.karbonzero.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Responses {
    public static ResponseEntity<Map<String, Object>> responseBadRequest (String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "error",
                "errorType", "VALIDATION_ERROR",
                "message", message
        ));
    }

    public static ResponseEntity<Map<String, String>> responseBadRequestBinding (BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    public static ResponseEntity<Map<String, Object>> responseCreated (Object answer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "success",
                "data", answer
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseUpdated (Object answer) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "status", "success",
                "data", answer
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseServerError (Exception e, String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", message,
                "detail", e.getMessage()
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseNotFound (String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "not found",
                    "mensaje", message,
                    "timestamp", Instant.now().toString()
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseOK (String message) {
        return ResponseEntity.ok().body(Map.of(
                "status", "process completed",
                "message", message
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseOK (Object value) {
        return ResponseEntity.ok().body(Map.of(
                "status", "process completed",
                "value", value
        ));
    }
}
