package project.karbonzero.Controllers.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import project.karbonzero.Models.DTO.RolDTO;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Services.User.RolService;
import project.karbonzero.Services.User.UserService;
import project.karbonzero.Utils.Responses;

import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@RequestMapping("/apiUser")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RolService rolService;

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getAllRols")
    public List<RolDTO> getALlRols() {
        return rolService.getAllRols();
    }

    @PostMapping("/insertUser")
    public ResponseEntity<Map<String, Object>> insertUser(@Valid @RequestBody UserDTO dto, HttpServletRequest request) {
        try {
            UserDTO answer = userService.insertUser(dto);

            if (answer == null) {
              return responseBadRequest("Invalid data to create a user object");
            }
            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't create user object");
        }
    }

    @PostMapping("/insertRol")
    public ResponseEntity<Map<String, Object>> insertRol (@Valid @RequestBody RolDTO dto,  HttpServletRequest request) {
        try {
            RolDTO answer = rolService.insertRol(dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to create a rol object");
            }
            return responseCreated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can`t create rol object");
        }
    }
}
