package project.karbonzero.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    private String idResourcePurity;
    private String idUser;

    @Min(0)
    @NotBlank()
    private Double quantity;
    @NotBlank()
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String consumptionDate;

    @Min(0)
    @NotBlank()
    private Double cost;

}
