package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.ValuationDTO;
import cl.ipfsoftware.bakend.service.ValuationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/valuations")
public class ValuationRestController {

    private final ValuationService valuationService;

    public ValuationRestController(ValuationService valuationService) {
        this.valuationService = valuationService;
    }

    @PostMapping
    public ResponseEntity<ValuationDTO> createValuation(@Valid @RequestBody ValuationDTO valuationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valuationService.createValuation(valuationDTO));
    }

    @GetMapping
    public ResponseEntity<List<ValuationDTO>> getAllValuations() {
        return ResponseEntity.ok(valuationService.getAllValuations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValuationDTO> getValuationById(@PathVariable Integer id) {
        Optional<ValuationDTO> valuation = valuationService.getValuationById(id);
        return valuation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ValuationDTO> updateValuation(
            @PathVariable Integer id,
            @Valid @RequestBody ValuationDTO valuationDTO
    ) throws Exception {
        Optional<ValuationDTO> updated = valuationService.updateValuation(id, valuationDTO);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValuation(@PathVariable Integer id) {
        valuationService.deleteValuationById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ValuationDTO>> getByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(valuationService.getValuationsByClientId(clientId));
    }

    @GetMapping("/ship/{shipId}")
    public ResponseEntity<List<ValuationDTO>> getByShip(@PathVariable Integer shipId) {
        return ResponseEntity.ok(valuationService.getValuationsByShipId(shipId));
    }

    @GetMapping("/port/{portId}")
    public ResponseEntity<List<ValuationDTO>> getByPort(@PathVariable Integer portId) {
        return ResponseEntity.ok(valuationService.getValuationsByPortId(portId));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ValuationDTO>> getByTrip(@PathVariable Integer tripId) {
        return ResponseEntity.ok(valuationService.getValuationsByTripId(tripId));
    }

    @GetMapping("/container/{containerId}")
    public ResponseEntity<List<ValuationDTO>> getByContainer(@PathVariable Integer containerId) {
        return ResponseEntity.ok(valuationService.getValuationsByContainerId(containerId));
    }

    @GetMapping("/ground-transport/{transportId}")
    public ResponseEntity<List<ValuationDTO>> getByGroundTransport(@PathVariable Integer transportId) {
        return ResponseEntity.ok(valuationService.getValuationsByGroundTransportId(transportId));
    }

    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        valuationService.exportExcelValuations(response);
    }

    @GetMapping("/export/excel/client")
    public void exportExcelByClientAndDate(
            HttpServletResponse response,
            @RequestParam Integer clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) throws Exception {
        valuationService.exportExcelValuationsByClientAndDate(response, clientId, startDate, endDate);
    }
}
