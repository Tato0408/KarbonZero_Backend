package project.karbonzero.Services.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.karbonzero.Entities.User.RolEntity;
import project.karbonzero.Models.DTO.RolDTO;
import project.karbonzero.Repositories.User.RolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolService {

    @Autowired
    private RolRepository repo;

    public List<RolDTO> getAllRoles () {
        List<RolEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::RolToDTO)
                .collect(Collectors.toList());
    }

    public Page<RolDTO> getAllRoles (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RolEntity> roles = repo.findAll(pageable);
        return roles.map(this::RolToDTO);
    }

    public RolDTO getRolById (String id) {
        if (repo.existsById(id)) {
            RolEntity entity =  repo.getReferenceById(id);
            return RolToDTO(entity);
        }
        return null;
    }

    public RolDTO insertRol (RolDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Rol can't be null or empty");
        }

        try {
            RolEntity entity = DTOToRol(dto);
            RolEntity savedEntity = repo.save(entity);

            return RolToDTO(savedEntity);
        }
        catch (Exception e) {
            throw new RuntimeException("Can`t create role " + e.getMessage());
        }

    }

    public RolDTO updateRol(String id, RolDTO dto){
        try {
            if (repo.existsById(id)){
                RolEntity entity = repo.getReferenceById(id);

                entity.setName(dto.getName());
                entity.setStatus(dto.getStatus());

                RolEntity savedEntity = repo.save(entity);
                return RolToDTO(savedEntity);
            }
            throw new IllegalArgumentException("The Rol with the ID : " + id + " doesn't exists");

        } catch (Exception e) {
            throw new RuntimeException("Can`t update Rol " + e.getMessage());
        }
    }

    public boolean deleteRol(String id){
        try {
            if (repo.existsById(id)){
                repo.deleteById(id);
                return true;
            } else{
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("The Rol with the ID : " + id + " doesn't exists", 1);
        }
    }

    public RolDTO RolToDTO (RolEntity entity) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(entity.getIdRol());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public RolEntity DTOToRol (RolDTO dto) {
        RolEntity entity = new RolEntity();
        entity.setIdRol(dto.getIdRol());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());

        return entity;
    }
}
