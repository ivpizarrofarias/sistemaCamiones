package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.PortDTO;
import cl.ipfsoftware.bakend.model.entities.Puerto;
import cl.ipfsoftware.bakend.model.mapper.PortMapper;
import cl.ipfsoftware.bakend.persistence.repositories.PuertoRepository;
import cl.ipfsoftware.bakend.service.PortService;
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
public class PortServiceImpl implements PortService {

    private final PuertoRepository puertoRepository;
    private final PortMapper portMapper;
    private final Validator validator;

    public PortServiceImpl(PuertoRepository puertoRepository, PortMapper portMapper, Validator validator) {
        this.puertoRepository = puertoRepository;
        this.portMapper = portMapper;
        this.validator = validator;
    }

    @Override
    public PortDTO createPort(PortDTO portDTO) {
        Puerto puerto;
        try {
            Set<ConstraintViolation<PortDTO>> violations = validator.validate(portDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de puerto fallida: " + message);
            }

            if (puertoRepository.existsByIdPuerto(portDTO.getPortId())) {
                throw new DuplicateKeyException("El puerto ya existe: " + portDTO.getPortId());
            }

            if (puertoRepository.existsByNombre(portDTO.getPortName())) {
                throw new DuplicateKeyException("El puerto con ese nombre ya existe: " + portDTO.getPortName());
            }

            puerto = portMapper.toPuerto(portDTO);
            puerto = puertoRepository.save(puerto);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el puerto: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del puerto: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el puerto: " + e.getMessage());
        }

        PortDTO resultDTO = portMapper.toPortDTO(puerto);
        System.out.println("Puerto DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<PortDTO> getAllPorts() {
        return puertoRepository.findAll()
                .stream()
                .map(portMapper::toPortDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PortDTO> updatePort(Integer id, PortDTO portDTO) throws Exception {
        try {
            Set<ConstraintViolation<PortDTO>> violations = validator.validate(portDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de puerto fallida: " + message);
            }

            Puerto puertoToUpdate = puertoRepository.findByIdPuerto(id)
                    .orElseThrow(() -> new EntityNotFoundException("Puerto no encontrado con id: " + id));

            if (!puertoToUpdate.getNombre().equals(portDTO.getPortName())
                    && puertoRepository.existsByNombre(portDTO.getPortName())) {
                throw new DuplicateKeyException("El nombre del puerto ya existe: " + portDTO.getPortName());
            }

            puertoToUpdate.setNombre(portDTO.getPortName());

            puertoRepository.save(puertoToUpdate);

            PortDTO resultDTO = portMapper.toPortDTO(puertoToUpdate);
            System.out.println("El puerto ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el puerto: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del puerto: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el puerto: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el puerto: " + e.getMessage());
        }
    }

    @Override
    public Optional<PortDTO> getPortById(Integer id) {
        return Optional.ofNullable(id)
                .map(puertoRepository::findByIdPuerto)
                .orElseThrow(() -> new IllegalArgumentException("Puerto no encontrado"))
                .map(portMapper::toPortDTO);
    }

    @Override
    public void deletePortById(Integer id) {
        Optional<Puerto> puertoOptional = puertoRepository.findById(id);
        if (puertoOptional.isPresent()) {
            puertoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Puerto no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsPortById(Integer id) {
        return puertoRepository.existsByIdPuerto(id);
    }

    @Override
    public boolean existsByPortName(String portName) {
        return puertoRepository.existsByNombre(portName);
    }

    public List<PortDTO> searchPortsByName(String searchTerm) {
        return puertoRepository.findByNombreContainingIgnoreCase(searchTerm)
                .stream()
                .map(portMapper::toPortDTO)
                .collect(Collectors.toList());
    }
}