package cl.ipfsoftware.bakend.model.dto;


import cl.ipfsoftware.bakend.model.entities.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDTO implements Serializable {

    private Integer userId;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$",
            message = "El nombre solo puede contener letras y espacios")
    @Size(max = 60, message = "El nombre no puede exceder los 60 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$",
            message = "El apellido paterno solo puede contener letras y espacios")
    @Size(max = 50, message = "El apellido paterno no puede exceder los 50 caracteres")
    private String paternalLastName;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$",
            message = "El apellido materno solo puede contener letras y espacios")
    @Size(max = 50, message = "El apellido materno no puede exceder los 50 caracteres")
    private String maternalLastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "El formato del correo electrónico no es válido")
    @Size(max = 90, message = "El correo electrónico no puede exceder los 90 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 150,
            message = "La contraseña debe tener entre 8 y 150 caracteres")
    private String password;

    @NotNull(message = "El rol del usuario es obligatorio")
    private RolUsuario userRole;

    private LocalDateTime creationDate;
}