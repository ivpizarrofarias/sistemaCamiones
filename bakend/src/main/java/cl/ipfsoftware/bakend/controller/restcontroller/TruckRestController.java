package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.TruckDTO;
import cl.ipfsoftware.bakend.service.TruckService;
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
@RequestMapping("/api/camiones")
public class TruckRestController {

    private final TruckService truckService;

    @Autowired
    public TruckRestController(TruckService truckService) {
        this.truckService = truckService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> createTruck(
            @Valid @RequestBody TruckDTO truckDTO,
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

        if (truckService.existsByLicensePlate(truckDTO.getLicensePlate())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe un camión con la patente: " + truckDTO.getLicensePlate());
            return ResponseEntity.badRequest().body(response);
        }

        TruckDTO created = truckService.createTruck(truckDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "El camión ha sido creado con éxito");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTruckById(@PathVariable Integer id) {
        Optional<TruckDTO> truckDTO = truckService.getTruckById(id);

        return truckDTO
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Camión no encontrado con ID: " + id);
                    return ResponseEntity.status(404).body(response);
                });
    }

    @GetMapping
    public ResponseEntity<Object> getAllTrucks() {
        List<TruckDTO> list = truckService.getAllTrucks();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", list);
        response.put("count", list.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Map<String, Object>> updateTruck(
            @PathVariable Integer id,
            @Valid @RequestBody TruckDTO truckDTO,
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

        if (!truckService.existsTruckById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Camión no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        Optional<TruckDTO> existing = truckService.getTruckById(id);
        if (existing.isPresent()
                && !existing.get().getLicensePlate().equals(truckDTO.getLicensePlate())
                && truckService.existsByLicensePlate(truckDTO.getLicensePlate())) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe otro camión con la patente: " + truckDTO.getLicensePlate());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<TruckDTO> updated = truckService.updateTruck(id, truckDTO);

        return updated
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "El camión ha sido modificado con éxito");
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Error al actualizar el camión");
                    return ResponseEntity.status(500).body(response);
                });
    }

    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Object> deleteTruck(@PathVariable Integer id) {

        if (!truckService.existsTruckById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Camión no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        try {
            truckService.deleteTruckById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "El camión ha sido eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el camión: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
