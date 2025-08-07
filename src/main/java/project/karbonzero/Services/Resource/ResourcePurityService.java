package project.karbonzero.Services.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.ResourcePurityDTO;
import project.karbonzero.Repositories.Resource.ResourcePurityRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourcePurityService {

    @Autowired
    private ResourcePurityRepository repo;
    @Autowired
    private ResourceRepository resourceRepo;

    public List<ResourcePurityDTO> getAllResourcePurities() {
        List<ResourcePurityEntity> resourcePurities = repo.findAll();
        return resourcePurities.stream()
                .map(this::ResourcePurityToDTO)
                .collect(Collectors.toList());
    }

    public ResourcePurityDTO getResourcePurityById (String id) {
        if (repo.existsById(id)) {
            ResourcePurityEntity entity = repo.getReferenceById(id);
            return  ResourcePurityToDTO(entity);
        }
        return null;
    }

    public List<ConsumptionDTO> getResourcePurityConsumptions (String id) {
        ResourcePurityEntity entity = repo.getReferenceById(id);
        return entity.getConsumptionsList().stream()
                .map(new ConsumptionService()::ConsumptionToDTO)
                .collect(Collectors.toList());
    }

    public ResourcePurityDTO insertResourcePurity(ResourcePurityDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Resource Purity can't be null or empty");
        }

        try {
            ResourcePurityEntity entity = DTOToResourcePurity(dto);
            ResourcePurityEntity savedEntity = repo.save(entity);

            return ResourcePurityToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create Resource Purity " + e.getMessage());
        }
    }

    public ResourcePurityDTO updateResourcePurity(String id, ResourcePurityDTO dto){
        try {
            if (repo.existsById(id)){
                ResourcePurityEntity entity = repo.getReferenceById(id);

                entity.setIdResource(resourceRepo.getReferenceById(dto.getIdResource()));
                entity.setPurity(dto.getPurity());

                ResourcePurityEntity savedEntity = repo.save(entity);
                return ResourcePurityToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Resource Purity with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Resource Purity " + e.getMessage());
        }
    }

    public boolean deleteResourcePurity(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Resource Purity with the ID : " + id + " doesn't exists", 1);
        }
    }

    public ResourcePurityDTO ResourcePurityToDTO(ResourcePurityEntity entity) {
        ResourcePurityDTO dto = new ResourcePurityDTO();
        dto.setIdResourcePurity(entity.getIdResourcePurity());
        dto.setPurity(entity.getPurity());
        if(entity.getIdResource() != null){
            dto.setResourceName(entity.getIdResource().getName());
            dto.setResourcePurityMeasureUnit(entity.getIdResource().getIdMeasureUnit().getName());
            dto.setIdResource(entity.getIdResource().getIdResource());
        }else{
            dto.setIdResource(null);
            dto.setResourcePurityMeasureUnit(null);
            dto.setResourceName("No hay un recursos asignado");
        }
        return dto;
    }

    public ResourcePurityEntity DTOToResourcePurity(ResourcePurityDTO dto) {
        ResourcePurityEntity entity = new ResourcePurityEntity();
        entity.setIdResourcePurity(dto.getIdResourcePurity());
        entity.setPurity(dto.getPurity());
        if(dto.getIdResource() != null){
            ResourceEntity resource = resourceRepo.findById(dto.getIdResource()).orElseThrow(()->new IllegalArgumentException("Recursos no encontrado con el id " + dto.getIdResource()));
            entity.setIdResource(resource);
        }
        return entity;
    }
}
