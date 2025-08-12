package project.karbonzero.Controllers.Resource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Models.DTO.ResourcePurityDTO;
import project.karbonzero.Services.Resource.ResourcePurityService;
import project.karbonzero.Services.Resource.ResourceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/apiResource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePurityService resourcePurityService;

    @GetMapping("/getAllResources")
    public List<ResourceDTO> getAllResources (@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
            return null;
        }
        return resourceService.getAllResources(page, size);
    }

    @GetMapping("/getResourceById/{id}")
    public ResourceDTO getResourceById (@PathVariable String id) {
        return resourceService.getResourceById(id);
    }

    @GetMapping("/getResourcesByName/{name}")
    public List<ResourceDTO> getResourcesByName (@PathVariable String name,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
            return null;
        }
        return resourceService.getResourcesByName(name, page, size);
    }

    @GetMapping("/getResourceConsumptions/{id}")
    public List<ConsumptionDTO> getResourceConsumptions(@PathVariable String id) {
        return resourceService.getResourceConsumptions(id);
    }

    @GetMapping("/getResourceConsumptionsCO2/{id}")
    public HashMap<String, Object> getResourceConsumptionsCO2(@PathVariable String id) {
        return resourceService.getResourceConsumptionsCO2(id);
    }

    @GetMapping("/getAllResourcesConsumptionsCO2")
    public List<HashMap<String, Object>> getAllResourcesConsumptionsCO2() {
        return resourceService.getAllResourcesConsumptionsCO2();
    }

    @GetMapping("/getAllResourceConsumptionsCO2Total")
    public HashMap<String, Double> getAllResourceConsumptionsCO2Total() {
        return resourceService.getAllResourceConsumptionsCO2Total();
    }

    @GetMapping("/getAllResourcePurities")
    public List<ResourcePurityDTO> getAllResourcePurities ( @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return  resourcePurityService.getAllResourcePurities(page, size);
    }

    @GetMapping("/getResourcePurityById/{id}")
    public ResourcePurityDTO getResourcePurityById (@PathVariable String id) {
        return resourcePurityService.getResourcePurityById(id);
    }

    @GetMapping("/getResourcePuritiesByName/{name}")
    public List<ResourcePurityDTO> getResourcePuritiesByName (@PathVariable String name,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return resourcePurityService.getResourcePuritiesByName(name, page, size);
    }

    @GetMapping("/getAllResourcePurityConsumptions/{id}")
    public List<ConsumptionDTO> getResourcePurityConsumptions (@PathVariable String id) {
        return resourcePurityService.getResourcePurityConsumptions(id);
    }

    @PostMapping("/insertResource")
    public ResponseEntity<Map<String, Object>> insertResource (@Valid @RequestBody ResourceDTO dto, HttpServletRequest request) {
        try {
            ResourceDTO answer = resourceService.insertResource(dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to create a Resource object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Resource object");
        }
    }

    @PostMapping("/insertResourcePurity")
    public ResponseEntity<Map<String, Object>> insertResourcePurity (@Valid @RequestBody ResourcePurityDTO dto, HttpServletRequest request) {
        try {
            ResourcePurityDTO answer = resourcePurityService.insertResourcePurity(dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to create a Resource Purity object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Resource Purity object");
        }
    }

    @PutMapping("/updateResource/{id}")
    public ResponseEntity<?> updateResource(
            @PathVariable String id,
            @Valid @RequestBody ResourceDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            ResourceDTO answer = resourceService.updateResource(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Resource object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Resource object");
        }
    }

    @PutMapping("/updateResourcePurity/{id}")
    public ResponseEntity<?> updateResourcePurity(
            @PathVariable String id,
            @Valid @RequestBody ResourcePurityDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            ResourcePurityDTO answer = resourcePurityService.updateResourcePurity(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Resource Purity object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Resource Purity object");
        }
    }

    @DeleteMapping("/deleteResource/{id}")
    public ResponseEntity<Map<String, Object>> deleteResource(@PathVariable String id) {
        try {
            if (!resourceService.deleteResource(id)) {
                return responseNotFound("Resource not found");
            }

            return responseOK("Resource deleted with success");

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return responseServerError(e, "Can't delete the Resource object");
        }
    }

    @DeleteMapping("/deleteResourcePurity/{id}")
    public ResponseEntity<Map<String, Object>> deleteResourcePurity(@PathVariable String id) {
        try {
            if (!resourcePurityService.deleteResourcePurity(id)) {
                return responseNotFound("Resource Purity not found");
            }

            return responseOK("Resource Purity deleted with success");

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return responseServerError(e, "Can't delete the Resource Purity object");
        }
    }
}
