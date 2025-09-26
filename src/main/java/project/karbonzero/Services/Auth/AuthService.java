package project.karbonzero.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Config.Argon2PasswordEncoder;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Repositories.User.UserRepository;

import java.util.Optional;

@Service
public class AuthService {
    /// Accedemos al userRpo
    @Autowired
    private UserRepository repo;

    ///Metodo de logIn
    public boolean Login (String email, String password) {
        Argon2PasswordEncoder objHash = new Argon2PasswordEncoder();
        UserEntity user = repo.findByEmailAndDisabled(email, 0);

        if (user != null) {
            return objHash.decodePassword(user.getUserPassword(), password);
        }
        return false;
    }

    public Optional<UserEntity> getUser(String email){
        // Buscar usuario completo en la base de datos
        Optional<UserEntity> userOpt = repo.findOptionalByEmail(email);
        return (userOpt != null) ? userOpt : null;
    }

}
