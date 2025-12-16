package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.PortDTO;

import java.util.List;
import java.util.Optional;

public interface PortService {

    // Método para crear un nuevo puerto a partir de un DTO.
    // Se utiliza para agregar un nuevo puerto a la base de datos.
    // Toma un objeto PortDTO que contiene los detalles del puerto a crear
    // y devuelve el DTO del puerto recién creado.
    PortDTO createPort(PortDTO portDTO);

    // Este método devuelve una lista de todos los puertos en la base de datos.
    // Se utiliza para obtener una vista general de todos los puertos disponibles.
    List<PortDTO> getAllPorts();

    // Este método actualiza un puerto existente con los datos proporcionados en el DTO.
    // Toma el ID del puerto a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del puerto actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el puerto.
    Optional<PortDTO> updatePort(Integer id, PortDTO portDTO) throws Exception;

    // Este método obtiene un puerto específico por su ID.
    // Devuelve un Optional que contiene el DTO del puerto si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<PortDTO> getPortById(Integer id);

    // Este método elimina un puerto específico por su ID.
    // Se utiliza para remover un puerto de la base de datos.
    void deletePortById(Integer id);

    // Este método verifica si existe un puerto con un ID específico.
    // Devuelve true si el puerto existe y false si no.
    Boolean existsPortById(Integer id);

    // Este método verifica si ya existe un puerto con un nombre específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un puerto.
    boolean existsByPortName(String portName);


}