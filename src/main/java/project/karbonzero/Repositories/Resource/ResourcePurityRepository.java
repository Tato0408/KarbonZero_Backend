package project.karbonzero.Repositories.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;

import java.util.List;

@Repository
public interface ResourcePurityRepository extends JpaRepository<ResourcePurityEntity, String> {
    List<ResourcePurityEntity> findByIdResource (ResourceEntity resource);
}
