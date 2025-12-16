package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.ClientDTO;
import cl.ipfsoftware.bakend.model.entities.Cliente;
import cl.ipfsoftware.bakend.model.mapper.ClientMapper;
import cl.ipfsoftware.bakend.persistence.repositories.ClienteRepository;
import cl.ipfsoftware.bakend.service.ClientService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClienteRepository clienteRepository;
    private final ClientMapper clientMapper;
    private final Validator validator;

    public ClientServiceImpl(ClienteRepository clienteRepository, ClientMapper clientMapper, Validator validator) {
        this.clienteRepository = clienteRepository;
        this.clientMapper = clientMapper;
        this.validator = validator;
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        Cliente cliente;
        try {
            Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de cliente fallida: " + message);
            }

            if (clienteRepository.existsByIdCliente(clientDTO.getClientId())) {
                throw new DuplicateKeyException("El cliente ya existe: " + clientDTO.getClientId());
            }

            if (clienteRepository.existsByEmail(clientDTO.getClientEmail())) {
                throw new DuplicateKeyException("El cliente con ese email ya existe: " + clientDTO.getClientEmail());
            }

            cliente = clientMapper.toCliente(clientDTO);
            cliente = clienteRepository.save(cliente);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el cliente: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del cliente: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el cliente: " + e.getMessage());
        }

        ClientDTO resultDTO = clientMapper.toClientDTO(cliente);
        System.out.println("Cliente DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clienteRepository.findAll()
                .stream()
                .map(clientMapper::toClientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> updateClient(Integer id, ClientDTO clientDTO) throws Exception {
        try {
            Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de cliente fallida: " + message);
            }

            Cliente clienteToUpdate = clienteRepository.findByIdCliente(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));

            if (!clienteToUpdate.getEmail().equals(clientDTO.getClientEmail())
                    && clienteRepository.existsByEmail(clientDTO.getClientEmail())) {
                throw new DuplicateKeyException("El email del cliente ya existe: " + clientDTO.getClientEmail());
            }

            clienteToUpdate.setNombre(clientDTO.getClientName());
            clienteToUpdate.setEmail(clientDTO.getClientEmail());

            clienteRepository.save(clienteToUpdate);

            ClientDTO resultDTO = clientMapper.toClientDTO(clienteToUpdate);
            System.out.println("El cliente ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el cliente: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del cliente: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el cliente: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el cliente: " + e.getMessage());
        }
    }

    @Override
    public Optional<ClientDTO> getClientById(Integer id) {
        return Optional.ofNullable(id)
                .map(clienteRepository::findByIdCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"))
                .map(clientMapper::toClientDTO);
    }

    @Override
    public void deleteClientById(Integer id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            clienteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cliente no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsClientById(Integer id) {
        return clienteRepository.existsByIdCliente(id);
    }

    @Override
    public boolean existsByClientEmail(String clientEmail) {
        return clienteRepository.existsByEmail(clientEmail);
    }

    public List<ClientDTO> searchClientsByNameOrEmail(String searchTerm) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(clientMapper::toClientDTO)
                .collect(Collectors.toList());
    }
}