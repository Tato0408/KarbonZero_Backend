package project.karbonzero.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Responses {
    public static ResponseEntity<Map<String, Object>> responseBadRequest (String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "error",
                "errorType", "VALIDATION_ERROR",
                "message", message
        ));
    }

    public static ResponseEntity<Map<String, Object>> responseCreated (Object answer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
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

}
