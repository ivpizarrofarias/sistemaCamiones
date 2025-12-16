package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.DriverDTO;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    // Método para crear un nuevo chofer a partir de un DTO.
    // Se utiliza para agregar un nuevo chofer a la base de datos.
    // Toma un objeto DriverDTO que contiene los detalles del chofer a crear
    // y devuelve el DTO del chofer recién creado.
    DriverDTO createDriver(DriverDTO driverDTO);

    // Este método devuelve una lista de todos los choferes en la base de datos.
    // Se utiliza para obtener una vista general de todos los choferes disponibles.
    List<DriverDTO> getAllDrivers();

    // Este método actualiza un chofer existente con los datos proporcionados en el DTO.
    // Toma el ID del chofer a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del chofer actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el chofer.
    Optional<DriverDTO> updateDriver(Integer id, DriverDTO driverDTO) throws Exception;

    // Este método obtiene un chofer específico por su ID.
    // Devuelve un Optional que contiene el DTO del chofer si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<DriverDTO> getDriverById(Integer id);

    // Este método elimina un chofer específico por su ID.
    // Se utiliza para remover un chofer de la base de datos.
    void deleteDriverById(Integer id);

    // Este método verifica si existe un chofer con un ID específico.
    // Devuelve true si el chofer existe y false si no.
    Boolean existsDriverById(Integer id);

    // Este método verifica si ya existe un chofer con un RUT específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un chofer.
    boolean existsByDriverRut(String driverRut);
}