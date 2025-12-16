package cl.ipfsoftware.bakend.controller.restcontroller;

import cl.ipfsoftware.bakend.model.dto.ClientDTO;
import cl.ipfsoftware.bakend.service.ClientService;
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
@RequestMapping("/api/clientes")
public class ClientRestController {

    private final ClientService clientService;

    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping("/guardar")
    public ResponseEntity<Object> createClient(
            @Valid @RequestBody ClientDTO clientDTO,
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

        // Validar email duplicado
        if (clientService.existsByClientEmail(clientDTO.getClientEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe un cliente con el email: " + clientDTO.getClientEmail());
            return ResponseEntity.badRequest().body(response);
        }

        ClientDTO created = clientService.createClient(clientDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "El cliente ha sido creado con éxito");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    // =========================
    // READ BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getClientById(@PathVariable Integer id) {
        Optional<ClientDTO> clientDTO = clientService.getClientById(id);

        return clientDTO
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Cliente no encontrado con ID: " + id);
                    return ResponseEntity.status(404).body(response);
                });
    }

    // =========================
    // READ ALL
    // =========================
    @GetMapping
    public ResponseEntity<Object> getAllClients() {
        List<ClientDTO> list = clientService.getAllClients();
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
    public ResponseEntity<Map<String, Object>> updateClient(
            @PathVariable Integer id,
            @Valid @RequestBody ClientDTO clientDTO,
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

        // Verificar si el cliente existe
        if (!clientService.existsClientById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Cliente no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        // Validar email duplicado (excepto para el mismo cliente)
        Optional<ClientDTO> existingClient = clientService.getClientById(id);
        if (existingClient.isPresent() &&
                !existingClient.get().getClientEmail().equals(clientDTO.getClientEmail()) &&
                clientService.existsByClientEmail(clientDTO.getClientEmail())) {

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Ya existe otro cliente con el email: " + clientDTO.getClientEmail());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<ClientDTO> updated = clientService.updateClient(id, clientDTO);

        return updated
                .map(dto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "El cliente ha sido modificado con éxito");
                    response.put("data", dto);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Error al actualizar el cliente");
                    return ResponseEntity.status(500).body(response);
                });
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable Integer id) {

        if (!clientService.existsClientById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Cliente no encontrado con ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        try {
            clientService.deleteClientById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "El cliente ha sido eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar el cliente: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}