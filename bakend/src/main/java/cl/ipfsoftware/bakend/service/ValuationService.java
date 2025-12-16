
package cl.ipfsoftware.bakend.service;


import cl.ipfsoftware.bakend.model.dto.ValuationDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ValuationService {

    // Crear una nueva valorización
    ValuationDTO createValuation(ValuationDTO valuationDTO);

    // Obtener todas las valorizaciones
    List<ValuationDTO> getAllValuations();

    // Actualizar una valorización existente
    Optional<ValuationDTO> updateValuation(Integer id, ValuationDTO valuationDTO) throws Exception;

    // Obtener una valorización por su ID
    Optional<ValuationDTO> getValuationById(Integer id);

    // Eliminar una valorización por su ID
    void deleteValuationById(Integer id);

    // Verificar si existe una valorización por su ID
    Boolean existsValuationById(Integer id);

    // Consultas personalizadas

    // Obtener valorizaciones por ID de cliente
    List<ValuationDTO> getValuationsByClientId(Integer clientId);

    // Obtener valorizaciones por ID de nave
    List<ValuationDTO> getValuationsByShipId(Integer shipId);

    // Obtener valorizaciones por ID de puerto
    List<ValuationDTO> getValuationsByPortId(Integer portId);

    // Obtener valorizaciones por ID de viaje
    List<ValuationDTO> getValuationsByTripId(Integer tripId);

    // Obtener valorizaciones por ID de contenedor
    List<ValuationDTO> getValuationsByContainerId(Integer containerId);

    // Obtener valorizaciones por ID de transporte terrestre
    List<ValuationDTO> getValuationsByGroundTransportId(Integer groundTransportId);

    // Exportar valorizaciones a Excel
    void exportExcelValuations(HttpServletResponse response) throws Exception;

    // Exportar valorizaciones por cliente y rango de fechas a Excel
    void exportExcelValuationsByClientAndDate(
            HttpServletResponse response,
            Integer clientId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) throws IOException;
}