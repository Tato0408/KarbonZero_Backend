package project.karbonzero.Controllers.Measure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Entities.Measure.ConversionUnitEntity;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Models.DTO.MeasureDTO;
import project.karbonzero.Models.DTO.MeasureUnitDTO;
import project.karbonzero.Services.Measure.ConversionUnitService;
import project.karbonzero.Services.Measure.MeasureService;
import project.karbonzero.Services.Measure.MeasureUnitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;
import static project.karbonzero.Utils.Responses.responseCreated;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/apiMeasure")
public class MeasureController {

    @Autowired
    private MeasureService measureService;
    @Autowired
    private MeasureUnitService measureUnitService;
    @Autowired
    private ConversionUnitService conversionUnitService;

    @GetMapping("/getAllMeasuresList")
    public List<MeasureDTO> getAllMeasuresList () {
        return measureService.getAllMeasures();
    }

    @GetMapping("/getAllMeasures")
    public Page<MeasureDTO> getAllMeasures (@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return measureService.getAllMeasures(page, size);
    }

    @GetMapping("/getMeasureById/{id}")
    public MeasureDTO getMeasureByID (@PathVariable String id) {
        return measureService.getMeasureById(id);
    }

    @GetMapping("getMeasuresByName/{name}")
    public Page<MeasureDTO> getMeasuresByName (@PathVariable String name,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return measureService.getMeasuresByName(name, page, size);
    }

    @GetMapping("/getAllMeasureUnitsList")
    public List<MeasureUnitDTO> getAllMeasureUnitsList () {
        return  measureUnitService.getAllMeasureUnits();
    }

    @GetMapping("/getAllMeasureUnits")
    public Page<MeasureUnitDTO> getAllMeasureUnits (@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return  measureUnitService.getAllMeasureUnits(page, size);
    }

    @GetMapping("/getMeasureUnitById/{id}")
    public MeasureUnitDTO getMeasureUnitById (@PathVariable String id) {
        return measureUnitService.getMeasureUnitById(id);
    }

    @GetMapping("/getMeasureUnitsByName/{name}")
    public Page<MeasureUnitDTO> getMeasureUnitsByName (@PathVariable String name,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return measureUnitService.getMeasureUnitsByName(name, page, size);
    }

    @GetMapping("/getAllConversionUnits")
    public Page<ConversionUnitDTO> getAllConversionUnits (@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size >= 50) {
            return null;
        }
        return conversionUnitService.getAllConversionUnits(page, size);
    }

    @GetMapping("/getConversionUnitById/{id}")
    public ConversionUnitDTO getConversionUnitById (@PathVariable String id) {
        return conversionUnitService.getConversionUnitById(id);
    }

    @GetMapping("/getConversionUnitsByName/{name}")
    public Page<ConversionUnitDTO> getConversionUnitsByName (@PathVariable String name,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size){
        if (size <= 0 || size >= 50) {
            return null;
        }
        return conversionUnitService.getConversionUnitsByNames(name, page, size);
    }

    @GetMapping("/convertMeasureUnit")
    public HashMap<String, Double> convertMeasureUnit (@RequestParam String idMeasureUnit1,
                                                       @RequestParam String idMeasureUnit2,
                                                       @RequestParam String idResource,
                                                       @RequestParam Double value) {
        HashMap<String, Double> answer = new HashMap<String, Double>();
        answer.put("value",
                    conversionUnitService.convertMeasureUnit(idMeasureUnit1, idMeasureUnit2, idResource, value)
        );
        return  answer;
    }

    @PostMapping("/insertMeasure")
    public ResponseEntity<Map<String, Object>> insertMeasure(@Valid @RequestBody MeasureDTO dto, HttpServletRequest request) {
        try {
            MeasureDTO answer = measureService.insertMeasure(dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to create a Measure object");
            }
            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create Measure object");
        }
    }

    @PostMapping("insertMeasureUnit")
    public ResponseEntity<Map<String, Object>> insertMeasureUnit (@Valid @RequestBody MeasureUnitDTO dto, HttpServletRequest request) {
        try {
            MeasureUnitDTO answer = measureUnitService.insertMeasureUnit(dto);

            if (answer == null) {
                return  responseBadRequest("Invalid data to create a Measure Unit object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Measure Unit object");
        }

    }

    // TODO: insert reverse conversion Unit
    @PostMapping("insertConversionUnit")
    public ResponseEntity<Map<String, Object>> insertConversionUnit (@Valid @RequestBody ConversionUnitDTO dto, HttpServletRequest request) {
        try {
            ConversionUnitDTO answer = conversionUnitService.insertConversionUnit(dto);

            if (answer == null) {
                return  responseBadRequest("Invalid data to create a Conversion Unit object");
            }

            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create a Conversion Unit object");
        }

    }

    @PutMapping("/updateMeasure/{id}")
    public ResponseEntity<?> updateMeasure(
            @PathVariable String id,
            @Valid @RequestBody MeasureDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            MeasureDTO answer = measureService.updateMeasure(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Measure object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Measure object");
        }
    }

    @PutMapping("/updateMeasureUnit/{id}")
    public ResponseEntity<?> updateMeasureUnit(
            @PathVariable String id,
            @Valid @RequestBody MeasureUnitDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            MeasureUnitDTO answer = measureUnitService.updateMeasureUnit(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Measure Unit object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Measure Unit object");
        }
    }

    @PutMapping("/updateConversionUnit/{id}")
    public ResponseEntity<?> updateConversionUnit(
            @PathVariable String id,
            @Valid @RequestBody ConversionUnitDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            ConversionUnitDTO answer = conversionUnitService.updateConversionUnit(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Conversion Unit object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Conversion Unit object");
        }
    }

    @DeleteMapping("/deleteMeasure/{id}")
    public ResponseEntity<Map<String, Object>> deleteMeasure(@PathVariable String id) {
        try {
            if (!measureService.deleteMeasure(id)) {
                return responseNotFound("Measure not found");
            }

            return responseOK("Measure deleted with success");

        } catch (Exception e) {
            return responseServerError(e, "Can't delete the Measure object");
        }
    }

    @DeleteMapping("/deleteMeasureUnit/{id}")
    public ResponseEntity<Map<String, Object>> deleteMeasureUnit(@PathVariable String id) {
        try {
            if (!measureUnitService.deleteMeasureUnit(id)) {
                return responseNotFound("Measure Unit not found");
            }

            return responseOK("Measure Unit deleted with success");

        } catch (Exception e) {
            return responseServerError(e, "Can't delete the Measure Unit object");
        }
    }

    @DeleteMapping("/deleteConversionUnit/{id}")
    public ResponseEntity<Map<String, Object>> deleteConversionUnit(@PathVariable String id) {
        try {
            if (!conversionUnitService.deleteConversionUnit(id)) {
                return responseNotFound("Conversion Unit not found");
            }

            return responseOK("Conversion Unit deleted with success");

        } catch (Exception e) {
            return responseServerError(e, "Can't delete the Conversion Unit object");
        }
    }
}
