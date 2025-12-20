package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.ContainerDTO;
import cl.ipfsoftware.bakend.service.ContainerService;
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
@RequestMapping("/api/contenedores")
public class ContainerRestController {

    private final ContainerService containerService;

    @Autowired
    public ContainerRestController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> createContainer(
            @Valid @RequestBody ContainerDTO containerDTO,
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

        if (containerService.existsByContainerCode(containerDTO.getContainerCode())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe un contenedor con el código: " + containerDTO.getContainerCode());
            return ResponseEntity.badRequest().body(response);
        }

        ContainerDTO created = containerService.createContainer(containerDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "El contenedor ha sido creado con éxito");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getContainerById(@PathVariable Integer id) {
        Optional<ContainerDTO> containerDTO = containerService.getContainerById(id);

        return containerDTO
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Contenedor no encontrado con ID: " + id);
                    return ResponseEntity.status(404).body(response);
                });
    }

    @GetMapping
    public ResponseEntity<Object> getAllContainers() {
        List<ContainerDTO> list = containerService.getAllContainers();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", list);
        response.put("count", list.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Map<String, Object>> updateContainer(
            @PathVariable Integer id,
            @Valid @RequestBody ContainerDTO containerDTO,
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

        if (!containerService.existsContainerById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Contenedor no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        Optional<ContainerDTO> existing = containerService.getContainerById(id);
        if (existing.isPresent()
                && !existing.get().getContainerCode().equals(containerDTO.getContainerCode())
                && containerService.existsByContainerCode(containerDTO.getContainerCode())) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe otro contenedor con el código: " + containerDTO.getContainerCode());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<ContainerDTO> updated = containerService.updateContainer(id, containerDTO);

        return updated
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "El contenedor ha sido modificado con éxito");
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Error al actualizar el contenedor");
                    return ResponseEntity.status(500).body(response);
                });
    }

    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Object> deleteContainer(@PathVariable Integer id) {

        if (!containerService.existsContainerById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Contenedor no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        try {
            containerService.deleteContainerById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "El contenedor ha sido eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el contenedor: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
