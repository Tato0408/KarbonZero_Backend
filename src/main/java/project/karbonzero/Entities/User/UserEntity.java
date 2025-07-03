package project.karbonzero.Entities.User;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TBUSERS")
@Getter() @Setter() @ToString() @EqualsAndHashCode()
public class UserEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDUSER")
    private String idUser;

    @Column(name = "IDROL", nullable = false)
    private String idRol;

    @Column(name = "USERNAME", nullable = false, length = 200)
    private String username;

    @Column(name = "FIRSTNAME", nullable = true, length = 200)
    private String firstName;

    @Column(name = "LASTNAME", nullable = true, length = 200)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, length = 200)
    private String email;

    @Column(name = "USERPASSWORD", nullable = false, length = 200)
    private String userPassword;
}
