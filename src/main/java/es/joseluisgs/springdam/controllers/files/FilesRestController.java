package es.joseluisgs.springdam.controllers.files;

import es.joseluisgs.springdam.config.APIConfig;
import es.joseluisgs.springdam.errors.storage.StorageException;
import es.joseluisgs.springdam.services.uploads.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(APIConfig.API_PATH + "/files")
public class FilesRestController {
    private StorageService storageService;

    // También podemos inyectar dependencias por el setter
    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    // Devuelve el fichero indicado por fichero y su contenido
    // Usamos el request para tener los datos de la petición
    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        Resource file = storageService.loadAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new StorageException("No se puede determinar el tipo del fichero", ex);
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Aunque no es obligatorio, podemos indicar que se consume multipart/form-data
    // Para ficheros usamos, Resuqest part, porque lo tenemos dividido en partes
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestPart("file") MultipartFile file) {

        // Almacenamos el fichero y obtenemos su URL
        String urlImagen = null;

        try {
            if (!file.isEmpty()) {
                String imagen = storageService.store(file);
                urlImagen = storageService.getUrl(imagen);
                Map<String, Object> response = Map.of("url", urlImagen);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                throw new StorageException("No se puede subir un fichero vacío");
            }
        } catch (StorageException e) {
            throw new StorageException("No se puede subir un fichero vacío");
        }
    }

    // Implementar el resto de metodos del servicio que nos interesen...
    // Delete file, listar ficheros, etc....
}
