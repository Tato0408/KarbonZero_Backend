package project.karbonzero.Entities.Consumption;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "TBCONSUMPTIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ConsumptionEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCONSUMPTION")
    private String idConsumption;

    @Column(name = "IDRESOURCEPURITY", nullable = false)
    private String idResourcePurity;

    @Column(name = "IDUSER", nullable = false)
    private String idUser;

    @Column(name = "QUANTITY", nullable = false)
    private Double quantity;

    @Column(name = "CONSUMPTIONDATE", nullable = false)
    private String consumptionDate;

    @Column(name = "COST", nullable = false)
    private Double cost;
}
