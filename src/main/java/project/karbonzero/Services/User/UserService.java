package project.karbonzero.Services.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Config.Argon2PasswordEncoder;
import project.karbonzero.Entities.User.RolEntity;
import project.karbonzero.Entities.User.UserEntity;
import project.karbonzero.Exceptions.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.Mail.MailDTO;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Repositories.User.RolRepository;
import project.karbonzero.Repositories.User.UserRepository;
import project.karbonzero.Services.Consumption.ConsumptionService;
import project.karbonzero.Services.Mail.MailService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private RolRepository rolRepo;
    @Autowired
    private MailService mailService;

    public Boolean confirmPassword(String idUser, String password) {
        Argon2PasswordEncoder argonObj = new Argon2PasswordEncoder();

        if (!repo.existsById(idUser)) {
            return false;
        }

        UserEntity user = repo.getReferenceById(idUser);
        String databaseHashedPassword = user.getUserPassword();
        return argonObj.decodePassword(databaseHashedPassword, password);
    }

    public Boolean loginUser(String email, String password) {
        Argon2PasswordEncoder argonObj = new Argon2PasswordEncoder();

        UserEntity user = repo.findByEmail(email);
        if (user == null) {
            return false;
        }

        String databaseHashedPassword = user.getUserPassword();
        return argonObj.decodePassword(databaseHashedPassword, password);
    }

    public Page<UserDTO> getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = repo.findAll(pageable);
        return users.map(this::UserToDTO);
    }

    public UserDTO getUserById (String id) {
        if (repo.existsById(id)){
            UserEntity entity = repo.getReferenceById(id);
            return UserToDTO(entity);
        }
        return null;
    }

    public UserDTO getUserByEmail(String email) {
        UserEntity entity = repo.findByEmail(email);
        return UserToDTO(entity);
    }

    public Page<UserDTO> getUsersByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = repo.findByUsernameContains(username, pageable);
        return users.map(this::UserToDTO);
    }

    public List<ConsumptionDTO> getUserConsumptions (String id) {
        UserEntity entity = repo.getReferenceById(id);

        return entity.getConsumptionsList().stream()
                .map(new ConsumptionService()::ConsumptionToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO insertUser(UserDTO dto) {
        if (dto == null || dto.getUserPassword() == null || dto.getUserPassword().isEmpty()) {
            throw new BadDataException("User or password can`t be null or empty");
        }

        try {
            Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();
            String hashedPassword = argon2PasswordEncoder.HashPassword(dto.getUserPassword());
            //Se crea una variable que almacene la contraseña sin encriptar para que el usuario nuevo vea cual es su contraseña
            String rawPassword = dto.getUserPassword();
            dto.setUserPassword(hashedPassword);

            UserEntity entity = DTOToUser(dto);
            UserEntity savedEntity = repo.save(entity);

            //Se crea un objteo de tipo MailDto que guardara los datos del email
            MailDTO mail = new MailDTO();
            //Se asigna al SET del atributo que almacena el emial de correo destinatario el correo nuevo ingresado guardado en la variable savedEntity
            mail.setToEmail(savedEntity.getEmail());
            //Se asigna a la variable SET del atributo de asunto de correo diciendo que el usuario fue creado exitosamente
            mail.setSubject("Usuario creado exitosamente");
            //Se asigna a la variable SET del atributo del cuerpo del texto diciendo que el usuario fue creado exitosamente

            mail.setText("Hola " + savedEntity.getFirstName() +" " + savedEntity.getLastName() + ", tu usuario ha sido creado exitosamente. "+" Su usuario es: "+ savedEntity.getUsername() +   " y su contraseña es: " + rawPassword);

            //Se envia el correo usando el metodo de MailService
            mailService.sendEmail(mail);

            return UserToDTO(savedEntity);
        }
        catch (Exception e) {
            throw new CreateException("Can't create user " + e.getMessage());
        }

    }

    public UserDTO updateUser(String id, UserDTO dto){
        try {
            if (repo.existsById(id)){
                UserEntity entity = repo.getReferenceById(id);

                entity.setUsername(dto.getUsername());
                entity.setFirstName(dto.getFirstName());
                entity.setLastName(dto.getLastName());
                entity.setEmail(dto.getEmail());
                entity.setIdRol(rolRepo.getReferenceById(dto.getIdRol()));

                if (dto.getUserPassword() != null) {
                    Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();
                    String hashedPassword = argon2PasswordEncoder.HashPassword(dto.getUserPassword());
                    entity.setUserPassword(hashedPassword);
                }

                UserEntity savedEntity = repo.save(entity);
                return UserToDTO(savedEntity);
            }
            throw new BadIdException("The User with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new UpdateException("Can`t update User " + e.getMessage());
        }
    }

    public boolean deleteUser(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DeleteException("The User with the ID : " + id + " doesn't exists");
        }
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
        }else{
            dto.setNameRol("No hay cargo asignado");
            dto.setIdRol(null);
        }
        return dto;
    }

    public UserEntity DTOToUser(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setIdUser(dto.getIdUser());
        entity.setUsername(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setUserPassword(dto.getUserPassword());
        if (dto.getIdRol() != null){
            RolEntity rol = rolRepo.getReferenceById(dto.getIdRol());
            entity.setIdRol(rol);
        }
        return entity;
    }
}
