package project.karbonzero.Models.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class RolDTO {

    private String idRol;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Max(999)
    @Min(0)
    private Integer status;
}
