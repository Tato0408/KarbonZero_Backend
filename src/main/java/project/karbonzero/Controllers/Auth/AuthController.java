package project.karbonzero.Controllers.Auth;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Models.DTO.ForgotPasswordDTO;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Services.Auth.AuthService;
import project.karbonzero.Services.Auth.PasswordRecoveryService;
import project.karbonzero.Services.User.UserService;
import project.karbonzero.Utils.JWTUtils;
import project.karbonzero.Utils.Responses;

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

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    // Las 2 funciones login devuelven tokens de diferente duración
    // No mandamos la duración del token como parámetro porque podría darnos más problemas de seguridad

    @PostMapping("/shortLogin")
    private ResponseEntity<Map<String, Object>> shortLogin (@RequestBody UserDTO data, HttpServletResponse response) {
        if (data.getEmail() == null || data.getEmail().isBlank()) {
            return Responses.responseNotAuthenticated("Error, credenciales inválidas");
        }
        if (data.getUserPassword() == null || data.getUserPassword().isBlank()) {
            return Responses.responseNotAuthenticated("Error, credenciales inválidas");
        }

        if (service.Login(data.getEmail(), data.getUserPassword())) {
            addTokenCookie(response, data.getEmail(), 3600);
            return Responses.responseOK("Inicio de sesión exitoso");
        }
        return Responses.responseNotAuthenticated("Error, credenciales inválidas");
    }

    @PostMapping("/login")
    private ResponseEntity<Map<String, Object>> login (@RequestBody UserDTO data, HttpServletResponse response) {
        if (data.getEmail() == null || data.getEmail().isBlank()) {
            return Responses.responseNotAuthenticated("Error, credenciales inválidas");
        }
        if (data.getUserPassword() == null || data.getUserPassword().isBlank()) {
            return Responses.responseNotAuthenticated("Error, credenciales inválidas");
        }

        if (service.Login(data.getEmail(), data.getUserPassword())) {
            addTokenCookie(response, data.getEmail(), 86400);
            return Responses.responseOK("Inicio de sesión exitoso");
        }
        return Responses.responseNotAuthenticated("Error, credenciales inválidas");
    }

    @PostMapping("/loginCompany")
    private ResponseEntity<?> loginCompany (HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();

        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", dotenv.get("EXTERNAL_API_KEY")); // Header personalizado
        headers.set("X-Company-Domain", dotenv.get("EXTERNAL_DOMAIN"));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:8081" + "/apiAuth/authenticateCompany",
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    private void addTokenCookie (HttpServletResponse response, String email, Integer maxAge) {
        Optional<UserEntity> userOpt = service.getUser(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            String token = jwtUtils.create(
                    String.valueOf(user.getIdUser()),
                    user.getEmail(),
                    user.getIdRol().getStatus()
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
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
                            "nombre", user.getFirstName(),
                            "apellido", user.getLastName(),
                            "correo", user.getEmail(),
                            "status", user.getIdRol().getStatus(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            //log.error("Error en /me endpoint: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }

    @GetMapping("/company")
    private ResponseEntity<?> company (HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("channelToken".equals(cookie.getName())) {
                headers.set("Cookie", "channelToken=" + cookie.getValue());
                break;
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Dotenv dotenv = Dotenv.load();
        return restTemplate.exchange(
                dotenv.get("EXTERNAL_ADMIN_DOMAIN") + "/apiAuth/company",
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        // Crear cookie de expiración con SameSite=None
        String cookieValue = "authToken=; Path=/; HttpOnly; Secure; SameSite=None; MaxAge=0; Domain=localhost";

        response.addHeader("Set-Cookie", cookieValue);
        //response.addHeader("Access-Control-Allow-Credentials", "true"); <-- ESTO NO DEBEN AGREGARLO
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");

        // También agregar headers CORS para la respuesta
        String origin = request.getHeader("Origin");
        if (origin != null &&
                (origin.contains("localhost") || origin.contains("herokuapp.com"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        return Responses.responseOK("logout exitoso");
    }

    //ENDPOINTS DE RECUPERACION DE CONTRASEÑA
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        boolean sent = passwordRecoveryService.generateAndSendCode(dto.getEmail());
        if (!sent) {
            return ResponseEntity.badRequest().body(Map.of("message", "Correo no registrado"));
        }
        return ResponseEntity.ok(Map.of("message", "Código enviado"));
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");

        String idUser = passwordRecoveryService.verifyCode(code);

        if (idUser == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Código inválido o expirado"));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Código válido",
                "idUser", idUser
        ));
    }
}
