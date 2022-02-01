package es.joseluisgs.springdam.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Nos permite devolver un estado cuando salta la excepción
public class GeneralBadRequestException extends RuntimeException {
    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public GeneralBadRequestException(String operacion, String error) {
        super("Error al procesar: " + operacion + " : " + error);
    }
}