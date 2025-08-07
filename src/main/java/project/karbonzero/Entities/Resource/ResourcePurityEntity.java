package project.karbonzero.Entities.Resource;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;

import java.util.List;

@Entity
@Table(name = "TBRESOURCEPURITIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class ResourcePurityEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDRESOURCEPURITY")
    private String idResourcePurity;

    @ManyToOne
    @JoinColumn(name = "IDRESOURCE", referencedColumnName = "IDRESOURCE", nullable = false)
    private ResourceEntity idResource;

    @Column(name = "PURITY", nullable = false)
    private Double purity;

    @OneToMany(mappedBy = "idResourcePurity")
    private List<ConsumptionEntity> consumptionsList;
}
