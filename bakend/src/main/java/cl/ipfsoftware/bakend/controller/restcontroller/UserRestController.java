package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.UserDTO;
import cl.ipfsoftware.bakend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true", maxAge = 3600
)
@RestController
@RequestMapping("/api/usuarios")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping("/guardar")
    public ResponseEntity<Object> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errors", bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(response);
        }

        // Validar correo duplicado
        if (userService.existsByEmail(userDTO.getEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe un usuario con el correo: " + userDTO.getEmail());
            return ResponseEntity.badRequest().body(response);
        }

        UserDTO created = userService.createUser(userDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "El usuario ha sido creado con éxito");
        response.put("data", created);

        return ResponseEntity.ok(response);
    }

    // =========================
    // READ BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer id) {
        Optional<UserDTO> userDTO = userService.getUserById(id);

        return userDTO
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Usuario no encontrado con ID: " + id);
                    return ResponseEntity.status(404).body(response);
                });
    }

    // =========================
    // READ ALL
    // =========================
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<UserDTO> list = userService.getAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", list);
        response.put("count", list.size());

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errors", bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(response);
        }

        // Verificar si el usuario existe
        if (!userService.existsUserById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        // Validar correo duplicado (excepto el mismo usuario)
        Optional<UserDTO> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()
                && !existingUser.get().getEmail().equals(userDTO.getEmail())
                && userService.existsByEmail(userDTO.getEmail())) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe otro usuario con el correo: " + userDTO.getEmail());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<UserDTO> updated = userService.updateUser(id, userDTO);

        return updated
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "El usuario ha sido modificado con éxito");
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Error al actualizar el usuario");
                    return ResponseEntity.status(500).body(response);
                });
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id) {

        if (!userService.existsUserById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        try {
            userService.deleteUserById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "El usuario ha sido eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
