package cl.ipfsoftware.bakend.exception;

//Se lanza para manejar el caso donde intentas crear un recurso que ya existe
public class ResourceAlreadyExistsException extends BaseLoggedException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    /*Constructor permite especificar una causa raíz que llevó a la excepción.*/
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
