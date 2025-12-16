package cl.ipfsoftware.bakend.service.impl;

import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.TripDTO;
import cl.ipfsoftware.bakend.model.entities.Viaje;
import cl.ipfsoftware.bakend.model.mapper.TripMapper;
import cl.ipfsoftware.bakend.persistence.repositories.ViajeRepository;
import cl.ipfsoftware.bakend.service.TripService;
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
public class TripServiceImpl implements TripService {

    private final ViajeRepository viajeRepository;
    private final TripMapper tripMapper;
    private final Validator validator;

    public TripServiceImpl(ViajeRepository viajeRepository, TripMapper tripMapper, Validator validator) {
        this.viajeRepository = viajeRepository;
        this.tripMapper = tripMapper;
        this.validator = validator;
    }

    @Override
    public TripDTO createTrip(TripDTO tripDTO) {
        Viaje viaje;
        try {
            Set<ConstraintViolation<TripDTO>> violations = validator.validate(tripDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de viaje fallida: " + message);
            }

            if (viajeRepository.existsByIdViaje(tripDTO.getTripId())) {
                throw new DuplicateKeyException("El viaje ya existe: " + tripDTO.getTripId());
            }

            if (viajeRepository.existsByOrigen(tripDTO.getOrigin())) {
                throw new DuplicateKeyException("El viaje con ese origen ya existe: " + tripDTO.getOrigin());
            }

            viaje = tripMapper.toViaje(tripDTO);
            viaje = viajeRepository.save(viaje);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el viaje: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del viaje: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el viaje: " + e.getMessage());
        }

        TripDTO resultDTO = tripMapper.toTripDTO(viaje);
        System.out.println("Viaje DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<TripDTO> getAllTrips() {
        return viajeRepository.findAll()
                .stream()
                .map(tripMapper::toTripDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TripDTO> updateTrip(Integer id, TripDTO tripDTO) throws Exception {
        try {
            Set<ConstraintViolation<TripDTO>> violations = validator.validate(tripDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de viaje fallida: " + message);
            }

            Viaje viajeToUpdate = viajeRepository.findByIdViaje(id)
                    .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id: " + id));

            if (!viajeToUpdate.getOrigen().equals(tripDTO.getOrigin())
                    && viajeRepository.existsByOrigen(tripDTO.getOrigin())) {
                throw new DuplicateKeyException("El origen del viaje ya existe: " + tripDTO.getOrigin());
            }

            viajeToUpdate.setOrigen(tripDTO.getOrigin());

            viajeRepository.save(viajeToUpdate);

            TripDTO resultDTO = tripMapper.toTripDTO(viajeToUpdate);
            System.out.println("El viaje ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el viaje: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del viaje: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el viaje: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el viaje: " + e.getMessage());
        }
    }

    @Override
    public Optional<TripDTO> getTripById(Integer id) {
        return Optional.ofNullable(id)
                .map(viajeRepository::findByIdViaje)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"))
                .map(tripMapper::toTripDTO);
    }

    @Override
    public void deleteTripById(Integer id) {
        Optional<Viaje> viajeOptional = viajeRepository.findById(id);
        if (viajeOptional.isPresent()) {
            viajeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Viaje no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsTripById(Integer id) {
        return viajeRepository.existsByIdViaje(id);
    }

    @Override
    public boolean existsByTripOrigin(String origin) {
        return viajeRepository.existsByOrigen(origin);
    }

    public List<TripDTO> searchTripsByOrigin(String searchTerm) {
        return viajeRepository.findByOrigenContainingIgnoreCase(searchTerm)
                .stream()
                .map(tripMapper::toTripDTO)
                .collect(Collectors.toList());
    }
}