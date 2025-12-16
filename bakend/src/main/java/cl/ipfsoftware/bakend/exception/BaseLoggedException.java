package cl.ipfsoftware.bakend.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Excepción base para manejar errores con registro de logs automáticos.
 * Esta clase encapsula la lógica para registrar excepciones tanto con mensaje como con causa.
 * Utiliza el nivel de log SEVERE para los mensajes y causa de excepción.
 */
public abstract class BaseLoggedException extends RuntimeException {
    // Logger estático para registrar las excepciones
    private static final Logger LOGGER = Logger.getLogger(BaseLoggedException.class.getName());

    /**
     * Constructor para excepciones sin causa.
     * @param message Mensaje de la excepción.
     */
    public BaseLoggedException(String message) {
        super(message);
        logException(message, null);
    }

    /**
     * Constructor para excepciones con causa.
     * @param message Mensaje de la excepción.
     * @param cause Causa de la excepción.
     */
    public BaseLoggedException(String message, Throwable cause) {
        super(message, cause);
        logException(message, cause);
    }

    /**
     * Método privado para registrar la excepción.
     * @param message Mensaje de la excepción.
     * @param cause Causa de la excepción (puede ser null).
     */
    private void logException(String message, Throwable cause) {
        if (cause == null) {
            // Si no hay causa, registrar solo el mensaje
            LOGGER.log(Level.SEVERE, message);
        } else {
            // Si hay causa, registrar el mensaje y la causa
            LOGGER.log(Level.SEVERE, message, cause);
        }
    }
}
