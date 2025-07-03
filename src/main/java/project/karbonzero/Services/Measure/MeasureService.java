package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Models.DTO.MeasureDTO;
import project.karbonzero.Repositories.Measure.MeasureRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeasureService {

    @Autowired
    private MeasureRepository repo;

    public List<MeasureDTO> getAllMeasures () {
        List<MeasureEntity> measures = repo.findAll();
        return measures.stream()
                .map(this::MeasureToDTO)
                .collect(Collectors.toList());
    }

    public MeasureDTO insertMeasure (MeasureDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Measure cant't be null or empty");
        }

        try {
            MeasureEntity entity = DTOToMeasure(dto);
            MeasureEntity savedEntity = repo.save(entity);

            return MeasureToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create measure " + e.getMessage());
        }
    }

    public MeasureDTO MeasureToDTO (MeasureEntity entity) {
        MeasureDTO dto = new MeasureDTO();
        dto.setIdMeasure(entity.getIdMeasure());
        dto.setName(entity.getName());
        return dto;
    }

    public MeasureEntity DTOToMeasure (MeasureDTO dto) {
        MeasureEntity entity = new MeasureEntity();
        entity.setIdMeasure(dto.getIdMeasure());
        entity.setName(dto.getName());
        return entity;
    }

}
