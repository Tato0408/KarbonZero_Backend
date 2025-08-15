package project.karbonzero.Services.Consumption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Repositories.Consumption.ConsumptionRepository;
import project.karbonzero.Repositories.Resource.ResourcePurityRepository;
import project.karbonzero.Repositories.User.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConsumptionService {
    @Autowired
    private ConsumptionRepository repo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ResourcePurityRepository resourcePurityRepo;

    public List<ConsumptionDTO> getAllConsumptions(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ConsumptionEntity> consumptions = repo.findAll(pageable);
        return consumptions.stream()
                .map(this::ConsumptionToDTO)
                .collect(Collectors.toList());
    }

    public ConsumptionDTO getConsumptionById (String id) {
        if (repo.existsById(id)) {
            ConsumptionEntity entity = repo.getReferenceById(id);
            return ConsumptionToDTO(entity);
        }
        return null;
    }

    public Double getConsumptionCO2 (String id) {
        ConsumptionEntity entity = repo.getReferenceById(id);
        return (entity.getQuantity() * entity.getIdResourcePurity().getPurity() * entity.getIdResourcePurity().getIdResource().getCarbonFootprint());
    }

    public ConsumptionDTO insertConsumption (ConsumptionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Consumption can't be null or empty");
        }

        try {
            ConsumptionEntity entity = DTOToConsumption(dto);
            ConsumptionEntity savedEntity = repo.save(entity);

            return ConsumptionToDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create Consumption " + e.getMessage());
        }
    }

    public ConsumptionDTO updateConsumption(String id, ConsumptionDTO dto){
        try {
            if (repo.existsById(id)){
                ConsumptionEntity entity = repo.getReferenceById(id);

                entity.setIdUser(userRepo.getReferenceById(dto.getIdUser()));
                entity.setIdResourcePurity(resourcePurityRepo.getReferenceById(dto.getIdResourcePurity()));
                entity.setQuantity(dto.getQuantity());
                entity.setCost(dto.getCost());
                entity.setConsumptionDate(dto.getConsumptionDate());

                ConsumptionEntity savedEntity = repo.save(entity);
                return ConsumptionToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Consumption with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Consumption " + e.getMessage());
        }
    }

    public boolean deleteConsumption(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Consumption with the ID : " + id + " doesn't exists", 1);
        }
    }

    public ConsumptionDTO ConsumptionToDTO(ConsumptionEntity entity) {
        ConsumptionDTO dto = new ConsumptionDTO();
        dto.setIdConsumption(entity.getIdConsumption());
        dto.setIdResourcePurity(entity.getIdResourcePurity().getIdResourcePurity());
        dto.setResourcePurityName(entity.getIdResourcePurity().getIdResource().getName() + ": " + entity.getIdResourcePurity().getPurity());
        dto.setIdUser(entity.getIdUser().getIdUser());
        dto.setUserName(entity.getIdUser().getUsername());
        dto.setQuantity(entity.getQuantity());
        dto.setConsumptionDate(entity.getConsumptionDate());
        dto.setCost(entity.getCost());
        return dto;
    }

    public ConsumptionEntity DTOToConsumption(ConsumptionDTO dto) {
        ConsumptionEntity entity = new ConsumptionEntity();
        entity.setIdConsumption(dto.getIdConsumption());
        entity.setIdUser(userRepo.getReferenceById(dto.getIdUser()));
        entity.setIdResourcePurity(resourcePurityRepo.getReferenceById(dto.getIdResourcePurity()));
        entity.setQuantity(dto.getQuantity());
        entity.setConsumptionDate(dto.getConsumptionDate());
        entity.setCost(dto.getCost());
        return entity;
    }
}
