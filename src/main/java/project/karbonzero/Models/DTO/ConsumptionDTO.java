package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ConsumptionDTO {

    private String idConsumption;

    @NotBlank
    private String idResourcePurity;
    @NotBlank
    private String idUser;

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
