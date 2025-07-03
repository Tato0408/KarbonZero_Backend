package project.karbonzero.Controllers.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.karbonzero.Models.DTO.ResourceDTO;
import project.karbonzero.Models.DTO.ResourcePurityDTO;
import project.karbonzero.Services.Resource.ResourcePurityService;
import project.karbonzero.Services.Resource.ResourceService;
import java.util.List;

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
}
