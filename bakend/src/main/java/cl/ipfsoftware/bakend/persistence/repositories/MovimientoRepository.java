package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Movimiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends CrudRepository<Movimiento, Integer> {

    // Consulta todos los movimientos
    List<Movimiento> findAll();

    // Busca un movimiento por su ID
    Optional<Movimiento> findByIdMovimiento(Integer idMovimiento);

    // Verifica si existe un movimiento con el ID dado
    boolean existsByIdMovimiento(Integer idMovimiento);

    // Elimina un movimiento por su ID
    void deleteByIdMovimiento(Integer idMovimiento);

    // Consultas personalizadas basadas en relaciones con otras entidades
    List<Movimiento> findByCamionIdCamion(Integer idCamion);
    List<Movimiento> findByChoferIdChofer(Integer idChofer);
    List<Movimiento> findByClienteIdCliente(Integer idCliente);
    List<Movimiento> findByNaveIdNave(Integer idNave);
    List<Movimiento> findByPuertoIdPuerto(Integer idPuerto);
    List<Movimiento> findByTransporteTerrestreIdTransporte(Integer idTransporte);
    List<Movimiento> findByContenedorIdContenedor(Integer idContenedor);

    // Consultas basadas en campos de texto (ignorando mayúsculas/minúsculas)
    List<Movimiento> findByNumeroDocumentoContainingIgnoreCase(String numeroDocumento);
    List<Movimiento> findByClienteNombreContainingIgnoreCase(String nombreCliente);
    List<Movimiento> findByNaveNombreBarcoContainingIgnoreCase(String nombreBarco);

    // Consulta combinada: busca por número de documento o nombre del cliente
    List<Movimiento> findByNumeroDocumentoContainingIgnoreCaseOrClienteNombreContainingIgnoreCase(
            String numeroDocumento, String nombreCliente
    );
}