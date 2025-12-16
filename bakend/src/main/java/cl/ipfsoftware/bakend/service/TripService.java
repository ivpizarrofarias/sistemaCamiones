package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.TripDTO;

import java.util.List;
import java.util.Optional;

public interface TripService {

    // Método para crear un nuevo viaje a partir de un DTO.
    // Se utiliza para agregar un nuevo viaje a la base de datos.
    // Toma un objeto TripDTO que contiene los detalles del viaje a crear
    // y devuelve el DTO del viaje recién creado.
    TripDTO createTrip(TripDTO tripDTO);

    // Este método devuelve una lista de todos los viajes en la base de datos.
    // Se utiliza para obtener una vista general de todos los viajes disponibles.
    List<TripDTO> getAllTrips();

    // Este método actualiza un viaje existente con los datos proporcionados en el DTO.
    // Toma el ID del viaje a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del viaje actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el viaje.
    Optional<TripDTO> updateTrip(Integer id, TripDTO tripDTO) throws Exception;

    // Este método obtiene un viaje específico por su ID.
    // Devuelve un Optional que contiene el DTO del viaje si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<TripDTO> getTripById(Integer id);

    // Este método elimina un viaje específico por su ID.
    // Se utiliza para remover un viaje de la base de datos.
    void deleteTripById(Integer id);

    // Este método verifica si existe un viaje con un ID específico.
    // Devuelve true si el viaje existe y false si no.
    Boolean existsTripById(Integer id);

    // Este método verifica si ya existe un viaje con un origen específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un viaje.
    boolean existsByTripOrigin(String origin);
}
