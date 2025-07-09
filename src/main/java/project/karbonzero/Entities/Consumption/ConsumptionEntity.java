package project.karbonzero.Entities.Consumption;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Entities.User.UserEntity;

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

    @Column(name = "CONSUMPTIONDATE", nullable = false)
    private String consumptionDate;

    @Column(name = "COST", nullable = false)
    private Double cost;
}
