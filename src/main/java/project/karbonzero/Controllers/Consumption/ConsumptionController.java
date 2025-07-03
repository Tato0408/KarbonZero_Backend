package project.karbonzero.Controllers.Consumption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.List;

@RestController
@RequestMapping("/apiConsumption")
public class ConsumptionController {

    @Autowired
    private ConsumptionService service;

    @GetMapping("/getAllConsumptions")
    public List<ConsumptionDTO> getAllConsumptions () {
        return service.getAllConsumptions();
    }
}
