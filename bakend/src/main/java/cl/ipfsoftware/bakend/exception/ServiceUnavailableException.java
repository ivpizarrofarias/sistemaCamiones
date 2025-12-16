package cl.ipfsoftware.bakend.exception;

//Se lanza cuando un servicio necesario no está disponible.
public class ServiceUnavailableException extends BaseLoggedException {
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
