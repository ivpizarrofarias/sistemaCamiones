package cl.ipfsoftware.bakend.service;



import cl.ipfsoftware.bakend.model.dto.ContainerDTO;

import java.util.List;
import java.util.Optional;

public interface ContainerService {

    // Método para crear un nuevo contenedor a partir de un DTO.
    // Se utiliza para agregar un nuevo contenedor a la base de datos.
    // Toma un objeto ContainerDTO que contiene los detalles del contenedor a crear
    // y devuelve el DTO del contenedor recién creado.
    ContainerDTO createContainer(ContainerDTO containerDTO);

    // Este método devuelve una lista de todos los contenedores en la base de datos.
    // Se utiliza para obtener una vista general de todos los contenedores disponibles.
    List<ContainerDTO> getAllContainers();

    // Este método actualiza un contenedor existente con los datos proporcionados en el DTO.
    // Toma el ID del contenedor a actualizar y el DTO con los nuevos datos.
    // Devuelve un Optional que contiene el DTO del contenedor actualizado si la operación fue exitosa,
    // o un Optional vacío si no se encontró el contenedor.
    Optional<ContainerDTO> updateContainer(Integer id, ContainerDTO containerDTO) throws Exception;

    // Este método obtiene un contenedor específico por su ID.
    // Devuelve un Optional que contiene el DTO del contenedor si se encuentra,
    // o un Optional vacío si no se encuentra.
    Optional<ContainerDTO> getContainerById(Integer id);

    // Este método elimina un contenedor específico por su ID.
    // Se utiliza para remover un contenedor de la base de datos.
    void deleteContainerById(Integer id);

    // Este método verifica si existe un contenedor con un ID específico.
    // Devuelve true si el contenedor existe y false si no.
    Boolean existsContainerById(Integer id);

    // Este método verifica si ya existe un contenedor con un código específico.
    // Es útil para prevenir duplicados y se utiliza típicamente durante la creación o actualización de un contenedor.
    boolean existsByContainerCode(String containerCode);
}