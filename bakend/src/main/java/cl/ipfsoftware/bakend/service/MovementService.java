package cl.ipfsoftware.bakend.service;


import cl.ipfsoftware.bakend.model.dto.MovementDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MovementService {

    // Crear un nuevo movimiento
    MovementDTO createMovement(MovementDTO movementDTO);

    // Obtener todos los movimientos
    List<MovementDTO> getAllMovements();

    // Actualizar un movimiento existente
    Optional<MovementDTO> updateMovement(Integer id, MovementDTO movementDTO) throws Exception;

    // Obtener un movimiento por su ID
    Optional<MovementDTO> getMovementById(Integer id);

    // Eliminar un movimiento por su ID
    void deleteMovementById(Integer id);

    // Verificar si existe un movimiento por su ID
    Boolean existsMovementById(Integer id);

    // Consultas personalizadas

    // Obtener movimientos por ID de camión
    List<MovementDTO> getMovementsByTruckId(Integer truckId);

    // Obtener movimientos por ID de chofer
    List<MovementDTO> getMovementsByDriverId(Integer driverId);

    // Obtener movimientos por ID de cliente
    List<MovementDTO> getMovementsByClientId(Integer clientId);

    // Obtener movimientos por ID de nave
    List<MovementDTO> getMovementsByShipId(Integer shipId);

    // Obtener movimientos por ID de puerto
    List<MovementDTO> getMovementsByPortId(Integer portId);

    // Obtener movimientos por ID de transporte terrestre
    List<MovementDTO> getMovementsByGroundTransportId(Integer groundTransportId);

    // Obtener movimientos por ID de contenedor
    List<MovementDTO> getMovementsByContainerId(Integer containerId);

    // Exportar movimientos a Excel
    void exportExcelMovements(HttpServletResponse response) throws Exception;

    // Exportar movimientos por cliente a Excel
    void exportExcelMovementsByDriver(HttpServletResponse response, Integer clientId) throws IOException;

    // Búsqueda de movimientos por término (número de documento o nombre del cliente)
    List<MovementDTO> searchMovements(String searchTerm);
}