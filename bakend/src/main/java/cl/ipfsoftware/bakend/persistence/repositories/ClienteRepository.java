package cl.ipfsoftware.bakend.persistence.repositories;


import cl.ipfsoftware.bakend.model.entities.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

    // Método sobrescrito para guardar múltiples entidades de tipo Cliente
    @Override
    <S extends Cliente> Iterable<S> saveAll(Iterable<S> entities);

    // Método sobrescrito para listar todas las entidades de tipo Cliente
    @Override
    List<Cliente> findAll();

    // Método personalizado para buscar un Cliente por su ID
    Optional<Cliente> findByIdCliente(Integer idCliente);

    // Método personalizado para buscar un Cliente por su email
    boolean existsByEmail(String email);

    // Método personalizado para eliminar un Cliente por su ID
    void deleteByIdCliente(Integer idCliente);

    // Método personalizado para verificar si existe un Cliente por su ID
    boolean existsByIdCliente(Integer idCliente);

    // Método personalizado para buscar clientes por nombre o email
    List<Cliente> findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombre, String email);
}