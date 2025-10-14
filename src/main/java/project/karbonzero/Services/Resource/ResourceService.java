package project.karbonzero.Services.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;
import project.karbonzero.Entities.Measure.MeasureUnitEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Exceptions.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Repositories.Consumption.ConsumptionRepository;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.HashMap;
import java.util.List;
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

    public Page<ResourceDTO> getAllResources (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ResourceEntity> resources = repo.findAll(pageable);
        return resources.map(this::ResourceToDTO);
    }
    public List<ResourceDTO> getAllResourcesSP () {
        List<ResourceEntity> resources = repo.findAll();
        return resources.stream().map(this::ResourceToDTO).collect(Collectors.toList());
    }


    public ResourceDTO getResourceById (String id) {
        if (repo.existsById(id)) {
            ResourceEntity entity = repo.getReferenceById(id);
            return ResourceToDTO(entity);
        }
        return null;
    }

    public Page<ResourceDTO> getResourcesByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ResourceEntity> resources = repo.findByNameContains(name, pageable);
        return resources.map(this::ResourceToDTO);
    }

    public List<ConsumptionDTO> getResourceConsumptions(String id) {
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

    public HashMap<String, Object> getResourceConsumptionsCO2(String id) {
        ResourceEntity entity = repo.getReferenceById(id);
        List<ResourcePurityEntity> resourcePurityEntities = entity.getResourcePuritiesList();

        HashMap<String, Object> mergedAnswer = new HashMap<>();
        resourcePurityEntities.forEach(x -> {
            List<ConsumptionEntity> consumptionEntities = x.getConsumptionsList();
            consumptionEntities.forEach(y -> {
                mergedAnswer.put(y.getConsumptionDate().toString(),
                        (y.getQuantity() * y.getIdResourcePurity().getPurity() * y.getIdResourcePurity().getIdResource().getCarbonFootprint()));
            });
        });

        return mergedAnswer;
    }

    public List<HashMap<String, Object>> getAllResourcesConsumptionsCO2() {
        List<ResourceDTO> resources = repo.findAll().stream()
                .map(this::ResourceToDTO)
                .toList();

        return resources.stream()
                .map(resource -> {
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("id", resource.getIdResource());
                    hashMap.put("name", resource.getName());
                    hashMap.put("data", getResourceConsumptionsCO2(resource.getIdResource()));
                    return hashMap;
                })
                .collect(Collectors.toList());
    }

    public HashMap<String, Double> getAllResourceConsumptionsCO2Total() {

        HashMap<String, Double> mergedAnswer = new HashMap<>();
        repo.findAll()
            .forEach(resource -> {
                resource.getResourcePuritiesList()
                   .forEach(purity -> {
                       purity.getConsumptionsList()
                           .forEach(consumption -> {
                               if (!mergedAnswer.containsKey(resource.getName())) {
                                   mergedAnswer.put(resource.getName(), 0.0);
                               }
                               mergedAnswer.put(resource.getName(),
                                   (consumption.getQuantity() *
                                   purity.getPurity() *
                                   resource.getCarbonFootprint()) +
                                   mergedAnswer.get(resource.getName())
                               );
                           });
                       });
            });

        return mergedAnswer;
    }

    public ResourceDTO insertResource (ResourceDTO dto) {
        if (dto == null) {
            throw new BadDataException("Resource can't be null or empty");
        }

        try {
            ResourceEntity entity = DTOToResource(dto);
            ResourceEntity savedEntity = repo.save(entity);

            return ResourceToDTO(savedEntity);
        } catch (Exception e) {
            throw new CreateException("Can`t create Resource " + e.getMessage());
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
            throw new BadIdException("The Resource with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new UpdateException("Can`t update Resource " + e.getMessage());
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
        } catch (Exception e) {
            throw new DeleteException("The Resource with the ID : " + id + " doesn't exists");
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
