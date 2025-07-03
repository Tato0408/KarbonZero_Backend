package project.karbonzero.Entities.Measure;

import com.sun.jdi.Field;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "TBCONVERSIONUNITS")
@Getter @Setter @EqualsAndHashCode @ToString
public class ConversionUnitEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCONVERSIONUNIT")
    private String idConversionUnit;

    @Column(name = "IDINITIALUNIT", nullable = false)
    private String idInitialUnit;

    @Column(name = "IDFINALUNIT", nullable = false)
    private String idFinalUnit;

    @Column(name = "IDRESOURCE", nullable = true)
    private String idResource;

    @Column(name = "OPERATION", nullable = false)
    private String operation;

    @Column(name = "CONSTANT", nullable = false)
    private Double constant;
}
