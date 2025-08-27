package project.karbonzero.Repositories.Measure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;

import java.util.List;

@Repository
public interface ConversionUnitRepository extends JpaRepository<ConversionUnitEntity, String> {
    List<ConversionUnitEntity> findByIdInitialUnitAndIdResource (MeasureUnitEntity idInitialUnit, ResourceEntity idResource);

    Page<ConversionUnitEntity> findByIdInitialUnitInOrIdFinalUnitIn (List<MeasureUnitEntity> initialUnits, List<MeasureUnitEntity> finalUnits, Pageable pageable);

}
