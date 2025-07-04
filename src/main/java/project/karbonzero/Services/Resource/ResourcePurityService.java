package project.karbonzero.Services.Resource;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Measure.MeasureEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Models.DTO.MeasureDTO;
import project.karbonzero.Models.DTO.ResourcePurityDTO;
import project.karbonzero.Repositories.Resource.ResourcePurityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourcePurityService {

    @Autowired
    private ResourcePurityRepository repo;

    public List<ResourcePurityDTO> getAllResourcePurities() {
        List<ResourcePurityEntity> resourcePurities = repo.findAll();
        return resourcePurities.stream()
                .map(this::ResourcePurityToDTO)
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
                ResourcePurityEntity entity = repo.getById(id);

                entity.setIdResource(dto.getIdResource());
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

    private ResourcePurityDTO ResourcePurityToDTO(ResourcePurityEntity entity) {
        ResourcePurityDTO dto = new ResourcePurityDTO();
        dto.setIdResourcePurity(entity.getIdResourcePurity());
        dto.setIdResource(entity.getIdResource());
        dto.setPurity(entity.getPurity());
        return dto;
    }

    private ResourcePurityEntity DTOToResourcePurity(ResourcePurityDTO dto) {
        ResourcePurityEntity entity = new ResourcePurityEntity();
        entity.setIdResourcePurity(dto.getIdResourcePurity());
        entity.setIdResource(dto.getIdResource());
        entity.setPurity(dto.getPurity());
        return entity;
    }
}
