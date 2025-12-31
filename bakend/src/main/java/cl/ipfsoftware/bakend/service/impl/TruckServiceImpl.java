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

    public TruckServiceImpl(
            CamionRepository camionRepository,
            TruckMapper truckMapper,
            Validator validator
    ) {
        this.camionRepository = camionRepository;
        this.truckMapper = truckMapper;
        this.validator = validator;
    }

    /* ======================================================
       NORMALIZACIÓN DE PATENTE
       ====================================================== */
    private String normalizeLicensePlate(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        // Quita todo excepto letras y números
        String cleaned = value
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");

        // CVTV33 → CV*TV*33
        if (cleaned.matches("^[A-Z]{4}[0-9]{2}$")) {
            return cleaned.substring(0, 2)
                    + "*"
                    + cleaned.substring(2, 4)
                    + "*"
                    + cleaned.substring(4, 6);
        }

        return value.toUpperCase().trim();
    }

    /* ======================================================
       CREATE
       ====================================================== */
    @Override
    public TruckDTO createTruck(TruckDTO truckDTO) {
        try {
            // 🔥 NORMALIZAR ANTES DE VALIDAR
            truckDTO.setLicensePlate(
                    normalizeLicensePlate(truckDTO.getLicensePlate())
            );

            // 🔒 VALIDACIÓN
            Set<ConstraintViolation<TruckDTO>> violations = validator.validate(truckDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException(message);
            }

            // 🚫 DUPLICADO
            if (camionRepository.existsByPatente(truckDTO.getLicensePlate())) {
                throw new DuplicateKeyException(
                        "El camión con esa patente ya existe: " + truckDTO.getLicensePlate()
                );
            }

            Camion camion = truckMapper.toCamion(truckDTO);
            camion = camionRepository.save(camion);

            System.out.println("Camión guardado con patente: " + camion.getPatente());

            return truckMapper.toTruckDTO(camion);

        } catch (BusinessValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(
                    "Error inesperado al guardar el camión: " + e.getMessage()
            );
        }
    }

    /* ======================================================
       READ
       ====================================================== */
    @Override
    public List<TruckDTO> getAllTrucks() {
        return camionRepository.findAll()
                .stream()
                .map(truckMapper::toTruckDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TruckDTO> getTruckById(Integer id) {
        Camion camion = camionRepository.findByIdCamion(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Camión no encontrado con ID: " + id)
                );
        return Optional.of(truckMapper.toTruckDTO(camion));
    }

    /* ======================================================
       UPDATE
       ====================================================== */
    @Override
    public Optional<TruckDTO> updateTruck(Integer id, TruckDTO truckDTO) {
        try {
            truckDTO.setLicensePlate(
                    normalizeLicensePlate(truckDTO.getLicensePlate())
            );

            Set<ConstraintViolation<TruckDTO>> violations = validator.validate(truckDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException(message);
            }

            Camion camion = camionRepository.findByIdCamion(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Camión no encontrado con ID: " + id)
                    );

            if (!camion.getPatente().equals(truckDTO.getLicensePlate())
                    && camionRepository.existsByPatente(truckDTO.getLicensePlate())) {
                throw new DuplicateKeyException(
                        "La patente del camión ya existe: " + truckDTO.getLicensePlate()
                );
            }

            camion.setPatente(truckDTO.getLicensePlate());
            camionRepository.save(camion);

            return Optional.of(truckMapper.toTruckDTO(camion));

        } catch (BusinessValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(
                    "Error inesperado al actualizar el camión: " + e.getMessage()
            );
        }
    }

    /* ======================================================
       DELETE
       ====================================================== */
    @Override
    public void deleteTruckById(Integer id) {
        if (!camionRepository.existsByIdCamion(id)) {
            throw new EntityNotFoundException("Camión no encontrado con ID: " + id);
        }
        camionRepository.deleteById(id);
    }

    /* ======================================================
       EXISTS
       ====================================================== */
    @Override
    public Boolean existsTruckById(Integer id) {
        return camionRepository.existsByIdCamion(id);
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        return camionRepository.existsByPatente(
                normalizeLicensePlate(licensePlate)
        );
    }
}
