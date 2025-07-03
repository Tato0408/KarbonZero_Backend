package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Models.DTO.MeasureUnitDTO;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasureUnitService {

    @Autowired
    private MeasureUnitRepository repo;

    public List<MeasureUnitDTO> getAllMeasureUnits () {
        List<MeasureUnitEntity> measureUnits = repo.findAll();
        return measureUnits.stream()
                .map(this::MeasureUnitToDTO)
                .collect(Collectors.toList());
    }

    private MeasureUnitDTO MeasureUnitToDTO(MeasureUnitEntity entity) {
        MeasureUnitDTO dto = new MeasureUnitDTO();
        dto.setIdMeasureUnit(entity.getIdMeasureUnit());
        dto.setIdMeasure(entity.getIdMeasure());
        dto.setName(entity.getName());
        return dto;
    }

}
