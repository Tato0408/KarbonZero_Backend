package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


@ToString
@EqualsAndHashCode
@Getter @Setter
public class ConsumptionDTO {

    private String idConsumption;

    @NotBlank
    private String idResourcePurity;
    private String resourcePurityName;
    @NotBlank
    private String idUser;
    private String userName;

    @Min(0)
    @NotNull
    private Double quantity;

    @NotBlank()
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String consumptionDate;

    @Min(0)
    @NotNull
    private Double cost;

}
