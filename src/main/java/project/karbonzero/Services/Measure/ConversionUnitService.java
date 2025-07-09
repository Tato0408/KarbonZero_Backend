package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Models.DTO.MeasureUnitDTO;
import project.karbonzero.Repositories.Measure.ConversionUnitRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConversionUnitService {

    @Autowired
    private ConversionUnitRepository repo;
    @Autowired
    private MeasureUnitRepository measureUnitRepo;

    public List<ConversionUnitDTO> getAllConversionUnits() {
        List<ConversionUnitEntity> conversionUnits = repo.findAll();
        return conversionUnits.stream()
                .map(this::ConversionUnitToDTO)
                .collect(Collectors.toList());
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
                ConversionUnitEntity entity = repo.getById(id);

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

    private ConversionUnitDTO ConversionUnitToDTO(ConversionUnitEntity entity) {
        ConversionUnitDTO dto = new ConversionUnitDTO();
        dto.setIdConversionUnit(entity.getIdConversionUnit());
        dto.setIdInitialUnit(entity.getIdInitialUnit().getIdMeasureUnit());
        dto.setIdFinalUnit(entity.getIdFinalUnit().getIdMeasureUnit());
        dto.setIdResource(entity.getIdResource());
        dto.setOperation(entity.getOperation());
        dto.setConstant(entity.getConstant());
        return dto;
    }

    private ConversionUnitEntity DTOToConversionUnit(ConversionUnitDTO dto) {
        ConversionUnitEntity entity = new ConversionUnitEntity();
        entity.setIdConversionUnit(dto.getIdConversionUnit());
        entity.setIdInitialUnit(measureUnitRepo.getReferenceById(dto.getIdInitialUnit()));
        entity.setIdFinalUnit(measureUnitRepo.getReferenceById(dto.getIdFinalUnit()));
        entity.setIdResource(dto.getIdResource());
        entity.setOperation(dto.getOperation());
        entity.setConstant(dto.getConstant());
        return entity;
    }

}
