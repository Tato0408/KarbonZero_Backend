package project.karbonzero.Entities.Measure;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "TBMEASURES")
@Getter @Setter @ToString @EqualsAndHashCode
public class MeasureEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDMEASURE")
    private String idMeasure;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "idMeasure")
    private Set<MeasureUnitEntity> measureUnitsSet;
}
