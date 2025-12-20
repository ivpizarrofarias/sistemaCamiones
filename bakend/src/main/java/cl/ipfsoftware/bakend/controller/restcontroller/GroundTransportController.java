package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.GroundTransportDTO;
import cl.ipfsoftware.bakend.service.GroundTransportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ground-transports")
public class GroundTransportController {

    private final GroundTransportService groundTransportService;

    public GroundTransportController(GroundTransportService groundTransportService) {
        this.groundTransportService = groundTransportService;
    }

    @PostMapping
    public ResponseEntity<GroundTransportDTO> create(@Valid @RequestBody GroundTransportDTO groundTransportDTO) {
        return new ResponseEntity<>(
                groundTransportService.createGroundTransport(groundTransportDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<GroundTransportDTO>> findAll() {
        return ResponseEntity.ok(groundTransportService.getAllGroundTransports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroundTransportDTO> findById(@PathVariable Integer id) {
        return groundTransportService.getGroundTransportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroundTransportDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody GroundTransportDTO groundTransportDTO
    ) throws Exception {
        return groundTransportService.updateGroundTransport(id, groundTransportDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        groundTransportService.deleteGroundTransportById(id);
        return ResponseEntity.noContent().build();
    }
}
