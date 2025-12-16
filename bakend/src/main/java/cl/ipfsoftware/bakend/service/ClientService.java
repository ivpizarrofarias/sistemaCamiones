package cl.ipfsoftware.bakend.service;


import cl.ipfsoftware.bakend.model.dto.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    // Método para crear un nuevo cliente a partir de un DTO.
    // Se utiliza para agregar un nuevo cliente a la base de datos.
    // Toma un objeto ClientDTO que contiene los detalles del cliente a crear
    // y devuelve el DTO del cliente recién creado.
    ClientDTO createClient(ClientDTO clientDTO);

    // Este método devuelve una lista de todos los clientes en la base de datos.
    // Se utiliza para obtener una vista general de todos los clientes disponibles.
    List<ClientDTO> getAllClients();

    // Este método actualiza un cliente existente con los datos proporcionados en el DTO.
    // Toma el ID del cliente a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del cliente actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el cliente.
    Optional<ClientDTO> updateClient(Integer id, ClientDTO clientDTO) throws Exception;

    // Este método obtiene un cliente específico por su ID.
    // Devuelve un Optional que contiene el DTO del cliente si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<ClientDTO> getClientById(Integer id);

    // Este método elimina un cliente específico por su ID.
    // Se utiliza para remover un cliente de la base de datos.
    void deleteClientById(Integer id);

    // Este método verifica si existe un cliente con un ID específico.
    // Devuelve true si el cliente existe y false si no.
    Boolean existsClientById(Integer id);

    // Este método verifica si ya existe un cliente con un email específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un cliente.
    boolean existsByClientEmail(String clientEmail);
}