package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
@Data
public class GroundTransportDTO implements Serializable {
    private Integer transportId;
   @NotBlank(message = "El nombre del transportista es obligatorio")
    @Size(max = 255, message = "El nombre del transportista no puede exceder los 255 caracteres")
    private String transporterName;
}
