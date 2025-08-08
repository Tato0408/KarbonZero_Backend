package project.karbonzero.Repositories.Measure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;

import java.util.List;

@Repository
public interface MeasureUnitRepository extends JpaRepository<MeasureUnitEntity, String> {
    List<MeasureUnitEntity> findByNameContains(String name);
}
