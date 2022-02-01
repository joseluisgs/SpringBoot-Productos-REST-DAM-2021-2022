package es.joseluisgs.springdam.errors.productos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Nos permite devolver un estado cuando salta la excepción
public class ProductosNotFoundException extends RuntimeException {

    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public ProductosNotFoundException() {
        super("La lista de productos está vacía o no existe");
    }
}