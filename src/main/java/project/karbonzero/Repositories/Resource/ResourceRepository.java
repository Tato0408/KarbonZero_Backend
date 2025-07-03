package project.karbonzero.Repositories.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Resource.ResourceEntity;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, String> {
}
