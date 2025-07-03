package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Models.DTO.MeasureDTO;
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

    public MeasureUnitDTO insertMeasureUnit (MeasureUnitDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Measure Unit can't be null or empty");
        }

        try {
            MeasureUnitEntity entity = DTOToMeasureUnit(dto);
            MeasureUnitEntity savedEntity = repo.save(entity);

            return MeasureUnitToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create measure Unit " + e.getMessage());
        }
    }

    private MeasureUnitDTO MeasureUnitToDTO(MeasureUnitEntity entity) {
        MeasureUnitDTO dto = new MeasureUnitDTO();
        dto.setIdMeasureUnit(entity.getIdMeasureUnit());
        dto.setIdMeasure(entity.getIdMeasure());
        dto.setName(entity.getName());
        return dto;
    }

    private MeasureUnitEntity DTOToMeasureUnit(MeasureUnitDTO dto) {
        MeasureUnitEntity entity = new MeasureUnitEntity();
        entity.setIdMeasureUnit(dto.getIdMeasureUnit());
        entity.setIdMeasure(dto.getIdMeasure());
        entity.setName(dto.getName());
        return entity;
    }

}
