package project.karbonzero.Repositories.Consumption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;

@Repository
public interface ConsumptionRepository extends JpaRepository<ConsumptionEntity, String> {
}
