
package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Valorizacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValorizacionRepository extends CrudRepository<Valorizacion, Integer> {

    @Override
    <S extends Valorizacion> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    List<Valorizacion> findAll();

    Optional<Valorizacion> findByIdValorizacion(Integer idValorizacion);

    boolean existsByIdValorizacion(Integer idValorizacion);

    void deleteByIdValorizacion(Integer idValorizacion);

    List<Valorizacion> findByClienteIdCliente(Integer idCliente);

    List<Valorizacion> findByNaveIdNave(Integer idNave);

    List<Valorizacion> findByViajeIdViaje(Integer idViaje);

    List<Valorizacion> findByPuertoIdPuerto(Integer idPuerto);

    List<Valorizacion> findByContenedorIdContenedor(Integer idContenedor);

    List<Valorizacion> findByTransporteTerrestreIdTransporte(Integer idTransporte);

    // Método para filtrar por cliente y rango de fechas
    List<Valorizacion> findByClienteIdClienteAndFechaBetween(Integer clientId, LocalDateTime startDate, LocalDateTime endDate);
}