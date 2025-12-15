package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Camion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CamionRepository extends CrudRepository<Camion, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Camion
    @Override
    <S extends Camion> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Camion
    @Override
    List<Camion> findAll();

    // Método personalizado para buscar un Camion por su ID
    Optional<Camion> findByIdCamion(Integer idCamion);

    // Método personalizado para buscar un Camion por su patente
    boolean existsByPatente(String patente);

    // Método personalizado para eliminar un Camion por su ID
    void deleteByIdCamion(Integer idCamion);

    // Método personalizado para verificar si existe un Camion por su ID
    boolean existsByIdCamion(Integer idCamion);

    // Método personalizado para buscar camiones por patente, insensible a mayúsculas y minúsculas
    List<Camion> findByPatenteContainingIgnoreCase(String patente);
}