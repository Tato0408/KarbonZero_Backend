package project.karbonzero.Repositories.Measure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;

@Repository
public interface ConversionUnitRepository extends JpaRepository<ConversionUnitEntity, String> {
}
