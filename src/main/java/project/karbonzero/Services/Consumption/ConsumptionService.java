package project.karbonzero.Services.Consumption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.Consumption.ConsumptionEntity;
import project.karbonzero.Entities.Resource.ResourceEntity;
import project.karbonzero.Entities.Resource.ResourcePurityEntity;
import project.karbonzero.Exceptions.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Repositories.Consumption.ConsumptionRepository;
import project.karbonzero.Repositories.Resource.ResourcePurityRepository;
import project.karbonzero.Repositories.Resource.ResourceRepository;
import project.karbonzero.Repositories.User.UserRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    private ResourceRepository resourceRepo;

    public Page<ConsumptionDTO> getAllConsumptions(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ConsumptionEntity> consumptions = repo.findAllByOrderByConsumptionDateDesc(pageable);
        return consumptions.map(this::ConsumptionToDTO);
    }

    public ConsumptionDTO getConsumptionById (String id) {
        if (repo.existsById(id)) {
            ConsumptionEntity entity = repo.getReferenceById(id);
            return ConsumptionToDTO(entity);
        }
        return null;
    }

    public Page<ConsumptionDTO> getConsumptionsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ResourceEntity> resources = resourceRepo.findByNameContains(name);

        List<ResourcePurityEntity> resourcePurities = resources.stream()
                .map(ResourceEntity::getResourcePuritiesList)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return repo.findByIdResourcePurityIn(resourcePurities, pageable).map(this::ConsumptionToDTO);
    }

    public Page<ConsumptionDTO> getConsumptionsByFilters(String name, int year, int month, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<ResourceEntity> resources;
        if (!name.isEmpty()) {
            resources = resourceRepo.findByNameContains(name);
        } else {
            resources = resourceRepo.findAll();
        }

        List<ResourcePurityEntity> resourcePurities = resources.stream()
                .map(ResourceEntity::getResourcePuritiesList)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (year == 0) {
            return repo.findByIdResourcePurityIn(resourcePurities, pageable)
                    .map(this::ConsumptionToDTO);
        }

        LocalDate initialDate;
        LocalDate finalDate;

        if (month != 0) {
            YearMonth ym = YearMonth.of(year, month);
            initialDate = ym.atDay(1);
            finalDate   = ym.plusMonths(1).atDay(1);
        } else {
            initialDate = LocalDate.of(year, 1, 1);
            finalDate   = LocalDate.of(year + 1, 1, 1);
        }

        return repo.findByIdResourcePurityInAndConsumptionDateBetweenOrderByConsumptionDateDesc(
                        resourcePurities, initialDate, finalDate, pageable)
                .map(this::ConsumptionToDTO);
    }

    public List<ConsumptionDTO> getConsumptionsByFiltersMonth(String name, String start, String end) {
        List<ResourceEntity> resources;
        if (!name.isEmpty()) {
            resources = resourceRepo.findByNameContains(name);
        } else {
            resources = resourceRepo.findAll();
        }

        List<ResourcePurityEntity> resourcePurities = resources.stream()
                .map(ResourceEntity::getResourcePuritiesList)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate   = LocalDate.parse(end);

        return repo.findByIdResourcePurityInAndConsumptionDateBetweenOrderByConsumptionDateDesc(
                        resourcePurities, startDate, endDate)
                .stream()
                .map(this::ConsumptionToDTO)
                .collect(Collectors.toList());
    }
    public Double getConsumptionCO2 (String id) {
        ConsumptionEntity entity = repo.getReferenceById(id);
        return (entity.getQuantity() * entity.getIdResourcePurity().getPurity() * entity.getIdResourcePurity().getIdResource().getCarbonFootprint());
    }

    public ConsumptionDTO insertConsumption (ConsumptionDTO dto) {
        if (dto == null) {
            throw new BadDataException("Consumption can't be null or empty");
        }
        try {
            ConsumptionEntity entity = DTOToConsumption(dto);
            ConsumptionEntity savedEntity = repo.save(entity);

            return ConsumptionToDTO(savedEntity);
        } catch (Exception e) {
            throw new CreateException("Can`t create Consumption " + e.getMessage());
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
            throw new BadIdException("The Consumption with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new UpdateException("Can`t update Consumption " + e.getMessage());
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
        } catch (Exception e) {
            throw new DeleteException("The Consumption with the ID : " + id + " doesn't exists");
        }
    }

    public ConsumptionDTO ConsumptionToDTO(ConsumptionEntity entity) {
        ConsumptionDTO dto = new ConsumptionDTO();
        dto.setIdConsumption(entity.getIdConsumption());
        dto.setIdResourcePurity(entity.getIdResourcePurity().getIdResourcePurity());
        dto.setResourcePurityName(entity.getIdResourcePurity().getIdResource().getName() + ": " + entity.getIdResourcePurity().getPurity());
        dto.setResourceMeasureUnit(entity.getIdResourcePurity().getIdResource().getIdMeasureUnit().getName());
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
