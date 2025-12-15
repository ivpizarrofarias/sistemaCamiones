package cl.ipfsoftware.bakend.model.dto;

import cl.ipfsoftware.bakend.model.entities.EstadoFisico;
import cl.ipfsoftware.bakend.model.entities.TipoDocumento;
import cl.ipfsoftware.bakend.model.entities.TipoTamanio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MovementDTO implements Serializable {

    // Identificador único del movimiento
    private Integer movementId;

    // Fecha de emisión del movimiento
    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDateTime emissionDate;

    // Número de documento asociado al movimiento
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 50, message = "El número de documento no puede exceder los 50 caracteres")
    private String documentNumber;

    // Tipo de documento (enum)
    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumento documentType;

    // Estado físico del movimiento (enum)
    @NotNull(message = "El estado físico es obligatorio")
    private EstadoFisico physicalState;

    // Tipo de tamaño del movimiento (enum)
    @NotNull(message = "El tipo de tamaño es obligatorio")
    private TipoTamanio sizeType;

    // Fecha y hora de entrada
    @NotNull(message = "La fecha y hora de entrada son obligatorias")
    private LocalDateTime entryDateTime;

    // Fecha y hora de salida
    @NotNull(message = "La fecha y hora de salida son obligatorias")
    private LocalDateTime exitDateTime;

    // Identificador del camión relacionado
    @NotNull(message = "El camión es obligatorio")
    private Integer truckId;

    // Identificador del chofer relacionado
    @NotNull(message = "El chofer es obligatorio")
    private Integer driverId;

    // Identificador del cliente relacionado
    @NotNull(message = "El cliente es obligatorio")
    private Integer clientId;

    // Identificador de la nave relacionada
    @NotNull(message = "La nave es obligatoria")
    private Integer shipId;

    // Identificador del puerto relacionado
    @NotNull(message = "El puerto es obligatorio")
    private Integer portId;

    // Identificador del transporte terrestre relacionado
    @NotNull(message = "El transporte terrestre es obligatorio")
    private Integer terrestrialTransportId;

    // Identificador del contenedor relacionado
    @NotNull(message = "El contenedor es obligatorio")
    private Integer containerId;

    // Información adicional para facilitar la visualización
    private String shipName; // Nombre de la nave
    private String driverName; // Nombre del chofer
    private String truckLicensePlate; // Placa del camión
    private String transportName; // Nombre del transporte terrestre
    private String portName; // Nombre del puerto
    private String clientName; // Nombre del cliente
    private String containerCode;
}