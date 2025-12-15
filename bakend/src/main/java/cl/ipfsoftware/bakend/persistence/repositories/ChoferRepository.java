package cl.ipfsoftware.bakend.persistence.repositories;

import cl.ipfsoftware.bakend.model.entities.Chofer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChoferRepository extends CrudRepository<Chofer, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Chofer
    @Override
    <S extends Chofer> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Chofer
    @Override
    List<Chofer> findAll();

    // Método personalizado para buscar un Chofer por su ID
    Optional<Chofer> findByIdChofer(Integer idChofer);

    // Método personalizado para buscar un Chofer por su RUT
    boolean existsByRut(String rut);

    // Método personalizado para eliminar un Chofer por su ID
    void deleteByIdChofer(Integer idChofer);

    // Método personalizado para verificar si existe un Chofer por su ID
    boolean existsByIdChofer(Integer idChofer);

    // Método personalizado para verificar si existe un Chofer por su RUT
    boolean existsByNumeroLicencia(String numeroLicencia);

    // Método personalizado para buscar choferes por nombre o RUT
    List<Chofer> findByNombreContainingIgnoreCaseOrRutContainingIgnoreCase(String nombre, String rut);
}