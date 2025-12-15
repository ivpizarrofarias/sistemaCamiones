package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class TripDTO implements Serializable {

    private Integer tripId;

    @NotBlank(message = "El origen del viaje es obligatorio")
    @Size(max = 100, message = "El origen del viaje no puede exceder los 100 caracteres")
    private String origin;

    // Si necesitas agregar más campos relacionados con las valorizaciones o detalles adicionales,
    // puedes agregarlos aquí con sus correspondientes validaciones.
}
