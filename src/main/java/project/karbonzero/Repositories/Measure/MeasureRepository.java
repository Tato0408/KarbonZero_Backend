package project.karbonzero.Repositories.Measure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.karbonzero.Entities.Measure.MeasureEntity;

import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<MeasureEntity, String> {

    List<MeasureEntity> findByNameContains(String name);
}
