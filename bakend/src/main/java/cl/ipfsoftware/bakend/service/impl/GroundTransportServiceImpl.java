package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.GroundTransportDTO;
import cl.ipfsoftware.bakend.model.entities.TransporteTerrestre;
import cl.ipfsoftware.bakend.model.mapper.GroundTransportMapper;
import cl.ipfsoftware.bakend.persistence.repositories.TransporteTerrestreRepository;
import cl.ipfsoftware.bakend.service.GroundTransportService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroundTransportServiceImpl implements GroundTransportService {
    private final TransporteTerrestreRepository transporteTerrestreRepository;
    private final GroundTransportMapper groundTransportMapper;
    private final Validator validator;

    @Autowired
    public GroundTransportServiceImpl(TransporteTerrestreRepository transporteTerrestreRepository, GroundTransportMapper groundTransportMapper, Validator validator) {
        this.transporteTerrestreRepository = transporteTerrestreRepository;
        this.groundTransportMapper = groundTransportMapper;
        this.validator = validator;
    }

    @Override
    public GroundTransportDTO createGroundTransport(GroundTransportDTO groundTransportDTO) {
        TransporteTerrestre transporteTerrestre;
        try {
            Set<ConstraintViolation<GroundTransportDTO>> violations = validator.validate(groundTransportDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de transporte terrestre fallida: " + message);
            }

            if (transporteTerrestreRepository.existsByIdTransporte(groundTransportDTO.getTransportId())) {
                throw new DuplicateKeyException("El transporte terrestre ya existe: " + groundTransportDTO.getTransportId());
            }

            if (transporteTerrestreRepository.existsByNombreTransportista(groundTransportDTO.getTransporterName())) {
                throw new DuplicateKeyException("El transporte terrestre con ese nombre de transportista ya existe: " + groundTransportDTO.getTransporterName());
            }

            transporteTerrestre = groundTransportMapper.toTransporteTerrestre(groundTransportDTO);
            transporteTerrestre = transporteTerrestreRepository.save(transporteTerrestre);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el transporte terrestre: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del transporte terrestre: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el transporte terrestre: " + e.getMessage());
        }

        GroundTransportDTO resultDTO = groundTransportMapper.toGroundTransportDTO(transporteTerrestre);
        System.out.println("Transporte terrestre DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<GroundTransportDTO> getAllGroundTransports() {
        return transporteTerrestreRepository.findAll()
                .stream()
                .map(groundTransportMapper::toGroundTransportDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroundTransportDTO> updateGroundTransport(Integer id, GroundTransportDTO groundTransportDTO) throws Exception {
        try {
            Set<ConstraintViolation<GroundTransportDTO>> violations = validator.validate(groundTransportDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de transporte terrestre fallida: " + message);
            }

            TransporteTerrestre transporteTerrestreToUpdate = transporteTerrestreRepository.findByIdTransporte(id)
                    .orElseThrow(() -> new EntityNotFoundException("Transporte terrestre no encontrado con id: " + id));

            if (!transporteTerrestreToUpdate.getNombreTransportista().equals(groundTransportDTO.getTransporterName())
                    && transporteTerrestreRepository.existsByNombreTransportista(groundTransportDTO.getTransporterName())) {
                throw new DuplicateKeyException("El nombre de transportista del transporte terrestre ya existe: " + groundTransportDTO.getTransporterName());
            }

            transporteTerrestreToUpdate.setNombreTransportista(groundTransportDTO.getTransporterName());

            transporteTerrestreRepository.save(transporteTerrestreToUpdate);

            GroundTransportDTO resultDTO = groundTransportMapper.toGroundTransportDTO(transporteTerrestreToUpdate);
            System.out.println("El transporte terrestre ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el transporte terrestre: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del transporte terrestre: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el transporte terrestre: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el transporte terrestre: " + e.getMessage());
        }
    }

    @Override
    public Optional<GroundTransportDTO> getGroundTransportById(Integer id) {
        return Optional.ofNullable(id)
                .map(transporteTerrestreRepository::findByIdTransporte)
                .orElseThrow(() -> new IllegalArgumentException("Transporte terrestre no encontrado"))
                .map(groundTransportMapper::toGroundTransportDTO);
    }

    @Override
    public void deleteGroundTransportById(Integer id) {
        Optional<TransporteTerrestre> transporteTerrestreOptional = transporteTerrestreRepository.findById(id);
        if (transporteTerrestreOptional.isPresent()) {
            transporteTerrestreRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Transporte terrestre no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsGroundTransportById(Integer id) {
        return transporteTerrestreRepository.existsByIdTransporte(id);
    }

    @Override
    public boolean existsByTransporterName(String transporterName) {
        return transporteTerrestreRepository.existsByNombreTransportista(transporterName);
    }

    public List<GroundTransportDTO> searchGroundTransportsByName(String searchTerm) {
        return transporteTerrestreRepository.findByNombreTransportistaContainingIgnoreCase(searchTerm)
                .stream()
                .map(groundTransportMapper::toGroundTransportDTO)
                .collect(Collectors.toList());
    }
}
