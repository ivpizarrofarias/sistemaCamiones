package cl.ipfsoftware.bakend.service;

import cl.ipfsoftware.bakend.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Método para crear un nuevo usuario a partir de un DTO.
    // Se utiliza para registrar un nuevo usuario en la base de datos.
    // Retorna el usuario creado.
    UserDTO createUser(UserDTO userDTO);

    // Devuelve una lista con todos los usuarios registrados.
    List<UserDTO> getAllUsers();

    // Actualiza un usuario existente según su ID.
    // Retorna un Optional con el usuario actualizado o vacío si no existe.
    Optional<UserDTO> updateUser(Integer id, UserDTO userDTO) throws Exception;

    // Obtiene un usuario específico por su ID.
    Optional<UserDTO> getUserById(Integer id);

    // Elimina un usuario por su ID.
    void deleteUserById(Integer id);

    // Verifica si existe un usuario por su ID.
    Boolean existsUserById(Integer id);

    // Verifica si ya existe un usuario registrado con el mismo correo.
    boolean existsByEmail(String email);

    // Busca usuarios por nombre, apellidos o correo (búsqueda general).
    List<UserDTO> searchUsers(String keyword);
}
