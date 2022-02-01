package es.joseluisgs.springdam.services.uploads;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Este interfaz nos permite definir una abstracción de lo que debería
 * ser un almacén secundario de información, de forma que podamos usarlo
 * en un controlador.
 * <p>
 * De esta forma, vamos a poder utilizar un almacen que acceda a nuestro
 * sistema de ficheros, o también podríamos implementar otro que estuviera
 * en un sistema remoto, almacenar los ficheros en un sistema GridFS, ...
 *
 * @author Equipo de desarrollo de Spring
 */
public interface StorageService {

    // Inicia sl sistema de ficheros
    void init();

    // Almacena un fichero llegado como un contenido multiparte
    String store(MultipartFile file);

    // Devuleve un Stream con todos los ficheros
    Stream<Path> loadAll();

    // Devuleve el Path o ruta de un fichero
    Path load(String filename);

    // Devuelve el fichero como recurso
    Resource loadAsResource(String filename);

    // Borra un fichero
    void delete(String filename);

    // Borra todos los ficheros
    void deleteAll();

}
