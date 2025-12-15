package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class PortDTO implements Serializable {

    private Integer portId;

    @NotBlank(message = "El nombre del puerto es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~-]+$", message = "El nombre del puerto no es válido")
    @Size(max = 255, message = "El nombre del puerto no puede exceder los 255 caracteres")
    private String portName;
}