package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class UserDTO {

    private String idUser;

    private String idRol;
    private String nameRol;
    private Integer status;


    private String username;
    private String firstName;
    private String lastName;


    private String email;

    private String userPassword;

    private String imageProfile;
}
