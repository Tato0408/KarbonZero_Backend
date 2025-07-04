package project.karbonzero.Controllers.Resource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Models.DTO.ResourcePurityDTO;
import project.karbonzero.Services.Resource.ResourcePurityService;
import project.karbonzero.Services.Resource.ResourceService;
import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@RequestMapping("/apiResource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePurityService resourcePurityService;

    @GetMapping("/getAllResources")
    public List<ResourceDTO> getAllResources () {
        return resourceService.getAllResources();
    }

    @GetMapping("/getAllResourcePurities")
    public List<ResourcePurityDTO> getAllResourcePurities () {
        return  resourcePurityService.getAllResourcePurities();
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
