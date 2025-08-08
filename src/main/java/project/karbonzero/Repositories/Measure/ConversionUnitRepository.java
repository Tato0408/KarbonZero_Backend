package project.karbonzero.Repositories.Measure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;

import java.util.List;

@Repository
public interface ConversionUnitRepository extends JpaRepository<ConversionUnitEntity, String> {
    List<ConversionUnitEntity> findByIdInitialUnitOrIdFinalUnit (MeasureUnitEntity idInitialUnit, MeasureUnitEntity idFinalUnit);
}
