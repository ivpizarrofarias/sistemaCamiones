package cl.ipfsoftware.bakend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*Registro en las Excepciones: BaseLoggedException maneja el registro de excepciones cuando se crean,
lo cual evita la duplicación de registros.

Manejador Global Limpio: GlobalExceptionHandler se mantiene simple al centrarse
únicamente en construir respuestas apropiadas para cada tipo de excepción.

Consistencia: Las excepciones específicas heredan de BaseLoggedException,
asegurando que todas las excepciones significativas sean registradas automáticamente.*/

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> buildResponse(HttpStatus status, Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(status.value(), new Date(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizedException(UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetails> handleForbiddenException(ForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDetails> handleConflictException(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorDetails> handleInternalServerException(InternalServerException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorDetails> handleServiceUnavailableException(ServiceUnavailableException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    /*En este ejemplo, handleValidationExceptions maneja la excepción MethodArgumentNotValidException
    para agregar los errores de validación al modelo (Model),
    que luego se pueden mostrar en la vista error.html.*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        model.addAttribute("validaErrors", errors);
        return "error.html"; // Nombre de la vista que muestra errores de validación
    }

    /*Este método actúa como una red de seguridad para cualquier
    excepción que no haya sido capturada por los manejadores específicos anteriores*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }


}
