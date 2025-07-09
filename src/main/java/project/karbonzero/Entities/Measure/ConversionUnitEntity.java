package project.karbonzero.Entities.Measure;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TBCONVERSIONUNITS")
@Getter @Setter @EqualsAndHashCode @ToString
public class ConversionUnitEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCONVERSIONUNIT")
    private String idConversionUnit;

    @ManyToOne
    @JoinColumn(name = "IDINITIALUNIT", nullable = false)
    private MeasureUnitEntity idInitialUnit;

    @ManyToOne
    @JoinColumn(name = "IDFINALUNIT", nullable = false)
    private MeasureUnitEntity idFinalUnit;

    @Column(name = "IDRESOURCE", nullable = true)
    private String idResource;

    @Column(name = "OPERATION", nullable = false)
    private String operation;

    @Column(name = "CONSTANT", nullable = false)
    private Double constant;
}
