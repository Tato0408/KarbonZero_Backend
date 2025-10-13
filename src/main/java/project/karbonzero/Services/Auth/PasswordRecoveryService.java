package project.karbonzero.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Services.Mail.MailService;
import project.karbonzero.Utils.CodeGenerator;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordRecoveryService {

    @Autowired
    private AuthService authService;

    @Autowired
    private MailService mailService;

    // Código temporal en memoria
    private final ConcurrentHashMap<String, CodeEntry> codes = new ConcurrentHashMap<>();

    private static class CodeEntry {
        String code;
        Instant expiresAt;

        CodeEntry(String code, Instant expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }
    }

    // Generar y enviar código
    public boolean generateAndSendCode(String email) {
        UserEntity user = authService.getUserRecoveryPassword(email);
        if (user == null) return false;

        String code = CodeGenerator.generateCode(6); // 6 caracteres
        Instant expiresAt = Instant.now().plusSeconds(15 * 60); // 15 min
        codes.put(user.getIdUser(), new CodeEntry(code, expiresAt));

        mailService.sendRecoveryCode(email, code);
        return true;
    }

    // Verificar código
    public String verifyCode(String code) {
        for (Map.Entry<String, CodeEntry> entry : codes.entrySet()) {
            CodeEntry stored = entry.getValue();

            if (stored.code.equals(code)) {
                if (Instant.now().isAfter(stored.expiresAt)) {
                    codes.remove(entry.getKey());
                    return null; // código expirado
                }

                // Código válido, eliminarlo (de un solo uso)
                String idUser = entry.getKey();
                codes.remove(entry.getKey());
                return idUser;
            }
        }
        return null; // no se encontró ese código
    }
}
