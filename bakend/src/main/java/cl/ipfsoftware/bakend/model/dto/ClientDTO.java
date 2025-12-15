package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
@Data
public class ClientDTO  implements Serializable {
    private Integer clientId;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~-]+$", message = "El nombre del cliente no es válido")
    @Size(max = 255, message = "El nombre del cliente no puede exceder los 255 caracteres")
    private String clientName;

    @Email(message = "El email del cliente no es válido")
    @Size(max = 255, message = "El email del cliente no puede exceder los 255 caracteres")
    private String clientEmail;

    @Pattern(regexp = "^[0-9]+$", message = "El teléfono del cliente no es válido")
    @Size(max = 20, message = "El teléfono del cliente no puede exceder los 20 caracteres")
    private String clientPhone;
}
