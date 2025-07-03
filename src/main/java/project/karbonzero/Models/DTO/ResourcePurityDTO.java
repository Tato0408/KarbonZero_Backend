package project.karbonzero.Models.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ResourcePurityDTO {

    private String idResourcePurity;
    private String idResource;

    @NotBlank
    @Min(0)
    @Max(1)
    private Double purity;
}
