package es.joseluisgs.springdam.errors.productos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Nos permite devolver un estado cuando salta la excepción
public class ProductoNotFoundException extends RuntimeException {

    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public ProductoNotFoundException(Long id) {
        super("No se puede encontrar el producto con la ID: " + id);
    }
}