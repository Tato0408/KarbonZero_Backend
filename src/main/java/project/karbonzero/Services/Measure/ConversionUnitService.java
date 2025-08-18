package project.karbonzero.Services.Measure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Repositories.Measure.ConversionUnitRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConversionUnitService {

    @Autowired
    private ConversionUnitRepository repo;
    @Autowired
    private MeasureUnitRepository measureUnitRepo;
    @Autowired
    private ResourceRepository resourceRepo;


    public List<ConversionUnitDTO> getAllConversionUnits (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConversionUnitEntity> conversionUnits = repo.findAll(pageable);
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

    public List<ConversionUnitDTO> getConversionUnitsByNames(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasureUnitEntity> measureUnits = measureUnitRepo.findByNameContains(name,pageable);

        return measureUnits.stream()
                .map(x -> {
                    return repo.findByIdInitialUnitOrIdFinalUnit(x, x).stream()
                            .map(this::ConversionUnitToDTO)
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    public List<ConversionUnitDTO> getConversionUnitPath(String idMeasure1, String idMeasure2, String idResource) {
        HashMap<ConversionUnitEntity, ConversionUnitEntity> parent = new HashMap<>();
        ResourceEntity resource = resourceRepo.getReferenceById(idResource);

        List<ConversionUnitEntity> queue = new ArrayList<>();
        queue.addAll(
                repo.findByIdInitialUnitAndIdResource(
                        measureUnitRepo.getReferenceById(idMeasure1),
                        resource));
        queue.addAll(
                repo.findByIdInitialUnitAndIdResource(
                        measureUnitRepo.getReferenceById(idMeasure1),
                        null));
        Set<ConversionUnitEntity> exploredSet = new HashSet<>();

        while (!queue.isEmpty()) {
            ConversionUnitEntity conversionUnitEntity = queue.get(0);
            queue.remove(0);
            exploredSet.add(conversionUnitEntity);

            if (conversionUnitEntity.getIdInitialUnit().getIdMeasureUnit().equals(idMeasure2)
            || conversionUnitEntity.getIdFinalUnit().getIdMeasureUnit().equals(idMeasure2)) {
                List<ConversionUnitDTO> answer = new ArrayList<>();
                ConversionUnitEntity actualConversion = conversionUnitEntity;

                answer.add(ConversionUnitToDTO(actualConversion));
                while (parent.containsKey(actualConversion)) {
                    answer.add(ConversionUnitToDTO(parent.get(actualConversion)));
                    actualConversion = parent.get(actualConversion);
                }
                Collections.reverse(answer);
                return answer;
            }

            List<ConversionUnitEntity> nextConversionUnits = new ArrayList<>();
            nextConversionUnits.addAll(
                    repo.findByIdInitialUnitAndIdResource(
                        conversionUnitEntity.getIdFinalUnit(), resource));
            nextConversionUnits.addAll(
                    repo.findByIdInitialUnitAndIdResource(
                            conversionUnitEntity.getIdFinalUnit(), null));

            for (ConversionUnitEntity nextConversionUnitEntity :  nextConversionUnits) {
                if (!exploredSet.contains(nextConversionUnitEntity)) {
                    parent.put(nextConversionUnitEntity, conversionUnitEntity);
                    queue.add(nextConversionUnitEntity);
                }
            };
        }
        return null;
    }

    public Double convertMeasureUnit (String idMeasure1, String idMeasure2, String idResource, Double value) {
        List<ConversionUnitDTO> path = getConversionUnitPath(idMeasure1, idMeasure2, idResource);
        if (path == null) {
            return null;
        }

        for (ConversionUnitDTO conversionUnitDTO : path) {
            if (conversionUnitDTO.getOperation().equals("PROD")) {
                value *= conversionUnitDTO.getConstant();
            } else if (conversionUnitDTO.getOperation().equals("DIV")) {
                //TODO: Agregar validaciones para la división por cero
                value /= conversionUnitDTO.getConstant();
            } else if (conversionUnitDTO.getOperation().equals("SUM")) {
                value += conversionUnitDTO.getConstant();
            } else {
                value -= conversionUnitDTO.getConstant();
            }
        };
        return value;
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

                if (dto.getIdResource().isBlank()) {
                    entity.setIdResource(null);
                } else {
                    entity.setIdResource(resourceRepo.getReferenceById(dto.getIdResource()));
                }

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
        if (entity.getIdResource() != null) {
            dto.setIdResource(entity.getIdResource().getIdResource());
            dto.setNameResource(entity.getIdResource().getName());
        } else {
            dto.setIdResource(null);
            dto.setNameResource("Universal");
        }

        dto.setOperation(entity.getOperation());
        dto.setConstant(entity.getConstant());
        dto.setNameInitialUnit(entity.getIdInitialUnit().getName());
        dto.setNameFinalUnit(entity.getIdFinalUnit().getName());
        dto.setIdInitialUnit(entity.getIdInitialUnit().getIdMeasureUnit());
        dto.setIdFinalUnit(entity.getIdFinalUnit().getIdMeasureUnit());
        return dto;
    }

    public ConversionUnitEntity DTOToConversionUnit(ConversionUnitDTO dto) {
        ConversionUnitEntity entity = new ConversionUnitEntity();
        entity.setIdConversionUnit(dto.getIdConversionUnit());
        entity.setOperation(dto.getOperation());
        entity.setConstant(dto.getConstant());
        if (dto.getIdResource().isBlank()) {
            entity.setIdResource(null);
        } else {
            entity.setIdResource(resourceRepo.getReferenceById(dto.getIdResource()));
        }
        MeasureUnitEntity finalMeasureUnit = measureUnitRepo.findById(dto.getIdFinalUnit()).orElseThrow(()-> new IllegalArgumentException("Unidad de medida no encontrada con ese ID " + dto.getIdFinalUnit()));
        MeasureUnitEntity initialMeasureUnit = measureUnitRepo.findById(dto.getIdInitialUnit()).orElseThrow(()-> new IllegalArgumentException("Unidad de medida no encontrada con ese ID " + dto.getIdInitialUnit()));

        entity.setIdInitialUnit(initialMeasureUnit);
        entity.setIdFinalUnit(finalMeasureUnit);

        return entity;
    }


}
