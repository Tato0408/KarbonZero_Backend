package project.karbonzero.Models.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String idInitialUnit;

    @NotBlank
    private String idFinalUnit;
    private String idResource;

    @NotBlank
    @Size(max = 100)
    private String operation;

    @NotNull
    private Double constant;
}
