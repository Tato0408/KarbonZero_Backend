package project.karbonzero.Entities.Resource;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TBRESOURCES")
@Getter @Setter @ToString @EqualsAndHashCode
public class ResourceEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDRESOURCE")
    private String idResource;

    @Column(name = "IDMEASUREUNIT", nullable = false)
    private String idMeasureUnit;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "CARBONFOOTPRINT", nullable = false)
    private Double carbonFootprint;
}
