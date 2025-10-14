package project.karbonzero.Repositories.Consumption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<ConsumptionEntity, String> {
    Page<ConsumptionEntity> findAllByOrderByConsumptionDateDesc(Pageable page);
    Page<ConsumptionEntity> findByIdResourcePurityIn (List<ResourcePurityEntity> resourcePuritiesId, Pageable pageable);
    Page<ConsumptionEntity> findByIdResourcePurityInAndConsumptionDateBetweenOrderByConsumptionDateDesc (List<ResourcePurityEntity> resourcePuritiesId, LocalDate  start, LocalDate  end, Pageable pageable);
    List<ConsumptionEntity> findByIdResourcePurityInAndConsumptionDateBetweenOrderByConsumptionDateDesc (List<ResourcePurityEntity> resourcePuritiesId, LocalDate  start, LocalDate  end);


}
