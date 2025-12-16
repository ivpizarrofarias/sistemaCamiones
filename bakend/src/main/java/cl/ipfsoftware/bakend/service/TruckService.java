package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.TruckDTO;

import java.util.List;
import java.util.Optional;

public interface TruckService {

    // Método para crear un nuevo camión a partir de un DTO.
    // Se utiliza para agregar un nuevo camión a la base de datos.
    // Toma un objeto TruckDTO que contiene los detalles del camión a crear
    // y devuelve el DTO del camión recién creado.
    TruckDTO createTruck(TruckDTO truckDTO);

    // Este método devuelve una lista de todos los camiones en la base de datos.
    // Se utiliza para obtener una vista general de todos los camiones disponibles.
    List<TruckDTO> getAllTrucks();

    // Este método actualiza un camión existente con los datos proporcionados en el DTO.
    // Toma el ID del camión a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del camión actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el camión.
    Optional<TruckDTO> updateTruck(Integer id, TruckDTO truckDTO) throws Exception;

    // Este método obtiene un camión específico por su ID.
    // Devuelve un Optional que contiene el DTO del camión si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<TruckDTO> getTruckById(Integer id);

    // Este método elimina un camión específico por su ID.
    // Se utiliza para remover un camión de la base de datos.
    void deleteTruckById(Integer id);

    // Este método verifica si existe un camión con un ID específico.
    // Devuelve true si el camión existe y false si no.
    Boolean existsTruckById(Integer id);

    // Este método verifica si ya existe un camión con una patente específica.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un camión.
    boolean existsByLicensePlate(String licensePlate);
}