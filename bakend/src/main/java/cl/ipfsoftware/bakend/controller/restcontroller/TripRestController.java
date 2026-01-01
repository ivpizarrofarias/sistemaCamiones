package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.TripDTO;
import cl.ipfsoftware.bakend.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TripRestController {

    private final TripService tripService;

    public TripRestController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) {
        return new ResponseEntity<>(tripService.createTrip(tripDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Integer id) {
        return tripService.getTripById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> updateTrip(
            @PathVariable Integer id,
            @Valid @RequestBody TripDTO tripDTO
    ) throws Exception {
        return tripService.updateTrip(id, tripDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Integer id) {
        tripService.deleteTripById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsTripById(@PathVariable Integer id) {
        return ResponseEntity.ok(tripService.existsTripById(id));
    }

    @GetMapping("/exists/origin/{origin}")
    public ResponseEntity<Boolean> existsByTripOrigin(@PathVariable String origin) {
        return ResponseEntity.ok(tripService.existsByTripOrigin(origin));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TripDTO>> searchTrips(@RequestParam String origin) {
        return ResponseEntity.ok(
                ((cl.ipfsoftware.bakend.service.impl.TripServiceImpl) tripService)
                        .searchTripsByOrigin(origin)
        );
    }
}
