package project.karbonzero.Services.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Repositories.Measure.MeasureUnitRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository repo;
    @Autowired
    private MeasureUnitRepository measureUnitRepo;

    public List<ResourceDTO> getAllResources () {
        List<ResourceEntity> resources = repo.findAll();
        return resources.stream()
                .map(this::ResourceToDTO)
                .collect(Collectors.toList());
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
                ResourceEntity entity = repo.getById(id);

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

    private ResourceDTO ResourceToDTO(ResourceEntity entity) {
        ResourceDTO dto = new ResourceDTO();
        dto.setIdResource(entity.getIdResource());
        dto.setIdMeasureUnit(entity.getIdMeasureUnit().getIdMeasureUnit());
        dto.setName(entity.getName());
        dto.setCarbonFootprint(entity.getCarbonFootprint());

        return dto;
    }

    private ResourceEntity DTOToResource(ResourceDTO dto) {
        ResourceEntity entity = new ResourceEntity();
        entity.setIdResource(dto.getIdResource());
        entity.setIdMeasureUnit(measureUnitRepo.getReferenceById(dto.getIdMeasureUnit()));
        entity.setName(dto.getName());
        entity.setCarbonFootprint(dto.getCarbonFootprint());

        return entity;
    }
}
