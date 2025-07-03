package project.karbonzero.Repositories.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.User.RolEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, String> {
}
