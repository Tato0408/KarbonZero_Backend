package project.karbonzero.Controllers.Consumption;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Services.Consumption.ConsumptionService;

import java.util.*;

import static project.karbonzero.Utils.Responses.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/apiConsumption")
public class ConsumptionController {

    @Autowired
    private ConsumptionService consumptionService;

    @GetMapping("/getAllConsumptions")
    public Page<ConsumptionDTO> getAllConsumptions (@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size){
        if (size <= 0 || size >= 50) {
            return null;
        }
        return consumptionService.getAllConsumptions(page, size);
    }

    @GetMapping("/getConsumptionById/{id}")
    public ConsumptionDTO getConsumptionById (@PathVariable String id) {
        return consumptionService.getConsumptionById(id);
    }

    @GetMapping("getConsumptionCO2/{id}")
    public Map<String, Object> getConsumptionCO2(@PathVariable String id) {
        HashMap<String, Object> answer = new HashMap<String, Object>();
        answer.put("idConsumption", id);
        answer.put("co2Produced", consumptionService.getConsumptionCO2(id) );
        return answer;
    }

    @PostMapping("/insertConsumption")
    public ResponseEntity<Map<String, Object>> insertConsumption (@Valid @RequestBody ConsumptionDTO dto, HttpServletRequest request) {
        try {
            ConsumptionDTO answer = consumptionService.insertConsumption(dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to create a Consumption object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Consumption object");
        }
    }

    @PutMapping("/updateConsumption/{id}")
    public ResponseEntity<?> updateConsumption(
            @PathVariable String id,
            @Valid @RequestBody ConsumptionDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            ConsumptionDTO answer = consumptionService.updateConsumption(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Consumption object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Consumption object");
        }
    }

    @DeleteMapping("/deleteConsumption/{id}")
    public ResponseEntity<Map<String, Object>> deleteConsumption(@PathVariable String id) {
        try {
            if (!consumptionService.deleteConsumption(id)) {
                return responseNotFound("Consumption not found");
            }

            return responseOK("Consumption deleted with success");

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return responseServerError(e, "Can't delete the Consumption object");
        }
    }
}
