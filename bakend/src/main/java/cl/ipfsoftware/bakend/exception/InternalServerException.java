package cl.ipfsoftware.bakend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Se lanza cuando ocurre un error inesperado en el servidor
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends BaseLoggedException {
    public InternalServerException(String message) {
        super(message);
    }
    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
