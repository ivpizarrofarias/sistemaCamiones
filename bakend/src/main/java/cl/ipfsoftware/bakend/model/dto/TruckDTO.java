package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class TruckDTO implements Serializable {
    private Integer truckId;

    @NotBlank(message = "La patente del camión es obligatoria")
    @Pattern(regexp = "^[A-Z]{2}\\*[A-Z]{2}\\*[0-9]{2}$", message = "La patente del camión no es válida")
    @Size(max = 10, message = "La patente del camión no puede exceder los 10 caracteres")
    private String licensePlate;
}
