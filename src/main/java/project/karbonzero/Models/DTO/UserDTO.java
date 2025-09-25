package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.*;
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

    @NotNull
    @Min(0)
    private Integer status;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer disabled;

    @NotBlank
    private String username;

    private String firstName;
    private String lastName;

    @NotBlank
    private String email;
    @NotBlank
    private String userPassword;

    private String imageProfile;

}
