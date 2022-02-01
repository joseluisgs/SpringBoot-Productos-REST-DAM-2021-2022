package es.joseluisgs.springdam.errors.productos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Nos permite devolver un estado cuando salta la excepción
public class ProductoBadRequestException extends RuntimeException {
    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public ProductoBadRequestException(String campo, String error) {
        super("Existe un error en el campo: " + campo + " Error: " + error);
    }
}