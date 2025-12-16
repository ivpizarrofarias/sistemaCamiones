package cl.ipfsoftware.bakend.exception;

//Se lanza cuando hay un conflicto en el estado del recurso, como intentos de creación de duplicados.
public class ConflictException extends BaseLoggedException {
    public ConflictException(String message) {
        super(message);
    }
    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
