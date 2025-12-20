package cl.ipfsoftware.bakend.service.impl;

import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.UserDTO;
import cl.ipfsoftware.bakend.model.entities.Usuario;
import cl.ipfsoftware.bakend.model.mapper.UserMapper;
import cl.ipfsoftware.bakend.persistence.repositories.UsuarioRepository;
import cl.ipfsoftware.bakend.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UsuarioRepository usuarioRepository,
            UserMapper userMapper,
            Validator validator,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.userMapper = userMapper;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== CREATE =====================
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Usuario usuario;
        try {
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de usuario fallida: " + message);
            }

            if (usuarioRepository.existsByCorreo(userDTO.getEmail())) {
                throw new DuplicateKeyException("El correo ya está registrado: " + userDTO.getEmail());
            }

            usuario = userMapper.toUser(userDTO);

            // 🔐 ENCRIPTAR CONTRASEÑA
            usuario.setClave(passwordEncoder.encode(userDTO.getPassword()));

            usuario = usuarioRepository.save(usuario);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al crear usuario: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al crear usuario: " + e.getMessage());
        }

        UserDTO resultDTO = userMapper.toUserDTO(usuario);
        System.out.println("Usuario creado: " + resultDTO);
        return resultDTO;
    }

    // ===================== READ =====================
    @Override
    public List<UserDTO> getAllUsers() {
        return usuarioRepository.findAll()
                .stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Integer id) {
        return Optional.ofNullable(id)
                .map(usuarioRepository::findByIdUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))
                .map(userMapper::toUserDTO);
    }

    // ===================== UPDATE =====================
    @Override
    public Optional<UserDTO> updateUser(Integer id, UserDTO userDTO) {
        try {
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de usuario fallida: " + message);
            }

            Usuario usuarioToUpdate = usuarioRepository.findByIdUsuario(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Usuario no encontrado con id: " + id));

            if (!usuarioToUpdate.getCorreo().equals(userDTO.getEmail())
                    && usuarioRepository.existsByCorreo(userDTO.getEmail())) {
                throw new DuplicateKeyException("El correo ya existe: " + userDTO.getEmail());
            }

            usuarioToUpdate.setNombre(userDTO.getFirstName());
            usuarioToUpdate.setPaterno(userDTO.getPaternalLastName());
            usuarioToUpdate.setMaterno(userDTO.getMaternalLastName());
            usuarioToUpdate.setCorreo(userDTO.getEmail());
            usuarioToUpdate.setRolUsuario(userDTO.getUserRole());

            // 🔐 ENCRIPTAR SOLO SI VIENE CLAVE NUEVA
            if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
                usuarioToUpdate.setClave(
                        passwordEncoder.encode(userDTO.getPassword())
                );
            }

            usuarioRepository.save(usuarioToUpdate);

            UserDTO resultDTO = userMapper.toUserDTO(usuarioToUpdate);
            System.out.println("Usuario actualizado con éxito");
            return Optional.of(resultDTO);

        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar usuario: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del usuario: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar usuario: " + e.getMessage());
        }
    }

    // ===================== DELETE =====================
    @Override
    public void deleteUserById(Integer id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByIdUsuario(id);
        if (usuarioOptional.isPresent()) {
            usuarioRepository.deleteByIdUsuario(id);
        } else {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
    }

    // ===================== EXISTS =====================
    @Override
    public Boolean existsUserById(Integer id) {
        return usuarioRepository.existsByIdUsuario(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByCorreo(email);
    }

    // ===================== SEARCH =====================
    @Override
    public List<UserDTO> searchUsers(String keyword) {
        return usuarioRepository
                .findByNombreContainingIgnoreCaseOrPaternoContainingIgnoreCaseOrMaternoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                        keyword, keyword, keyword, keyword)
                .stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }
}
