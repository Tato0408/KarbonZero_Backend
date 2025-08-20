package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<MeasureDTO> getAllMeasures (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasureEntity> measures = repo.findAll(pageable);
        return measures.map(this::MeasureToDTO);
    }

    public MeasureDTO getMeasureById (String id) {
        if (repo.existsById(id)) {
            MeasureEntity entity = repo.getReferenceById(id);
            return MeasureToDTO(entity);
        }
        return null;
    }

    public Page<MeasureDTO> getMeasuresByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasureEntity> measures = repo.findByNameContains(name, pageable);
        return measures.map(this::MeasureToDTO);

    }

    public MeasureDTO insertMeasure (MeasureDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Measure can't be null or empty");
        }

        try {
            MeasureEntity entity = DTOToMeasure(dto);
            MeasureEntity savedEntity = repo.save(entity);

            return MeasureToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create Measure " + e.getMessage());
        }
    }

    public MeasureDTO updateMeasure(String id, MeasureDTO dto){
        try {
            if (repo.existsById(id)){
                MeasureEntity entity = repo.getReferenceById(id);

                entity.setName(dto.getName());

                MeasureEntity savedEntity = repo.save(entity);
                return MeasureToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Measure with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Measure " + e.getMessage());
        }
    }

    public boolean deleteMeasure(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Measure with the ID : " + id + " doesn't exists", 1);
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
