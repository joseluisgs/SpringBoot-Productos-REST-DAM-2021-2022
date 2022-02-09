package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ListProductoPageDTO;
import es.joseluisgs.springdam.errors.GeneralBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoNotFoundException;
import es.joseluisgs.springdam.errors.productos.ProductosNotFoundException;
import es.joseluisgs.springdam.mappers.ProductoMapper;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.ProductosRepository;
import es.joseluisgs.springdam.services.uploads.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
// Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/
@RequestMapping("/rest")
public class ProductosRestController {
    private final ProductosRepository productosRepository;
    private final StorageService storageService;
    private final ProductoMapper productoMapper;

    // Inyección de dependencias por constructor
    // Es el método recomendado con el setter y no usando en el campo
    // https://blog.marcnuri.com/inyeccion-de-campos-desaconsejada-field-injection-not-recommended-spring-ioc
    @Autowired
    public ProductosRestController(ProductosRepository productosRepository, StorageService storageService, ProductoMapper productoMapper) {
        this.productosRepository = productosRepository;
        this.storageService = storageService;
        this.productoMapper = productoMapper;
    }

    @CrossOrigin(origins = "http://localhost:6969") //
    // Indicamos sobre que puerto u orignes dejamos que actue (navegador) En nuestro caso no habría problemas
    // Pero es bueno tenerlo en cuenta si tenemos en otro serviror una app en angular, vue android o similar
    // Pero es inviable para API consumida por muchos terceros. // Debes probar con un cliente desde ese puerto
    // Mejor hacer un filtro, ver MyConfig.java

    // test
    @GetMapping("/test")
    public String test() {
        return "Hola REST 2DAM. Todo OK";
    }

    // Obtener todos los productos
    @GetMapping("/productos")
    public ResponseEntity<?> findAll(@RequestParam(name = "limit") Optional<String> limit,
                                     @RequestParam(name = "nombre") Optional<String> nombre) {
        List<Producto> productos = null;
        try {
            if (nombre.isPresent()) {
                productos = productosRepository.findByNombreContainsIgnoreCase(nombre.get());
            } else {
                productos = productosRepository.findAll();
            }

            if (limit.isPresent() && !productos.isEmpty() && productos.size() > Integer.parseInt(limit.get())) {

                return ResponseEntity.ok(productoMapper.toDTO(
                        productos.subList(0, Integer.parseInt(limit.get())))
                );

            } else {
                if (!productos.isEmpty()) {
                    return ResponseEntity.ok(productoMapper.toDTO(productos));
                } else {
                    throw new ProductosNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException("Selección de Datos", "Parámetros de consulta incorrectos");
        }
    }


    // Obtener un producto por id
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Producto producto = productosRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ProductoNotFoundException(id);
        } else {
            return ResponseEntity.ok(productoMapper.toDTO(producto));
        }
    }

    // Insertar producto
    @PostMapping("/productos")
    public ResponseEntity<?> save(@RequestBody CreateProductoDTO productoDTO) {
        try {
            // Comprobamos los campos obligatorios
            Producto producto = productoMapper.fromDTO(productoDTO);
            checkProductoData(producto);
            Producto productoInsertado = productosRepository.save(producto);
            return ResponseEntity.ok(productoMapper.toDTO(productoInsertado));
        } catch (Exception e) {
            throw new GeneralBadRequestException("Insertar", "Error al insertar el producto. Campos incorrectos");
        }
    }


    // Actualiza producto por id. Podría ser un DTO si quisieramos
    @PutMapping("/productos/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productosRepository.findById(id).orElse(null);
            if (productoActualizado == null) {
                throw new ProductoNotFoundException(id);
            } else {
                checkProductoData(producto);
                // Actualizamos los datos que queramos
                productoActualizado.setNombre(producto.getNombre());
                productoActualizado.setPrecio(producto.getPrecio());
                productoActualizado.setStock(producto.getStock());

                productoActualizado = productosRepository.save(productoActualizado);
                return ResponseEntity.ok(productoMapper.toDTO(productoActualizado));
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException("Actualizar", "Error al actualizar el producto. Campos incorrectos");
        }
    }

    // Borrar producto por id
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Producto producto = productosRepository.findById(id).orElse(null);
            if (producto == null) {
                throw new ProductoNotFoundException(id);
            } else {
                productosRepository.delete(producto);
                return ResponseEntity.ok(productoMapper.toDTO(producto));
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException("Eliminar", "Error al borrar el producto");
        }
    }

    // Comprobar los campos obligatorios
    private void checkProductoData(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new ProductoBadRequestException("Nombre", "El nombre es obligatorio");
        }
        if (producto.getPrecio() < 0) {
            throw new ProductoBadRequestException("Precio", "El precio debe ser mayor que 0");
        }
        if (producto.getStock() < 0) {
            throw new ProductoBadRequestException("Stock", "El stock debe ser mayor o igual que 0");
        }
    }

    @PostMapping(value = "/productos/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> nuevoProducto(
            @RequestPart("producto") CreateProductoDTO productoDTO,
            @RequestPart("file") MultipartFile file) {

        try {
            // Comprobamos los campos obligatorios
            Producto producto = productoMapper.fromDTO(productoDTO);
            checkProductoData(producto);

            if (!file.isEmpty()) {
                String imagen = storageService.store(file);
                String urlImagen = storageService.getUrl(imagen);
                producto.setImagen(urlImagen);
            }
            Producto productoInsertado = productosRepository.save(producto);
            return ResponseEntity.ok(productoMapper.toDTO(productoInsertado));
        } catch (ProductoNotFoundException ex) {
            throw new GeneralBadRequestException("Insertar", "Error al insertar el producto. Campos incorrectos");
        }

    }

    // Obtener todos los productos
    @GetMapping("/productos/all")
    public ResponseEntity<?> listado(
            // Podemos buscar por los campos que quieramos... nombre, precio... así construir consultas
            @RequestParam(required = false, name = "nombre") Optional<String> nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Consulto en base a las páginas
        Pageable paging = PageRequest.of(page, size);
        Page<Producto> pagedResult;
        try {
            if (nombre.isPresent()) {
                pagedResult = productosRepository.findByNombreContainsIgnoreCase(nombre.get(), paging);
            } else {
                pagedResult = productosRepository.findAll(paging);
            }
            // De la página saco la lista de productos
            List<Producto> productos = pagedResult.getContent();
            // Mapeo al DTO. Si quieres ver toda la info de las paginas pon pageResult.
            ListProductoPageDTO listProductoPageDTO = ListProductoPageDTO.builder()
                    .data(productoMapper.toDTO(productos))
                    .totalPages(pagedResult.getTotalPages())
                    .totalElements(pagedResult.getTotalElements())
                    .currentPage(pagedResult.getNumber())
                    .build();
            return ResponseEntity.ok(listProductoPageDTO);
        } catch (Exception e) {
            throw new GeneralBadRequestException("Selección de Datos", "Parámetros de consulta incorrectos");
        }
    }


}
