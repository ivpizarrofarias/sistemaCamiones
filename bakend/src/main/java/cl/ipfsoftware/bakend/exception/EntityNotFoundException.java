package cl.ipfsoftware.bakend.exception;

//Se otorga cuando no encuentras una entidad específica en tu aplicación.
public class EntityNotFoundException extends BaseLoggedException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
