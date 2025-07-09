package project.karbonzero.Entities.User;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "TBROLS")
@Getter @Setter @EqualsAndHashCode @ToString
public class RolEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDROL")
    private String idRol;

    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @OneToMany(mappedBy="idRol")
    private Set<UserEntity> usersSet;
}
