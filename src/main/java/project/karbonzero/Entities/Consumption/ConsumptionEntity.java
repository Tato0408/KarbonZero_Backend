package project.karbonzero.Entities.Consumption;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Entities.User.UserEntity;

import java.time.LocalDate;

@Entity
@Table(name = "TBCONSUMPTIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ConsumptionEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCONSUMPTION")
    private String idConsumption;

    @ManyToOne
    @JoinColumn(name = "IDRESOURCEPURITY", nullable = false)
    private ResourcePurityEntity idResourcePurity;

    @ManyToOne
    @JoinColumn(name = "IDUSER", nullable = false)
    private UserEntity idUser;

    @Column(name = "QUANTITY", nullable = false)
    private Double quantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CONSUMPTIONDATE", nullable = false)
    private LocalDate consumptionDate;

    @Column(name = "COST", nullable = false)
    private Double cost;
}
