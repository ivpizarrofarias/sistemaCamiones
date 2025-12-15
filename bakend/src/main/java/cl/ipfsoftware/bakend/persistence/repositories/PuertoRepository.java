package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Puerto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuertoRepository extends CrudRepository<Puerto, Integer> {
    @Override
    <S extends Puerto> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Puerto
    @Override
    List<Puerto> findAll();

    // Método personalizado para buscar un Puerto por su ID
    Optional<Puerto> findByIdPuerto(Integer idPuerto);

    // Método personalizado para buscar un Puerto por su nombre
    boolean existsByNombre(String nombre);

    // Método personalizado para eliminar un Puerto por su ID
    void deleteByIdPuerto(Integer idPuerto);

    // Método personalizado para verificar si existe un Puerto por su ID
    boolean existsByIdPuerto(Integer idPuerto);

    // Método personalizado para buscar puertos por nombre, insensible a mayúsculas y minúsculas
    List<Puerto> findByNombreContainingIgnoreCase(String nombre);
}