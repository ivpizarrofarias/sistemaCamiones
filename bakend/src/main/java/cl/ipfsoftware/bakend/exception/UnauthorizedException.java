package cl.ipfsoftware.bakend.exception;

//Se lanza cuando el cliente no está autorizado para realizar una acción.
public class UnauthorizedException extends BaseLoggedException {
    public UnauthorizedException(String message) {
        super(message);
    }

    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
