package cl.ipfsoftware.bakend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*Se lanza cuando el tipo de exceptions es referente a una regla de negocio, por ejemplo;
Reglas de Validación de Datos y Procesos de Negocio:

-Edad mínima: En una aplicación de registro de usuarios, puede haber una regla de negocio que estipule que los usuarios
deben tener al menos 18 años.

-Formato de correo electrónico: Un sistema puede requerir que las direcciones de correo electrónico
sigan un formato específico y sean únicas en el sistema.

Descuentos: En una tienda en línea, puede haber una regla que defina que los descuentos solo se aplican a
compras superiores a cierta cantidad o durante un período específico.*/

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessValidationException extends BaseLoggedException {
    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}