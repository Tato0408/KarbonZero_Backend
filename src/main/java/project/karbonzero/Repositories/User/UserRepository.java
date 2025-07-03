package project.karbonzero.Repositories.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.User.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
}
