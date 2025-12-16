package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.DriverDTO;
import cl.ipfsoftware.bakend.model.entities.Chofer;
import cl.ipfsoftware.bakend.model.mapper.DriverMapper;
import cl.ipfsoftware.bakend.persistence.repositories.ChoferRepository;
import cl.ipfsoftware.bakend.service.DriverService;
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
public class DriverServiceImpl implements DriverService {

    private final ChoferRepository choferRepository;
    private final DriverMapper driverMapper;
    private final Validator validator;

    public DriverServiceImpl(ChoferRepository choferRepository, DriverMapper driverMapper, Validator validator) {
        this.choferRepository = choferRepository;
        this.driverMapper = driverMapper;
        this.validator = validator;
    }

    @Override
    public DriverDTO createDriver(DriverDTO driverDTO) {
        Chofer chofer;
        try {
            Set<ConstraintViolation<DriverDTO>> violations = validator.validate(driverDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de chofer fallida: " + message);
            }

            if (choferRepository.existsByIdChofer(driverDTO.getDriverId())) {
                throw new DuplicateKeyException("El chofer ya existe: " + driverDTO.getDriverId());
            }

            if (choferRepository.existsByRut(driverDTO.getDriverRut())) {
                throw new DuplicateKeyException("El chofer con ese RUT ya existe: " + driverDTO.getDriverRut());
            }

            chofer = driverMapper.toChofer(driverDTO);
            chofer = choferRepository.save(chofer);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el chofer: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del chofer: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el chofer: " + e.getMessage());
        }

        DriverDTO resultDTO = driverMapper.toDriverDTO(chofer);
        System.out.println("Chofer DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<DriverDTO> getAllDrivers() {
        return choferRepository.findAll()
                .stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DriverDTO> updateDriver(Integer id, DriverDTO driverDTO) throws Exception {
        try {
            Set<ConstraintViolation<DriverDTO>> violations = validator.validate(driverDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de chofer fallida: " + message);
            }

            Chofer choferToUpdate = choferRepository.findByIdChofer(id)
                    .orElseThrow(() -> new EntityNotFoundException("Chofer no encontrado con id: " + id));

            if (!choferToUpdate.getRut().equals(driverDTO.getDriverRut())
                    && choferRepository.existsByRut(driverDTO.getDriverRut())) {
                throw new DuplicateKeyException("El RUT del chofer ya existe: " + driverDTO.getDriverRut());
            }

            choferToUpdate.setNombre(driverDTO.getDriverName());
            choferToUpdate.setRut(driverDTO.getDriverRut());
            choferToUpdate.setNumeroLicencia(driverDTO.getDriverLicenseNumber());

            choferRepository.save(choferToUpdate);

            DriverDTO resultDTO = driverMapper.toDriverDTO(choferToUpdate);
            System.out.println("El chofer ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el chofer: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del chofer: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el chofer: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el chofer: " + e.getMessage());
        }
    }

    @Override
    public Optional<DriverDTO> getDriverById(Integer id) {
        return Optional.ofNullable(id)
                .map(choferRepository::findByIdChofer)
                .orElseThrow(() -> new IllegalArgumentException("Chofer no encontrado"))
                .map(driverMapper::toDriverDTO);
    }

    @Override
    public void deleteDriverById(Integer id) {
        Optional<Chofer> choferOptional = choferRepository.findById(id);
        if (choferOptional.isPresent()) {
            choferRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Chofer no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsDriverById(Integer id) {
        return choferRepository.existsByIdChofer(id);
    }

    @Override
    public boolean existsByDriverRut(String driverRut) {
        return choferRepository.existsByRut(driverRut);
    }

    public List<DriverDTO> searchDriversByNameOrRut(String searchTerm) {
        return choferRepository.findByNombreContainingIgnoreCaseOrRutContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }
}