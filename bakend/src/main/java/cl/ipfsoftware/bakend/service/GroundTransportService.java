package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.GroundTransportDTO;

import java.util.List;
import java.util.Optional;

public interface GroundTransportService {

    // Método para crear un nuevo transporte terrestre a partir de un DTO.
    // Se utiliza para agregar un nuevo transporte terrestre a la base de datos.
    // Toma un objeto GroundTransportDTO que contiene los detalles del transporte terrestre a crear
    // y devuelve el DTO del transporte terrestre recién creado.
    GroundTransportDTO createGroundTransport(GroundTransportDTO groundTransportDTO);

    // Este método devuelve una lista de todos los transportes terrestres en la base de datos.
    // Se utiliza para obtener una vista general de todos los transportes terrestres disponibles.
    List<GroundTransportDTO> getAllGroundTransports();

    // Este método actualiza un transporte terrestre existente con los datos proporcionados en el DTO.
    // Toma el ID del transporte terrestre a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del transporte terrestre actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el transporte terrestre.
    Optional<GroundTransportDTO> updateGroundTransport(Integer id, GroundTransportDTO groundTransportDTO) throws Exception;

    // Este método obtiene un transporte terrestre específico por su ID.
    // Devuelve un Optional que contiene el DTO del transporte terrestre si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<GroundTransportDTO> getGroundTransportById(Integer id);

    // Este método elimina un transporte terrestre específico por su ID.
    // Se utiliza para remover un transporte terrestre de la base de datos.
    void deleteGroundTransportById(Integer id);

    // Este método verifica si existe un transporte terrestre con un ID específico.
    // Devuelve true si el transporte terrestre existe y false si no.
    Boolean existsGroundTransportById(Integer id);

    // Este método verifica si ya existe un transporte terrestre con un nombre de transportista específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un transporte terrestre.
    boolean existsByTransporterName(String transporterName);
}