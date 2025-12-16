package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.ContainerDTO;
import cl.ipfsoftware.bakend.model.entities.Contenedor;
import cl.ipfsoftware.bakend.model.mapper.ContainerMapper;
import cl.ipfsoftware.bakend.persistence.repositories.ContenedorRepository;
import cl.ipfsoftware.bakend.service.ContainerService;
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
public class ContainerServiceImpl implements ContainerService {

    private final ContenedorRepository contenedorRepository;
    private final ContainerMapper containerMapper;
    private final Validator validator;

    public ContainerServiceImpl(ContenedorRepository contenedorRepository, ContainerMapper containerMapper, Validator validator) {
        this.contenedorRepository = contenedorRepository;
        this.containerMapper = containerMapper;
        this.validator = validator;
    }

    @Override
    public ContainerDTO createContainer(ContainerDTO containerDTO) {
        Contenedor contenedor;
        try {
            // Validar el DTO
            Set<ConstraintViolation<ContainerDTO>> violations = validator.validate(containerDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación del contenedor fallida: " + message);
            }

            // Verificar si ya existe un contenedor con el mismo ID
            if (contenedorRepository.existsByIdContenedor(containerDTO.getContainerId())) {
                throw new DuplicateKeyException("El contenedor ya existe: " + containerDTO.getContainerId());
            }

            // Verificar si ya existe un contenedor con el mismo código
            if (contenedorRepository.existsByCodigoContenedor(containerDTO.getContainerCode())) {
                throw new DuplicateKeyException("El contenedor con ese código ya existe: " + containerDTO.getContainerCode());
            }

            // Mapear el DTO a la entidad y guardar
            contenedor = containerMapper.toContenedor(containerDTO);
            contenedor = contenedorRepository.save(contenedor);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el contenedor: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del contenedor: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el contenedor: " + e.getMessage());
        }

        // Mapear la entidad guardada a DTO y retornar
        ContainerDTO resultDTO = containerMapper.toContainerDTO(contenedor);
        System.out.println("Contenedor DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<ContainerDTO> getAllContainers() {
        return contenedorRepository.findAll()
                .stream()
                .map(containerMapper::toContainerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContainerDTO> updateContainer(Integer id, ContainerDTO containerDTO) throws Exception {
        try {
            // Validar el DTO
            Set<ConstraintViolation<ContainerDTO>> violations = validator.validate(containerDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación del contenedor fallida: " + message);
            }

            // Buscar el contenedor existente
            Contenedor contenedorToUpdate = contenedorRepository.findByIdContenedor(id)
                    .orElseThrow(() -> new EntityNotFoundException("Contenedor no encontrado con id: " + id));

            // Verificar si el código del contenedor ya existe (si ha cambiado)
            if (!contenedorToUpdate.getCodigoContenedor().equals(containerDTO.getContainerCode())
                    && contenedorRepository.existsByCodigoContenedor(containerDTO.getContainerCode())) {
                throw new DuplicateKeyException("El código del contenedor ya existe: " + containerDTO.getContainerCode());
            }

            // Actualizar los datos del contenedor
            contenedorToUpdate.setCodigoContenedor(containerDTO.getContainerCode());

            // Guardar los cambios
            contenedorRepository.save(contenedorToUpdate);

            // Mapear la entidad actualizada a DTO y retornar
            ContainerDTO resultDTO = containerMapper.toContainerDTO(contenedorToUpdate);
            System.out.println("El contenedor ha sido modificado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el contenedor: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del contenedor: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el contenedor: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el contenedor: " + e.getMessage());
        }
    }

    @Override
    public Optional<ContainerDTO> getContainerById(Integer id) {
        return Optional.ofNullable(id)
                .map(contenedorRepository::findByIdContenedor)
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado"))
                .map(containerMapper::toContainerDTO);
    }

    @Override
    public void deleteContainerById(Integer id) {
        Optional<Contenedor> contenedorOptional = contenedorRepository.findById(id);
        if (contenedorOptional.isPresent()) {
            contenedorRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Contenedor no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsContainerById(Integer id) {
        return contenedorRepository.existsByIdContenedor(id);
    }

    @Override
    public boolean existsByContainerCode(String containerCode) {
        return contenedorRepository.existsByCodigoContenedor(containerCode);
    }

    public List<ContainerDTO> searchContainersByCode(String searchTerm) {
        return contenedorRepository.findByCodigoContenedorContainingIgnoreCase(searchTerm)
                .stream()
                .map(containerMapper::toContainerDTO)
                .collect(Collectors.toList());
    }
}