package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.MovementDTO;
import cl.ipfsoftware.bakend.service.MovementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movements")
@CrossOrigin(origins = "*")
public class MovementRestController {

    private final MovementService movementService;

    public MovementRestController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<MovementDTO> create(@Valid @RequestBody MovementDTO movementDTO) {
        return new ResponseEntity<>(movementService.createMovement(movementDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovementDTO>> findAll() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> findById(@PathVariable Integer id) {
        Optional<MovementDTO> movement = movementService.getMovementById(id);
        return movement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovementDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody MovementDTO movementDTO
    ) throws Exception {
        Optional<MovementDTO> updated = movementService.updateMovement(id, movementDTO);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        movementService.deleteMovementById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer id) {
        return ResponseEntity.ok(movementService.existsMovementById(id));
    }

    @GetMapping("/truck/{truckId}")
    public ResponseEntity<List<MovementDTO>> findByTruck(@PathVariable Integer truckId) {
        return ResponseEntity.ok(movementService.getMovementsByTruckId(truckId));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<MovementDTO>> findByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(movementService.getMovementsByDriverId(driverId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<MovementDTO>> findByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(movementService.getMovementsByClientId(clientId));
    }

    @GetMapping("/ship/{shipId}")
    public ResponseEntity<List<MovementDTO>> findByShip(@PathVariable Integer shipId) {
        return ResponseEntity.ok(movementService.getMovementsByShipId(shipId));
    }

    @GetMapping("/port/{portId}")
    public ResponseEntity<List<MovementDTO>> findByPort(@PathVariable Integer portId) {
        return ResponseEntity.ok(movementService.getMovementsByPortId(portId));
    }

    @GetMapping("/ground-transport/{transportId}")
    public ResponseEntity<List<MovementDTO>> findByGroundTransport(@PathVariable Integer transportId) {
        return ResponseEntity.ok(movementService.getMovementsByGroundTransportId(transportId));
    }

    @GetMapping("/container/{containerId}")
    public ResponseEntity<List<MovementDTO>> findByContainer(@PathVariable Integer containerId) {
        return ResponseEntity.ok(movementService.getMovementsByContainerId(containerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovementDTO>> search(@RequestParam String term) {
        return ResponseEntity.ok(movementService.searchMovements(term));
    }

    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        movementService.exportExcelMovements(response);
    }

    @GetMapping("/export/excel/driver/{driverId}")
    public void exportExcelByDriver(
            HttpServletResponse response,
            @PathVariable Integer driverId
    ) throws IOException {
        movementService.exportExcelMovementsByDriver(response, driverId);
    }
}
