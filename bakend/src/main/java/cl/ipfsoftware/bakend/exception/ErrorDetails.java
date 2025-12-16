package cl.ipfsoftware.bakend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
// Clase para detalles de error
public class ErrorDetails {
    private int statusCode;
    private Date timestamp;
    private String message;
}