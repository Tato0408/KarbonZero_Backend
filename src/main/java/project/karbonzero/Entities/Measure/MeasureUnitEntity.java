package project.karbonzero.Entities.Measure;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import project.karbonzero.Entities.Resource.ResourceEntity;

import java.util.Set;

@Entity
@Table(name = "TBMEASUREUNITS")
@Getter @Setter @EqualsAndHashCode @ToString
public class MeasureUnitEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDMEASUREUNIT")
    private String idMeasureUnit;

    @ManyToOne
    @JoinColumn(name = "IDMEASURE", nullable = false)
    private MeasureEntity idMeasure;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "idMeasureUnit")
    private Set<ResourceEntity> resourcesSet;
}
