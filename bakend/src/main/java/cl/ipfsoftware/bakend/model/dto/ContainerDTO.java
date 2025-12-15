package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContainerDTO  implements Serializable {
    private Integer containerId;

    @NotBlank(message = "El código del contenedor es obligatorio")
    @Size(max = 50, message = "El código del contenedor no puede exceder los 50 caracteres")
    private String containerCode;
}
