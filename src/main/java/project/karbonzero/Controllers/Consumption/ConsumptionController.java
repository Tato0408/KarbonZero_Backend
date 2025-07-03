package project.karbonzero.Controllers.Consumption;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@RequestMapping("/apiConsumption")
public class ConsumptionController {

    @Autowired
    private ConsumptionService consumptionService;

    @GetMapping("/getAllConsumptions")
    public List<ConsumptionDTO> getAllConsumptions () {
        return consumptionService.getAllConsumptions();
    }

    @PostMapping("/insertConsumption")
    public ResponseEntity<Map<String, Object>> insertConsumption (@Valid @RequestBody ConsumptionDTO dto, HttpServletRequest request) {
        try {
            ConsumptionDTO answer = consumptionService.insertConsumption(dto);

            if (answer == null) {
                return  responseBadRequest("Invalid data to create a Consumption object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Consumption object");
        }
    }

}
