package project.karbonzero.Services.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Repositories.Resource.ResourceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository repo;

    public List<ResourceDTO> getAllResources () {
        List<ResourceEntity> resources = repo.findAll();
        return resources.stream()
                .map(this::ResourceToDTO)
                .collect(Collectors.toList());
    }

    public ResourceDTO ResourceToDTO(ResourceEntity entity) {
        ResourceDTO dto = new ResourceDTO();
        dto.setIdResource(entity.getIdResource());
        dto.setIdMeasureUnit(entity.getIdMeasureUnit());
        dto.setName(entity.getName());
        dto.setCarbonFootprint(entity.getCarbonFootprint());

        return dto;
    }
}
