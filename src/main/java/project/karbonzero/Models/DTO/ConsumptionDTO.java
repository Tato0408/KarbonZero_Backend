package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.*;
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

    @Positive
    @NotNull
    private Double quantity;

    @NotBlank()
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String consumptionDate;

    @PositiveOrZero
    @NotNull
    private Double cost;

}
