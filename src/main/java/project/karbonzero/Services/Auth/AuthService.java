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
    public boolean LogIn(String email, String password){
        Argon2PasswordEncoder argon = new Argon2PasswordEncoder();
        Optional<UserEntity> userOptional = repo.findOptionalByEmail(email);
        if(userOptional.isPresent()){
            UserEntity user = userOptional.get();
            int role = user.getIdRol().getStatus();
            System.out.println("USER ENCONTRADO "+ user.getEmail() + " "+user.getUsername() + " "+role);
            return  argon.decodePassword(user.getUserPassword() ,password);
        }
        return false;
    }

    public Optional<UserEntity> getUser(String email){
        Optional<UserEntity> userOpt = repo.findOptionalByEmail(email);
        return (userOpt.isPresent()) ? userOpt : Optional.empty();
    }

}
