package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.PortDTO;
import cl.ipfsoftware.bakend.service.PortService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ports")
@CrossOrigin(origins = "*")
public class PortRestController {

    private final PortService portService;

    public PortRestController(PortService portService) {
        this.portService = portService;
    }

    @PostMapping
    public ResponseEntity<PortDTO> create(@Valid @RequestBody PortDTO portDTO) {
        return new ResponseEntity<>(portService.createPort(portDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PortDTO>> findAll() {
        return ResponseEntity.ok(portService.getAllPorts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortDTO> findById(@PathVariable Integer id) {
        Optional<PortDTO> port = portService.getPortById(id);
        return port.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody PortDTO portDTO
    ) throws Exception {
        Optional<PortDTO> updated = portService.updatePort(id, portDTO);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portService.deletePortById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(portService.existsPortById(id));
    }

    @GetMapping("/exists/name/{portName}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String portName) {
        return ResponseEntity.ok(portService.existsByPortName(portName));
    }
}
