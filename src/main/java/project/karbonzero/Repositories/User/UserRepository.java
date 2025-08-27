package project.karbonzero.Repositories.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.User.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername (String username);
    UserEntity findByEmail (String email);
    Page<UserEntity> findByUsernameContains(String username, Pageable pageable);
}
