package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.TruckDTO;
import cl.ipfsoftware.bakend.model.entities.Camion;
import cl.ipfsoftware.bakend.model.mapper.TruckMapper;
import cl.ipfsoftware.bakend.persistence.repositories.CamionRepository;
import cl.ipfsoftware.bakend.service.TruckService;
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
public class TruckServiceImpl implements TruckService {
    private final CamionRepository camionRepository;
    private final TruckMapper truckMapper;
    private final Validator validator;

    public TruckServiceImpl(CamionRepository camionRepository, TruckMapper truckMapper, Validator validator) {
        this.camionRepository = camionRepository;
        this.truckMapper = truckMapper;
        this.validator = validator;
    }

    @Override
    public TruckDTO createTruck(TruckDTO truckDTO) {
        Camion camion;
        try {
            Set<ConstraintViolation<TruckDTO>> violations = validator.validate(truckDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de camión fallida: " + message);
            }

            if (camionRepository.existsByIdCamion(truckDTO.getTruckId())) {
                throw new DuplicateKeyException("El camión ya existe: " + truckDTO.getTruckId());
            }

            if (camionRepository.existsByPatente(truckDTO.getLicensePlate())) {
                throw new DuplicateKeyException("El camión con esa patente ya existe: " + truckDTO.getLicensePlate());
            }

            camion = truckMapper.toCamion(truckDTO);
            camion = camionRepository.save(camion);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el camión: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del camión: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el camión: " + e.getMessage());
        }

        TruckDTO resultDTO = truckMapper.toTruckDTO(camion);
        System.out.println("Camión DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<TruckDTO> getAllTrucks() {
        return camionRepository.findAll()
                .stream()
                .map(truckMapper::toTruckDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TruckDTO> updateTruck(Integer id, TruckDTO truckDTO) throws Exception {
        try {
            Set<ConstraintViolation<TruckDTO>> violations = validator.validate(truckDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de camión fallida: " + message);
            }

            Camion camionToUpdate = camionRepository.findByIdCamion(id)
                    .orElseThrow(() -> new EntityNotFoundException("Camión no encontrado con id: " + id));

            if (!camionToUpdate.getPatente().equals(truckDTO.getLicensePlate())
                    && camionRepository.existsByPatente(truckDTO.getLicensePlate())) {
                throw new DuplicateKeyException("La patente del camión ya existe: " + truckDTO.getLicensePlate());
            }

            camionToUpdate.setPatente(truckDTO.getLicensePlate());

            camionRepository.save(camionToUpdate);

            TruckDTO resultDTO = truckMapper.toTruckDTO(camionToUpdate);
            System.out.println("El camión ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el camión: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del camión: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el camión: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el camión: " + e.getMessage());
        }
    }

    @Override
    public Optional<TruckDTO> getTruckById(Integer id) {
        return Optional.ofNullable(id)
                .map(camionRepository::findByIdCamion)
                .orElseThrow(() -> new IllegalArgumentException("Camión no encontrado"))
                .map(truckMapper::toTruckDTO);
    }

    @Override
    public void deleteTruckById(Integer id) {
        Optional<Camion> camionOptional = camionRepository.findById(id);
        if (camionOptional.isPresent()) {
            camionRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Camión no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsTruckById(Integer id) {
        return camionRepository.existsByIdCamion(id);
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        return camionRepository.existsByPatente(licensePlate);
    }

    public List<TruckDTO> searchTrucksByLicensePlate(String searchTerm) {
        return camionRepository.findByPatenteContainingIgnoreCase(searchTerm)
                .stream()
                .map(truckMapper::toTruckDTO)
                .collect(Collectors.toList());
    }
}