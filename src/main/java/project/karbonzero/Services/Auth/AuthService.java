package project.karbonzero.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.karbonzero.Config.Argon2PasswordEncoder;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Models.DTO.LoggedResponse;
import project.karbonzero.Models.DTO.LoggedUser;
import project.karbonzero.Models.DTO.LoggedResponse;
import project.karbonzero.Models.DTO.LoggedUser;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Repositories.User.UserRepository;

import java.util.List;
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

    /// Metodo para obtener el logged
    public LoggedResponse getLoguedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        /// Obtenemos el email, auth toma como nombre el getName pero en nuestro caso toma el email
        String email = auth.getName();

        /// Obtenemos las authorities
        List<String> authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        /// Obtenemos nuestro usuario con x email
        UserEntity user = repo.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        // Map status -> human role name, if you store status as int
        Integer roleName = user.getIdRol().getStatus();

        LoggedUser userDto = new LoggedUser(
                user.getIdUser(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),     // or "nombre"
                user.getLastName(),      // or "apellido"
                roleName,
                authorities
        );

        return new LoggedResponse(userDto, true);
    }

    public UserDTO UserToDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setIdUser(entity.getIdUser());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setUserPassword(entity.getUserPassword());
        if(entity.getIdRol() != null){
            dto.setNameRol(entity.getIdRol().getName());
            dto.setIdRol(entity.getIdRol().getIdRol());
            dto.setStatus(entity.getIdRol().getStatus());

        }else{
            dto.setNameRol("No hay cargo asignado");
            dto.setIdRol(null);
        }
        return dto;
    }
}
