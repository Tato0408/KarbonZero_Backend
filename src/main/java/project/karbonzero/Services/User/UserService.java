package project.karbonzero.Services.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Config.Argon2PasswordEncoder;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Repositories.User.UserRepository;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public List<UserDTO> getAllUsers(){
        List<UserEntity> usuarios = repo.findAll();
        return usuarios.stream()
                .map(this::UserToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO insertUser(UserDTO dto) {
        if (dto == null || dto.getUserPassword() == null || dto.getUserPassword().isEmpty()) {
            throw new IllegalArgumentException("User or password can`t be null or empty");
        }

        try {
            Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();
            String hashedPassword = argon2PasswordEncoder.HashPassword(dto.getUserPassword());
            dto.setUserPassword(hashedPassword);

            UserEntity entity = DTOToUser(dto);
            UserEntity savedEntity = repo.save(entity);

            return UserToDTO(savedEntity);
        }
        catch (Exception e) {
            throw new RuntimeException("Can't create user " + e.getMessage());
        }

    }

    public boolean deleteUser(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The User with the ID : " + id + " doesn't exists", 1);
        }
    }

    private UserDTO UserToDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setIdUser(entity.getIdUser());
        dto.setIdRol(entity.getIdRol());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setUserPassword(entity.getUserPassword());
        return dto;
    }

    // Metodo para convertir DTO a Entidad
    private UserEntity DTOToUser(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setIdUser(dto.getIdUser());
        entity.setIdRol(dto.getIdRol());
        entity.setUsername(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setUserPassword(dto.getUserPassword());
        return entity;
    }
}
