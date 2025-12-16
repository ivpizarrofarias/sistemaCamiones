package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.ShipDTO;
import cl.ipfsoftware.bakend.model.entities.Nave;
import cl.ipfsoftware.bakend.model.mapper.ShipMapper;
import cl.ipfsoftware.bakend.persistence.repositories.NaveRepository;
import cl.ipfsoftware.bakend.service.ShipService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private NaveRepository naveRepository;
    private final ShipMapper shipMapper;
    private final Validator validator;

    @Autowired
    public ShipServiceImpl(NaveRepository naveRepository, ShipMapper shipMapper, Validator validator) {
        this.naveRepository = naveRepository;
        this.shipMapper = shipMapper;
        this.validator = validator;
    }

    @Override
    public ShipDTO createShip(ShipDTO shipDTO) {
        Nave ship;
        try {
            Set<ConstraintViolation<ShipDTO>> violations = validator.validate(shipDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));
                throw new BusinessValidationException("Validación de nave fallida: " + message);
            }

            if (naveRepository.existsByIdNave(shipDTO.getShipId())) {
                throw new DuplicateKeyException("La nave ya existe: " + shipDTO.getShipId());
            }

            if (naveRepository.existsByNombreBarco(shipDTO.getShipName())) {
                throw new DuplicateKeyException("La nave con ese nombre ya existe: " + shipDTO.getShipName());
            }

            ship = shipMapper.toShip(shipDTO);
            ship = naveRepository.save(ship);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar la nave: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación de la nave: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar la nave: " + e.getMessage());
        }

        ShipDTO resultDTO = shipMapper.toShipDTO(ship);
        System.out.println("Nave DTO creada: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<ShipDTO> getAllShips() {
        return naveRepository.findAll()
                .stream()
                .map(shipMapper::toShipDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShipDTO> updateShip(Integer id, ShipDTO shipDTO) throws Exception {
        try {
            Set<ConstraintViolation<ShipDTO>> violations = validator.validate(shipDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de nave fallida: " + message);
            }

            Nave shipToUpdate = naveRepository.findByIdNave(id)
                    .orElseThrow(() -> new EntityNotFoundException("Nave no encontrada con id: " + id));

            if (!shipToUpdate.getNombreBarco().equals(shipDTO.getShipName())
                    && naveRepository.existsByNombreBarco(shipDTO.getShipName())) {
                throw new DuplicateKeyException("El nombre de la nave ya existe: " + shipDTO.getShipName());
            }

            shipToUpdate.setNombreBarco(shipDTO.getShipName());
            shipToUpdate.setNumeroViaje(shipDTO.getVoyageNumber());
            shipToUpdate.setNaviera(shipDTO.getShippingLine());

            naveRepository.save(shipToUpdate);

            ShipDTO resultDTO = shipMapper.toShipDTO(shipToUpdate);
            System.out.println("La nave ha sido modificada con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar la nave: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación de la nave: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar la nave: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar la nave: " + e.getMessage());
        }
    }

    @Override
    public Optional<ShipDTO> getShipById(Integer id) {
        return Optional.ofNullable(id)
                .map(naveRepository::findByIdNave)
                .orElseThrow(() -> new IllegalArgumentException("Nave no encontrada"))
                .map(shipMapper::toShipDTO);
    }

    @Override
    public void deleteShipById(Integer id) {
        Optional<Nave> shipOptional = naveRepository.findById(id);
        if (shipOptional.isPresent()) {
            naveRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Nave no encontrada con ID: " + id);
        }
    }

    @Override
    public Boolean existsShipById(Integer id) {
        return naveRepository.existsByIdNave(id);
    }

    @Override
    public boolean existsByShipName(String shipName) {
        return naveRepository.existsByNombreBarco(shipName);
    }

    @Override
    public List<ShipDTO> createShips(List<ShipDTO> shipDTOS) {
        List<Nave> ships = new ArrayList<>();
        try {
            for (ShipDTO shipDTO : shipDTOS) {
                Set<ConstraintViolation<ShipDTO>> violations = validator.validate(shipDTO);
                if (!violations.isEmpty()) {
                    String message = violations.stream()
                            .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                            .collect(Collectors.joining(","));
                    throw new BusinessValidationException("Validación de nave fallida: " + message);
                }

                if (naveRepository.existsByIdNave(shipDTO.getShipId())) {
                    throw new DuplicateKeyException("La nave ya existe: " + shipDTO.getShipId());
                }

                if (naveRepository.existsByNombreBarco(shipDTO.getShipName())) {
                    throw new DuplicateKeyException("La nave con ese nombre ya existe: " + shipDTO.getShipName());
                }

                Nave ship = shipMapper.toShip(shipDTO);
                ships.add(ship);
            }

            ships = (List<Nave>) naveRepository.saveAll(ships);
        } catch (Exception e) {
            throw new InternalServerException("Error al crear las naves: " + e.getMessage());
        }

        return ships.stream()
                .map(shipMapper::toShipDTO)
                .collect(Collectors.toList());
    }

    public List<ShipDTO> searchShipsByNameOrVoyageNumberOrShippingLine(String searchTerm) {
        return naveRepository.findByNombreBarcoContainingIgnoreCaseOrNumeroViajeContainingIgnoreCaseOrNavieraContainingIgnoreCase(searchTerm, searchTerm, searchTerm)
                .stream()
                .map(shipMapper::toShipDTO)
                .collect(Collectors.toList());
    }
}