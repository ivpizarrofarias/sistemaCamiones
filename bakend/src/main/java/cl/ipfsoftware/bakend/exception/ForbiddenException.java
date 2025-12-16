package cl.ipfsoftware.bakend.exception;

public class ForbiddenException extends BaseLoggedException {
    public ForbiddenException(String message) {
        super(message);
    }
    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
