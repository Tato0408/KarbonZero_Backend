package project.karbonzero.Controllers.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Models.DTO.LoggedResponse;
import project.karbonzero.Models.DTO.LoggedResponse;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Services.Auth.AuthService;
import project.karbonzero.Utils.JWTUtils;

import java.util.Optional;

@RestController
@RequestMapping("/apiAuth")
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<String> login(@Valid @RequestBody UserDTO data, HttpServletResponse response){
        if (data.getEmail() == null || data.getEmail().isBlank() ||
                data.getUserPassword() == null || data.getUserPassword().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        if(service.LogIn(data.getEmail(), data.getUserPassword())){
            addTokenCookie(response, data.getEmail());
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        //1. Eliminar la cookie del cliente
        Cookie cookie = new Cookie("authToken", null); //Nombre debe coincidir con la cookie del login
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().body("Sesión cerrada exitosamente");
    }

    @GetMapping("/me")
    public ResponseEntity<LoggedResponse> getMe() {
        return ResponseEntity.ok(service.getLoguedUser());
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = jwtUtils.extractTokenFromRequest(request);
        if (token == null || !jwtUtils.validate(token)) {
            return ResponseEntity.status(401).body("Token inválido o ausente");
        }
        return ResponseEntity.ok("Token válido");
    }
    private void addTokenCookie(HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UserEntity> userOpt = service.getUser(correo);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIdUser()),
                    user.getEmail(),
                    user.getIdRol().getStatus() // ← Usar el nombre real del tipo
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }

}
