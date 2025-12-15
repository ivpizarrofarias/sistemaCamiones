/*
package cl.ipfsoftware.sistemacamiones.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

import java.time.LocalDateTime;


@Data
public class ValuationDTO implements Serializable {
    private Integer valuationId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime date;

    @NotBlank(message = "El valor es obligatorio")
    private String value;

    @NotNull(message = "El cliente es obligatorio")
    private Integer clientId;

    @NotNull(message = "La nave es obligatoria")
    private Integer shipId;

    @NotNull(message = "El puerto es obligatorio")
    private Integer portId;

    @NotNull(message = "El viaje es obligatorio")
    private Integer tripId;

    @NotNull(message = "El movimiento es obligatorio")
    private Integer movementId;

    // Campos adicionales para mostrar información relacionada
    private String clientName; // Nombre del cliente
    private String shipName; // Nombre de la nave
    private String portName; // Nombre del puerto (usado como destino)
    private String tripName; // Nombre del viaje
    private String movementIdentificationNumber; // Número de identificación del movimiento
}


*/
package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ValuationDTO implements Serializable {

    // Identificador único de la valoración
    private Integer valuationId;

    // Fecha de la valoración
    @NotNull(message = "La fecha de la valoración es obligatoria")
    private LocalDateTime valuationDate;

    // Valor de la valoración
    @NotBlank(message = "El valor de la valoración es obligatorio")
    private String value;

    // Identificador del cliente relacionado
    @NotNull(message = "El cliente es obligatorio")
    private Integer clientId;

    // Identificador de la nave relacionada
    @NotNull(message = "La nave es obligatoria")
    private Integer shipId;

    // Identificador del puerto relacionado
    @NotNull(message = "El puerto es obligatorio")
    private Integer portId;

    // Identificador del viaje relacionado
    @NotNull(message = "El viaje es obligatorio")
    private Integer tripId;

    // Identificador del contenedor relacionado (opcional)
    @NotNull(message = "El codigo del contenedor es obligatorio")
    private Integer containerId;

    // Identificador del transporte terrestre relacionado (opcional)
    @NotNull(message = "El Transporte es obligatorio")
    private Integer terrestrialTransportId;

    // Información adicional para visualización
    private String clientName; // Nombre del cliente
    private String shipName; // Nombre de la nave
    private String portName; // Nombre del puerto
    private String tripCode; // Código del viaje
    private String containerCode; // Código del contenedor
    private String transportName; // Nombre del transporte terrestre
}
