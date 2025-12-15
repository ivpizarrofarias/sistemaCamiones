package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Nave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NaveRepository extends CrudRepository<Nave, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Nave
    @Override
    <S extends Nave> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Nave
    @Override
    List<Nave> findAll();

    // Método personalizado para buscar una Nave por su ID
    Optional<Nave> findByIdNave(Integer idNave);

    // Método personalizado para buscar una Nave por su nombre de barco
    boolean existsByNombreBarco(String nombreBarco);

    // Método personalizado para eliminar una Nave por su ID
    void deleteByIdNave(Integer idNave);

    // Método personalizado para verificar si existe una Nave por su ID
    boolean existsByIdNave(Integer idNave);

    // Método personalizado para buscar naves por nombre de la nave, número de viaje o línea naviera, insensible a mayúsculas y minúsculas
    List<Nave> findByNombreBarcoContainingIgnoreCaseOrNumeroViajeContainingIgnoreCaseOrNavieraContainingIgnoreCase(String nombreBarco, String numeroViaje, String naviera);
}