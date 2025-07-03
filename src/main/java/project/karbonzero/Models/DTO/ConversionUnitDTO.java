package project.karbonzero.Models.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@EqualsAndHashCode
public class ConversionUnitDTO {

    private String idConversionUnit;
    private String idInitialUnit;
    private String idFinalUnit;
    private String idResource;

    @NotBlank
    @Size(max = 100)
    private String operation;

    @NotBlank
    private Double constant;
}
