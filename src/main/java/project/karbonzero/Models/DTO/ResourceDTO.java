package project.karbonzero.Models.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    private String idMeasureUnit;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Min(0)
    private Double carbonFootprint;
}
