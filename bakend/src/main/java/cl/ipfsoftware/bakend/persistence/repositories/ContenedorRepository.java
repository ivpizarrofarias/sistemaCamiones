package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Contenedor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContenedorRepository extends CrudRepository<Contenedor, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Contenedor
    @Override
    <S extends Contenedor> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Contenedor
    @Override
    List<Contenedor> findAll();

    // Método personalizado para buscar un Contenedor por su ID
    Optional<Contenedor> findByIdContenedor(Integer idContenedor);

    // Método personalizado para verificar si existe un Contenedor por su código
    boolean existsByCodigoContenedor(String codigoContenedor);

    // Método personalizado para eliminar un Contenedor por su ID
    void deleteByIdContenedor(Integer idContenedor);

    // Método personalizado para verificar si existe un Contenedor por su ID
    boolean existsByIdContenedor(Integer idContenedor);

    // Método personalizado para buscar contenedores por su código (ignorando mayúsculas y minúsculas)
    List<Contenedor> findByCodigoContenedorContainingIgnoreCase(String codigoContenedor);
}