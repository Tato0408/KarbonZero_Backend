package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Repositories.Measure.ConversionUnitRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConversionUnitService {

    @Autowired
    private ConversionUnitRepository repo;
    @Autowired
    private MeasureUnitRepository measureUnitRepo;
    @Autowired

    public List<ConversionUnitDTO> getAllConversionUnits () {
        List<ConversionUnitEntity> conversionUnits = repo.findAll();
        return conversionUnits.stream()
                .map(this::ConversionUnitToDTO)
                .collect(Collectors.toList());
    }

    public ConversionUnitDTO getConversionUnitById (String id) {
        if (repo.existsById(id)) {
            ConversionUnitEntity entity = repo.getReferenceById(id);
            return ConversionUnitToDTO(entity);
        }
        return null;
    }

    public ConversionUnitDTO insertConversionUnit (ConversionUnitDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Conversion Unit can't be null or empty");
        }

        try {
            ConversionUnitEntity entity = DTOToConversionUnit(dto);
            ConversionUnitEntity savedEntity = repo.save(entity);

            return ConversionUnitToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create Conversion Unit " + e.getMessage());
        }
    }

    public ConversionUnitDTO updateConversionUnit(String id, ConversionUnitDTO dto){
        try {
            if (repo.existsById(id)){
                ConversionUnitEntity entity = repo.getReferenceById(id);

                entity.setIdInitialUnit(measureUnitRepo.getReferenceById(dto.getIdInitialUnit()));
                entity.setIdFinalUnit(measureUnitRepo.getReferenceById(dto.getIdFinalUnit()));
                entity.setIdResource(dto.getIdResource());
                entity.setOperation(dto.getOperation());
                entity.setConstant(dto.getConstant());

                ConversionUnitEntity savedEntity = repo.save(entity);
                return ConversionUnitToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Conversion Unit with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Conversion Unit " + e.getMessage());
        }
    }

    public boolean deleteConversionUnit(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Conversion Unit with the ID : " + id + " doesn't exists", 1);
        }
    }

    public ConversionUnitDTO ConversionUnitToDTO(ConversionUnitEntity entity) {
        ConversionUnitDTO dto = new ConversionUnitDTO();
        dto.setIdConversionUnit(entity.getIdConversionUnit());
        dto.setIdResource(entity.getIdResource());
        dto.setOperation(entity.getOperation());
        dto.setConstant(entity.getConstant());
        if(entity.getIdInitialUnit() != null && entity.getIdFinalUnit() != null){
            dto.setNameInitialUnit(entity.getIdInitialUnit().getName());
            dto.setNameFinalUnit(entity.getIdFinalUnit().getName());
            dto.setIdInitialUnit(entity.getIdInitialUnit().getIdMeasureUnit());
            dto.setIdFinalUnit(entity.getIdFinalUnit().getIdMeasureUnit());
        }else{
            dto.setNameFinalUnit("No hay unidad de medida asociada");
            dto.setNameInitialUnit("No hay unidad de medida asociada");
            dto.setIdFinalUnit(null);
            dto.setIdInitialUnit(null);
        }
        return dto;
    }

    public ConversionUnitEntity DTOToConversionUnit(ConversionUnitDTO dto) {
        ConversionUnitEntity entity = new ConversionUnitEntity();
        entity.setIdConversionUnit(dto.getIdConversionUnit());
        entity.setIdResource(dto.getIdResource());
        entity.setOperation(dto.getOperation());
        entity.setConstant(dto.getConstant());
        if(dto.getIdInitialUnit()!= null && dto.getIdFinalUnit() != null){
            MeasureUnitEntity finalMeasureUnit = measureUnitRepo.findById(dto.getIdFinalUnit()).orElseThrow(()-> new IllegalArgumentException("Unidad de medida no entonctrada con ese ID " + dto.getIdFinalUnit()));
            MeasureUnitEntity initialMeasureUnit = measureUnitRepo.findById(dto.getIdInitialUnit()).orElseThrow(()-> new IllegalArgumentException("Unidad de medida no entonctrada con ese ID " + dto.getIdInitialUnit()));

            entity.setIdInitialUnit(initialMeasureUnit);
            entity.setIdFinalUnit(finalMeasureUnit);
        }
        return entity;
    }

}
