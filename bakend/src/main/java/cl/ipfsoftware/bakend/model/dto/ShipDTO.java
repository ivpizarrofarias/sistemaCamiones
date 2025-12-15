package cl.ipfsoftware.bakend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data // Anotación de Lombok que genera automáticamente métodos como getters, setters, toString, hashCode y equals.
public class ShipDTO implements Serializable {

    // Campo privado que representa el identificador único de la nave.
    private Integer shipId;

    // Valida que el campo 'shipName' no sea nulo, vacío o contenga solo espacios en blanco.
    @NotBlank(message = "El nombre del barco es obligatorio")
    // Valida que el campo 'shipName' cumpla con una expresión regular que permite solo letras, espacios y ciertos caracteres especiales.
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~-]+$", message = "El nombre del barco no es válido")
    // Valida que el campo 'shipName' no tenga más de 150 caracteres.
    @Size(max = 150, message = "El nombre del barco no puede exceder los 150 caracteres")
    // Campo privado que representa el nombre del barco, con validaciones de no nulo, tamaño y patrón.
    private String shipName;

    // Valida que el campo 'voyageNumber' no sea nulo, vacío o contenga solo espacios en blanco.
    @NotBlank(message = "El número de viaje es obligatorio")
    // Valida que el campo 'voyageNumber' cumpla con una expresión regular que permite solo letras, espacios y ciertos caracteres especiales.
    @Pattern(regexp = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~-]+$", message = "El número de viaje no es válido")
    // Valida que el campo 'voyageNumber' no tenga más de 50 caracteres.
    @Size(max = 50, message = "El número de viaje no puede exceder los 50 caracteres")
    // Campo privado que representa el número de viaje, con validaciones de no nulo, tamaño y patrón.
    private String voyageNumber;

    // Valida que el campo 'shippingLine' no sea nulo, vacío o contenga solo espacios en blanco.
    @NotBlank(message = "La línea naviera es obligatoria")
    // Valida que el campo 'shippingLine' cumpla con una expresión regular que permite solo letras, espacios y ciertos caracteres especiales.
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s!@#$%&*()_+=|<>?{}\\[\\]~.,()-]+$", message = "La línea naviera no es válida")
    // Valida que el campo 'shippingLine' no tenga más de 100 caracteres.
    @Size(max = 100, message = "La línea naviera no puede exceder los 100 caracteres")
    // Campo privado que representa la línea naviera, con validaciones de no nulo, tamaño y patrón.
    private String shippingLine;
}