package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.DriverDTO;
import cl.ipfsoftware.bakend.service.DriverService;
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
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
@RestController
@RequestMapping("/api/choferes")
public class DriverRestController {

    private final DriverService driverService;

    @Autowired
    public DriverRestController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> createDriver(
            @Valid @RequestBody DriverDTO driverDTO,
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

        if (driverService.existsByDriverRut(driverDTO.getDriverRut())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe un chofer con el RUT: " + driverDTO.getDriverRut());
            return ResponseEntity.badRequest().body(response);
        }

        DriverDTO created = driverService.createDriver(driverDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "El chofer ha sido creado con éxito");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDriverById(@PathVariable Integer id) {
        Optional<DriverDTO> driverDTO = driverService.getDriverById(id);

        return driverDTO
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Chofer no encontrado con ID: " + id);
                    return ResponseEntity.status(404).body(response);
                });
    }

    @GetMapping
    public ResponseEntity<Object> getAllDrivers() {
        List<DriverDTO> list = driverService.getAllDrivers();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", list);
        response.put("count", list.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Map<String, Object>> updateDriver(
            @PathVariable Integer id,
            @Valid @RequestBody DriverDTO driverDTO,
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

        if (!driverService.existsDriverById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Chofer no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        Optional<DriverDTO> existing = driverService.getDriverById(id);
        if (existing.isPresent()
                && !existing.get().getDriverRut().equals(driverDTO.getDriverRut())
                && driverService.existsByDriverRut(driverDTO.getDriverRut())) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe otro chofer con el RUT: " + driverDTO.getDriverRut());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<DriverDTO> updated = driverService.updateDriver(id, driverDTO);

        return updated
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "El chofer ha sido modificado con éxito");
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Error al actualizar el chofer");
                    return ResponseEntity.status(500).body(response);
                });
    }

    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Object> deleteDriver(@PathVariable Integer id) {

        if (!driverService.existsDriverById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Chofer no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        try {
            driverService.deleteDriverById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "El chofer ha sido eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el chofer: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
