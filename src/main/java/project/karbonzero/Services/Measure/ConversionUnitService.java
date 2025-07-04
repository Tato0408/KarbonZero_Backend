package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Repositories.Measure.ConversionUnitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConversionUnitService {

    @Autowired
    private ConversionUnitRepository repo;

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
        dto.setIdInitialUnit(entity.getIdInitialUnit());
        dto.setIdFinalUnit(entity.getIdFinalUnit());
        dto.setIdResource(entity.getIdResource());
        dto.setOperation(entity.getOperation());
        dto.setConstant(entity.getConstant());
        return dto;
    }

    private ConversionUnitEntity DTOToConversionUnit(ConversionUnitDTO dto) {
        ConversionUnitEntity entity = new ConversionUnitEntity();
        entity.setIdConversionUnit(dto.getIdConversionUnit());
        entity.setIdInitialUnit(dto.getIdInitialUnit());
        entity.setIdFinalUnit(dto.getIdFinalUnit());
        entity.setIdResource(dto.getIdResource());
        entity.setOperation(dto.getOperation());
        entity.setConstant(dto.getConstant());
        return entity;
    }

}
