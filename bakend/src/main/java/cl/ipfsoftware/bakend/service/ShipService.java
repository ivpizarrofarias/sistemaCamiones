package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.ShipDTO;

import java.util.List;
import java.util.Optional;

    public interface ShipService {

        // Método para crear una nueva nave a partir de un DTO.
        // Se utiliza para agregar una nueva nave a la base de datos.
        // Toma un objeto ShipDTO que contiene los detalles de la nave a crear
        // y devuelve el DTO de la nave recién creada.
        ShipDTO createShip(ShipDTO shipDTO);

        // Este método devuelve una lista de todas las naves en la base de datos.
        // Se utiliza para obtener una vista general de todas las naves disponibles.
        List<ShipDTO> getAllShips();

        // Este método actualiza una nave existente con los datos proporcionados en el DTO.
        // Toma el ID de la nave a actualizar y el DTO con los nuevos datos.
        // Devuelve un Optional que contiene el DTO de la nave actualizada si la operación fue exitosa,
        // o un Optional vacío si no se encontró la nave.
        Optional<ShipDTO> updateShip(Integer id, ShipDTO shipDTO) throws Exception;

        // Este método obtiene una nave específica por su ID.
        // Devuelve un Optional que contiene el DTO de la nave si se encuentra,
        // o un Optional vacío si no se encuentra.
        Optional<ShipDTO> getShipById(Integer id);

        // Este método elimina una nave específica por su ID.
        // Se utiliza para remover una nave de la base de datos.
        void deleteShipById(Integer id);

        // Este método verifica si existe una nave con un ID específico.
        // Devuelve true si la nave existe y false si no.
        Boolean existsShipById(Integer id);

        // Este método verifica si ya existe una nave con un nombre de barco específico.
        // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de una nave.
        boolean existsByShipName(String shipName);

        // Este método permite la creación de múltiples naves a la vez.
        // Toma una lista de DTOs de naves y devuelve una lista de DTOs de las naves creadas.
        // Es útil para operaciones de inserción en masa.
        List<ShipDTO> createShips(List<ShipDTO> shipDTOS);
    }
