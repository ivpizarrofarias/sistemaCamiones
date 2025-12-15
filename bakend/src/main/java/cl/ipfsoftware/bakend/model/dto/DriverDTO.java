package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class DriverDTO implements Serializable {

    private Integer driverId;

    @NotBlank(message = "El nombre del chofer es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~-]+$", message = "El nombre del chofer no es válido")
    @Size(max = 255, message = "El nombre del chofer no puede exceder los 255 caracteres")
    private String driverName;

    @NotBlank(message = "El RUT del chofer es obligatorio")
    @Pattern(regexp = "^\\d{1,3}(?:\\.\\d{3}){2}-[0-9kK]{1}$", message = "El RUT del chofer no es válido")
    @Size(max = 12, message = "El RUT del chofer no puede exceder los 12 caracteres")
    private String driverRut;

    @NotBlank(message = "El número de licencia del chofer es obligatorio")
    @Pattern(regexp = "^\\d{1,3}(?:\\.\\d{3}){2}-[0-9kK]{1}$", message = "El número de licencia del chofer no es válido")
    @Size(max = 50, message = "El número de licencia del chofer no puede exceder los 50 caracteres")
    private String driverLicenseNumber;
}