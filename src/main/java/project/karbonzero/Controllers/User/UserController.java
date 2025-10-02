package project.karbonzero.Controllers.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import project.karbonzero.Models.DTO.ConsumptionDTO;
import project.karbonzero.Models.DTO.RolDTO;
import project.karbonzero.Models.DTO.UserDTO;
import project.karbonzero.Services.User.RolService;
import project.karbonzero.Services.User.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.karbonzero.Utils.Responses.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/apiUser")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RolService rolService;

    @PostMapping("/confirmPassword")
    public  HashMap<String, Boolean> confirmPassword (@RequestParam String idUser,
                                                      @RequestParam String password) {
        HashMap<String, Boolean> answer = new HashMap<>();
        answer.put("authenticated", userService.confirmPassword(idUser, password));
        return answer;
    }

    @PostMapping("/loginUser")
    public HashMap<String, Boolean> loginUser (@RequestParam String email,
                              @RequestParam String password) {

        HashMap<String, Boolean> answer = new HashMap<>();
        answer.put("authenticated", userService.loginUser(email, password));
        return answer;
    }

    @GetMapping("/getAllUsers")
    public Page<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
           return null;
        }
        Page<UserDTO> answer = userService.getAllUsers(page, size);
        answer.forEach(user -> {user.setUserPassword(null);});
        return answer;
    }

    @GetMapping("/getUserById/{id}")
    public UserDTO getUserById (@PathVariable String id) {
        UserDTO answer = userService.getUserById(id);
        answer.setUserPassword(null);
        return answer;
    }

    @GetMapping("/getUserByEmail/{email}")
    public UserDTO getUserByEmail (@PathVariable String email) {
        UserDTO answer = userService.getUserByEmail(email);
        answer.setUserPassword(null);
        return answer;
    }

    @GetMapping("/getUsersByUsername/{username}")
    public Page<UserDTO> getUsersByUsername (@PathVariable String username,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
            return null;
        }
        Page<UserDTO> answer = userService.getUsersByUsername(username, page, size);
        answer.forEach(user -> {user.setUserPassword(null);});
        return answer;
    }

    @GetMapping("/getUserConsumptions/{id}")
    public List<ConsumptionDTO> getUserConsumptions (@PathVariable String id) {
        return userService.getUserConsumptions(id);
    }

    @GetMapping("/getAllRolesList")
    public List<RolDTO> getAllRolesList() {
        return rolService.getAllRoles();
    }

    @GetMapping("/getAllRoles")
    public Page<RolDTO> getALlRoles(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
            return null;
        }
        return rolService.getAllRoles(page, size);
    }

    @GetMapping("/getRolesByName/{name}")
    public Page<RolDTO> getRolesByName (@PathVariable String name,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        if (size <= 0 || size > 50) {
            return null;
        }

        return rolService.getRolesByName(name, page, size);
    }

    @GetMapping("/getRolById/{id}")
    public RolDTO getRolById (@PathVariable String id) {
        return rolService.getRolById(id);
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

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            UserDTO answer = userService.updateUser(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a User object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a User object");
        }
    }

    @PutMapping("/updateRol/{id}")
    public ResponseEntity<?> updateRol(
            @PathVariable String id,
            @Valid @RequestBody RolDTO dto,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return responseBadRequestBinding(bindingResult);
        }

        try {
            RolDTO answer = rolService.updateRol(id, dto);

            if (answer == null) {
                return responseBadRequest("Invalid data to update a Rol object");
            }
            return responseUpdated(answer);
        } catch (Exception e) {
            return responseServerError(e, "Can't update a Rol object");
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        try {
            if (!userService.deleteUser(id)) {
                return responseNotFound("User not found");
            }

            return responseOK("User deleted with success");

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return responseServerError(e, "Can't delete the User object");
        }
    }

    @DeleteMapping("/deleteRol/{id}")
    public ResponseEntity<Map<String, Object>> deleteRol(@PathVariable String id) {
        try {
            if (!rolService.deleteRol(id)) {
                return responseNotFound("Rol not found");
            }

            return responseOK("Rol deleted with success");

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return responseServerError(e, "Can't delete the Rol object");
        }
    }

    @PostMapping("/updateDisabled/{id}")
    public ResponseEntity<Map<String,Object>> banUser(@PathVariable String id,  @RequestBody UserDTO dto){
        try {
            if (!userService.updateDisabled(id, dto)) {
                return responseNotFound("user not found");
            }
            return responseOK("User updated with succes");
        } catch (Exception e) {
            return responseServerError(e, "Cannot block the user object");
        }
    }
}
