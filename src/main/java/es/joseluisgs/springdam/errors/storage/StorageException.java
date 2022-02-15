package es.joseluisgs.springdam.errors.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Nos permite devolver un estado cuando salta la excepción
public class StorageException extends RuntimeException {

    private static final long serialVersionUID = -5502351264978098291L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

}