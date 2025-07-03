package project.karbonzero.Entities.Measure;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TBMEASUREUNITS")
@Getter @Setter @EqualsAndHashCode @ToString
public class MeasureUnitEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDMEASUREUNIT")
    private String idMeasureUnit;

    @Column(name = "IDMEASURE", nullable = false)
    private String idMeasure;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
}
