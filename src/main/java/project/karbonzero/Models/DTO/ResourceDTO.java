package project.karbonzero.Models.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ResourceDTO {

    private String idResource;

    @NotBlank
    private String idMeasureUnit;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Min(0)
    @NotNull
    private Double carbonFootprint;
}
