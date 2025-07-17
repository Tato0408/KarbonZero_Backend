package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ResourcePurityDTO {

    private String idResourcePurity;

    @NotBlank
    private String idResource;

    @DecimalMin("0.01")
    @Max(1)
    @NotNull
    private Double purity;
}
