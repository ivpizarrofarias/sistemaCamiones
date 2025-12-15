package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Viaje;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends CrudRepository<Viaje, Integer> {

    @Override
    <S extends Viaje> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Viaje
    @Override
    List<Viaje> findAll();

    // Método personalizado para buscar un Viaje por su ID
    Optional<Viaje> findByIdViaje(Integer idViaje);

    // Método personalizado para verificar si existe un Viaje por su origen
    boolean existsByOrigen(String origen);

    // Método personalizado para eliminar un Viaje por su ID
    void deleteByIdViaje(Integer idViaje);

    // Método personalizado para verificar si existe un Viaje por su ID
    boolean existsByIdViaje(Integer idViaje);

    // Método personalizado para buscar viajes por origen, insensible a mayúsculas y minúsculas
    List<Viaje> findByOrigenContainingIgnoreCase(String origen);
}
