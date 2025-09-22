package project.karbonzero.Controllers.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Services.Auth.AuthService;
import project.karbonzero.Utils.JWTUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    /// LOCALLOCALOCALOCALLOCALLOCAL
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

    /**
     * Se genera el token y se guarda en la Cookie, PRDO
     * @param
     * @param
     */
    /// ESTO SE DESCOMENTA CUANDO ESTEMOS EN PROD
//    private void addTokenCookie(HttpServletResponse response, String correo) {
//        Optional<UserEntity> userOpt = service.getUser(correo);
//
//        if (userOpt.isPresent()) {
//            System.out.println("we are here ");
//            UserEntity user = userOpt.get();
//            ///Generamos el token con el idUser, rol y email
//            String token = jwtUtils.create(
//                    String.valueOf(user.getIdUser()),
//                    user.getEmail(),
//                    user.getIdRol().getStatus()
//            );
//
//            ///Valores de la cookie
//            String cookieValue = String.format(
//                    "authToken=%s; " +
//                            "Path=/; " +
//                            "HttpOnly; " +
//                            "Secure; " +
//                            "SameSite=None; " +
//                            "MaxAge=86400; " +
//                            "Domain=localhost:8080",
//                    token
//            );
//            response.addHeader("Set-Cookie", cookieValue);
//            response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
//        }
//    }

    /// Obtener al usuario logged
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "No autenticado"
                        ));
            }

            // Manejar diferentes tipos de Principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
            } else {
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }

            Optional<UserEntity> userOpt = service.getUser(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "Usuario no encontrado"
                        ));
            }

            UserEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", user.getIdUser(),
                            "name", user.getFirstName(),
                            "lastname", user.getLastName(),
                            "email", user.getEmail(),
                            "status", user.getIdRol().getStatus(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse res) {
        ResponseCookie delete = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)     // delete
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, delete.toString());
        return ResponseEntity.ok("Logout exitoso");
    }
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        /// Crear cookie de expiración con SameSite=None
//        /// TODO: DOMAIN CAMBIARLO A EL PATH DE HEROKU
//        String cookieValue = "authToken=; Path=/; HttpOnly; Secure; SameSite=None; MaxAge=0; Domain=localhost:8080";
//
//        response.addHeader("Set-Cookie", cookieValue);
//        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
//
//        //Agregar los headers cors
//        String origin = request.getHeader("Origin");
//        if (origin != null &&
//                (origin.contains("localhost") || origin.contains("herokuapp.com"))) {
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
//        return ResponseEntity.ok()
//                .body("Logout exitoso");
//    }

}
