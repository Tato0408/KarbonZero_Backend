package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class MeasureUnitDTO {

    private String idMeasureUnit;

    @NotBlank
    private String idMeasure;
    private String measureName;

    @NotBlank
    @Size(max = 200)
    private String name;
}
