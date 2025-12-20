package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.ShipDTO;
import cl.ipfsoftware.bakend.service.ShipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ships")
@CrossOrigin(origins = "*")
public class ShipRestController {

    private final ShipService shipService;

    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @PostMapping
    public ResponseEntity<ShipDTO> create(@Valid @RequestBody ShipDTO shipDTO) {
        return new ResponseEntity<>(shipService.createShip(shipDTO), HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ShipDTO>> createBatch(@Valid @RequestBody List<ShipDTO> shipDTOS) {
        return new ResponseEntity<>(shipService.createShips(shipDTOS), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShipDTO>> findAll() {
        return ResponseEntity.ok(shipService.getAllShips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipDTO> findById(@PathVariable Integer id) {
        Optional<ShipDTO> ship = shipService.getShipById(id);
        return ship.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ShipDTO shipDTO
    ) throws Exception {
        Optional<ShipDTO> updated = shipService.updateShip(id, shipDTO);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shipService.deleteShipById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(shipService.existsShipById(id));
    }

    @GetMapping("/exists/name/{shipName}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String shipName) {
        return ResponseEntity.ok(shipService.existsByShipName(shipName));
    }
}
