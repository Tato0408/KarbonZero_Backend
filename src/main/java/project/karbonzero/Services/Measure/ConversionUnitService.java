package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


}
