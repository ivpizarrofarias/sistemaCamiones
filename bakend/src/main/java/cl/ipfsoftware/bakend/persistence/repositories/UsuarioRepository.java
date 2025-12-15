package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Usuario
    @Override
    <S extends Usuario> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Usuario
    @Override
    List<Usuario> findAll();

    // Método personalizado para buscar un Usuario por su ID
    Optional<Usuario> findByIdUsuario(Integer idUsuario);

    // Método personalizado para verificar si existe un Usuario por su correo
    boolean existsByCorreo(String correo);
Optional<Usuario> findByCorreo(String Correo);
    // Método personalizado para eliminar un Usuario por su ID
    void deleteByIdUsuario(Integer idUsuario);

    // Método personalizado para verificar si existe un Usuario por su ID
    boolean existsByIdUsuario(Integer idUsuario);

    // Método personalizado para buscar usuarios por nombre, apellido o correo
    List<Usuario> findByNombreContainingIgnoreCaseOrPaternoContainingIgnoreCaseOrMaternoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombre, String paterno, String materno, String correo);
}