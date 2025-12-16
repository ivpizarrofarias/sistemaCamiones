package cl.ipfsoftware.bakend.exception;

//Se lanza cuando una solicitud del cliente es inválida.
public class BadRequestException extends BaseLoggedException {
    public BadRequestException(String message) {
        super(message);
    }
    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
