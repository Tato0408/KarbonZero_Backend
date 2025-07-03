package project.karbonzero.Entities.Resource;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TBRESOURCEPURITIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class ResourcePurityEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDRESOURCEPURITY")
    private String idResourcePurity;

    @Column(name = "IDRESOURCE", nullable = false)
    private String idResource;

    @Column(name = "PURITY", nullable = false)
    private Double purity;
}
