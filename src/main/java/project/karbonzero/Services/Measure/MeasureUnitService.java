package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.User.RolEntity;
import project.karbonzero.Models.DTO.MeasureUnitDTO;
import project.karbonzero.Repositories.Measure.MeasureRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasureUnitService {

    @Autowired
    private MeasureUnitRepository repo;
    @Autowired
    private MeasureRepository measureRepo;

    public List<MeasureUnitDTO> getAllMeasureUnits (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasureUnitEntity> measureUnits = repo.findAll(pageable);
        return measureUnits.stream()
                .map(this::MeasureUnitToDTO)
                .collect(Collectors.toList());
    }

    public MeasureUnitDTO getMeasureUnitById (String id) {
        if (repo.existsById(id)) {
            MeasureUnitEntity entity = repo.getReferenceById(id);
            return MeasureUnitToDTO(entity);
        }
        return null;
    }

    public List<MeasureUnitDTO> getMeasureUnitsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasureUnitEntity> measureUnits = repo.findByNameContains(name, pageable);
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

    public MeasureUnitDTO updateMeasureUnit(String id, MeasureUnitDTO dto){
        try {
            if (repo.existsById(id)){
                MeasureUnitEntity entity = repo.getReferenceById(id);

                entity.setIdMeasure(measureRepo.getReferenceById(dto.getIdMeasure()));
                entity.setName(dto.getName());

                MeasureUnitEntity savedEntity = repo.save(entity);
                return MeasureUnitToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Measure Unit with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Measure Unit " + e.getMessage());
        }
    }

    public boolean deleteMeasureUnit(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Measure Unit with the ID : " + id + " doesn't exists", 1);
        }
    }

    public MeasureUnitDTO MeasureUnitToDTO(MeasureUnitEntity entity) {
        MeasureUnitDTO dto = new MeasureUnitDTO();
        dto.setIdMeasureUnit(entity.getIdMeasureUnit());
        dto.setName(entity.getName());
        if(entity.getIdMeasure() != null){
            dto.setMeasureName(entity.getIdMeasure().getName());
            dto.setIdMeasure(entity.getIdMeasure().getIdMeasure());
        }else{
            dto.setMeasureName("No hay una medida asociada");
            dto.setIdMeasure(null);
        }
        return dto;
    }

    public MeasureUnitEntity DTOToMeasureUnit(MeasureUnitDTO dto) {
        MeasureUnitEntity entity = new MeasureUnitEntity();
        entity.setIdMeasureUnit(dto.getIdMeasureUnit());
        entity.setName(dto.getName());
        if (dto.getIdMeasure()!=null){
            MeasureEntity measure = measureRepo.findById(dto.getIdMeasure()).orElseThrow(()->new IllegalArgumentException("Medida no encontrado con ese ID" + dto.getIdMeasure()));
            entity.setIdMeasure(measure);
        }
        return entity;
    }

}
