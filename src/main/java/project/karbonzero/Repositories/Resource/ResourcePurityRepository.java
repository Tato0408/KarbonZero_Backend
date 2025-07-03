package project.karbonzero.Repositories.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;

@Repository
public interface ResourcePurityRepository extends JpaRepository<ResourcePurityEntity, String> {
}
