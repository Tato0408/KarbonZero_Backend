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

    @NotBlank()
    @Size(max = 200)
    private String username;
    @Size(max = 200)
    private String firstName;
    @Size(max = 200)
    private String lastName;

    @NotBlank()
    @Email()
    @Size(max = 200)
    private String email;

    @Size(max = 200)
    private String userPassword;
}
