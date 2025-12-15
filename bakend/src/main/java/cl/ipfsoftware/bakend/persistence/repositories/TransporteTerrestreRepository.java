package cl.ipfsoftware.bakend.persistence.repositories;

import cl.ipfsoftware.bakend.model.entities.TransporteTerrestre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransporteTerrestreRepository extends CrudRepository<TransporteTerrestre, Integer> {
    // Método sobrescrito para guardar múltiples entidades de tipo TransporteTerrestre
    @Override
    <S extends TransporteTerrestre> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo TransporteTerrestre
    @Override
    List<TransporteTerrestre> findAll();

    // Método personalizado para buscar un TransporteTerrestre por su ID
    Optional<TransporteTerrestre> findByIdTransporte(Integer idTransporte);

    // Método personalizado para buscar un TransporteTerrestre por el nombre del transportista
    boolean existsByNombreTransportista(String nombreTransportista);

    // Método personalizado para eliminar un TransporteTerrestre por su ID
    void deleteByIdTransporte(Integer idTransporte);

    // Método personalizado para verificar si existe un TransporteTerrestre por su ID
    boolean existsByIdTransporte(Integer idTransporte);

    // Método personalizado para buscar transportes terrestres por nombre del transportista, insensible a mayúsculas y minúsculas
    List<TransporteTerrestre> findByNombreTransportistaContainingIgnoreCase(String nombreTransportista);
}