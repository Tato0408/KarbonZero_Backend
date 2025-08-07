package project.karbonzero.Services.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Repositories.Consumption.ConsumptionRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository repo;
    @Autowired
    private MeasureUnitRepository measureUnitRepo;
    @Autowired
    private ConsumptionRepository consumptionRepo;

    public List<ResourceDTO> getAllResources () {
        List<ResourceEntity> resources = repo.findAll();
        return resources.stream()
                .map(this::ResourceToDTO)
                .collect(Collectors.toList());
    }

    public ResourceDTO getResourceById (String id) {
        if (repo.existsById(id)) {
            ResourceEntity entity = repo.getReferenceById(id);
            return ResourceToDTO(entity);
        }
        return null;
    }

    public List<ConsumptionDTO> getAllResourceConsumptions (String id) {
        ResourceEntity entity = repo.getReferenceById(id);

        List<ResourcePurityEntity> resourcePurityEntities = entity.getResourcePuritiesList();
        return resourcePurityEntities.stream()
                .map(x -> {
                    return x.getConsumptionsList().stream()
                            .map(new ConsumptionService()::ConsumptionToDTO)
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getAllResourceConsumptionsCO2 (String id) {
        ResourceEntity entity = repo.getReferenceById(id);
        List<ResourcePurityEntity> resourcePurityEntities = entity.getResourcePuritiesList();

        HashMap<String, Object> mergedAnswer = new HashMap<>();
        resourcePurityEntities.forEach(x -> {
            List<ConsumptionEntity> consumptionEntities = x.getConsumptionsList();
            consumptionEntities.forEach(y -> {
                HashMap<String, Object> answer = new HashMap<>();
                mergedAnswer.put(y.getConsumptionDate(),
                        (y.getQuantity() * y.getIdResourcePurity().getPurity() * y.getIdResourcePurity().getIdResource().getCarbonFootprint()));
            });
        });

        return mergedAnswer;
    }

    public ResourceDTO insertResource (ResourceDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Resource can't be null or empty");
        }

        try {
            ResourceEntity entity = DTOToResource(dto);
            ResourceEntity savedEntity = repo.save(entity);

            return ResourceToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create Resource " + e.getMessage());
        }
    }

    public ResourceDTO updateResource(String id, ResourceDTO dto){
        try {
            if (repo.existsById(id)){
                ResourceEntity entity = repo.getReferenceById(id);

                entity.setName(dto.getName());
                entity.setIdMeasureUnit(measureUnitRepo.getReferenceById(dto.getIdMeasureUnit()));
                entity.setCarbonFootprint(dto.getCarbonFootprint());

                ResourceEntity savedEntity = repo.save(entity);
                return ResourceToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Resource with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Resource " + e.getMessage());
        }
    }

    public boolean deleteResource(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Resource with the ID : " + id + " doesn't exists", 1);
        }
    }

    public ResourceDTO ResourceToDTO(ResourceEntity entity) {
        ResourceDTO dto = new ResourceDTO();
        dto.setIdResource(entity.getIdResource());
        dto.setName(entity.getName());
        dto.setCarbonFootprint(entity.getCarbonFootprint());
        if(entity.getIdMeasureUnit() !=null){
            dto.setMeasureName(entity.getIdMeasureUnit().getName());
            dto.setIdMeasureUnit(entity.getIdMeasureUnit().getIdMeasureUnit());
        }
        return dto;
    }

    public ResourceEntity DTOToResource(ResourceDTO dto) {
        ResourceEntity entity = new ResourceEntity();
        entity.setIdResource(dto.getIdResource());
        entity.setName(dto.getName());
        entity.setCarbonFootprint(dto.getCarbonFootprint());
        if(dto.getIdMeasureUnit() !=null){
            MeasureUnitEntity measureUnit = measureUnitRepo.findById(dto.getIdMeasureUnit()).orElseThrow(()-> new IllegalArgumentException("No se pudo encontrar la unidad de medida con el id " + dto.getIdMeasureUnit()));
            entity.setIdMeasureUnit(measureUnit);
        }
        return entity;
    }
}
