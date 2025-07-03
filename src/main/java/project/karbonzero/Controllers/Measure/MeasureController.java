package project.karbonzero.Controllers.Measure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.karbonzero.Models.DTO.ConversionUnitDTO;
import project.karbonzero.Models.DTO.MeasureDTO;
import project.karbonzero.Models.DTO.MeasureUnitDTO;
import project.karbonzero.Services.Measure.ConversionUnitService;
import project.karbonzero.Services.Measure.MeasureService;
import project.karbonzero.Services.Measure.MeasureUnitService;

import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@RequestMapping("/apiMeasure")
public class MeasureController {

    @Autowired
    private MeasureService measureService;
    @Autowired
    private MeasureUnitService measureUnitService;
    @Autowired
    private ConversionUnitService conversionUnitService;

    @GetMapping("/getAllMeasures")
    public List<MeasureDTO> getAllMeasures () {
        return measureService.getAllMeasures();
    }

    @GetMapping("/getAllMeasureUnits")
    public List<MeasureUnitDTO> getAllMeasureUnits () {
        return  measureUnitService.getAllMeasureUnits();
    }

    @GetMapping("/getAllConversionUnits")
    public List<ConversionUnitDTO> getAllConversionUnits () {
        return conversionUnitService.getAllConversionUnits();
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
}
