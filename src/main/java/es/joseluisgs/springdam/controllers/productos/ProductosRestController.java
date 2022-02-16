package es.joseluisgs.springdam.controllers.productos;

import es.joseluisgs.springdam.config.APIConfig;
import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ListProductoPageDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.errors.GeneralBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoNotFoundException;
import es.joseluisgs.springdam.errors.productos.ProductosNotFoundException;
import es.joseluisgs.springdam.mappers.ProductoMapper;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import es.joseluisgs.springdam.services.uploads.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(APIConfig.API_PATH + "/productos")
// Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/
public class ProductosRestController {
    private final ProductosRepository productosRepository;
    private final StorageService storageService;
    private final ProductoMapper productoMapper;

    // Inyección de dependencias por constructor
    // Es el método recomendado con el setter y no usando en el campo
    // Podríamos usar Lombok para inyectar las dependencias
    // https://blog.marcnuri.com/inyeccion-de-campos-desaconsejada-field-injection-not-recommended-spring-ioc
    @Autowired
    public ProductosRestController(ProductosRepository productosRepository, StorageService storageService, ProductoMapper productoMapper) {
        this.productosRepository = productosRepository;
        this.storageService = storageService;
        this.productoMapper = productoMapper;
    }

    // @CrossOrigin(origins = "http://localhost:6969") //
    // Indicamos sobre que puerto u orignes dejamos que actue (navegador) En nuestro caso no habría problemas
    // Pero es bueno tenerlo en cuenta si tenemos en otro serviror una app en angular, vue android o similar
    // Pero es inviable para API consumida por muchos terceros. // Debes probar con un cliente desde ese puerto
    // Mejor hacer un filtro, ver MyConfig.java

    @ApiOperation(value = "test", notes = "Mensaje de bienvenida")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class)
    })
    @GetMapping("/test")
    public String test() {
        return "Hola REST 2DAM. Todo OK";
    }

    @ApiOperation(value = "Obtener todos los productos", notes = "Obtiene todos los productos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProductoDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found", response = ProductosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class)
    })
    @GetMapping("/")
    public ResponseEntity<List<ProductoDTO>> findAll(@RequestParam(name = "limit") Optional<String> limit,
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


    @ApiOperation(value = "Obtener un producto por id", notes = "Obtiene un producto por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProductoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ProductosNotFoundException.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Producto producto = productosRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ProductoNotFoundException(id);
        } else {
            return ResponseEntity.ok(productoMapper.toDTO(producto));
        }
    }

    @ApiOperation(value = "Crear un producto", notes = "Crea un producto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created", response = ProductoDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class)
    })
    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody CreateProductoDTO productoDTO) {
        try {
            Producto producto = productoMapper.fromDTO(productoDTO);
            checkProductoData(producto);
            Producto productoInsertado = productosRepository.save(producto);
            return ResponseEntity.ok(productoMapper.toDTO(productoInsertado));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GeneralBadRequestException("Insertar", "Error al insertar el producto. Campos incorrectos");
        }
    }


    @ApiOperation(value = "Actualizar un producto", notes = "Actualiza un producto por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProductoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ProductosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class)
    })
    @PutMapping("/{id}")
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

    @ApiOperation(value = "Eliminar un producto", notes = "Elimina un producto dado su id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProductoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ProductosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class)
    })
    @DeleteMapping("/{id}")
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

    /**
     * Método para comprobar que los datos del producto son correctos
     *
     * @param producto Producto a comprobar
     *                 - Nombre no puede estar vacío
     *                 - Precio no puede ser negativo
     *                 - Stock no puede ser negativo
     * @throws ProductoBadRequestException Si los datos no son correctos
     */
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

    @ApiOperation(value = "Crea un producto con imagen", notes = "Crea un producto con imagen")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProductoDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @Operation(summary = "Obtiene la lista de productos existentes", description = "Obtiene la lista de productos existentes")
    @ApiOperation(value = "Obtiene una lista de productos", notes = "Obtiene una lista de productos paginada, filtrada y ordenada")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK: Lista de productos", response = ListProductoPageDTO.class),
            @ApiResponse(code = 400, message = "Bad Request: Lista no encontrada", response = GeneralBadRequestException.class)
    })
    @GetMapping("/all")
    public ResponseEntity<?> listado(
            // Podemos buscar por los campos que quieramos... nombre, precio... así construir consultas
            @RequestParam(required = false, name = "nombre") Optional<String> nombre,
            @RequestParam(required = false, name = "precio") Optional<Double> precio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        // Consulto en base a las páginas
        Pageable paging = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Producto> pagedResult;
        try {
            if (nombre.isPresent() && precio.isPresent()) {
                pagedResult = productosRepository.findByNombreContainsIgnoreCaseAndPrecioGreaterThanEqualOrderByNombreAsc(nombre.get(), precio.get(), paging);
            } else if (nombre.isPresent()) {
                pagedResult = productosRepository.findByNombreContainsIgnoreCase(nombre.get(), paging);
            } else if (precio.isPresent()) {
                pagedResult = productosRepository.findByPrecioGreaterThanEqualOrderByNombreAsc(precio.get(), paging);
            } else {
                pagedResult = productosRepository.findAll(paging);
            }
            // De la página saco la lista de productos
            //List<Producto> productos = pagedResult.getContent();
            // Mapeo al DTO. Si quieres ver toda la info de las paginas pon pageResult.
            ListProductoPageDTO listProductoPageDTO = ListProductoPageDTO.builder()
                    .data(productoMapper.toDTO(pagedResult.getContent()))
                    .totalPages(pagedResult.getTotalPages())
                    .totalElements(pagedResult.getTotalElements())
                    .currentPage(pagedResult.getNumber())
                    .sort(pagedResult.getSort().toString())
                    .build();
            return ResponseEntity.ok(listProductoPageDTO);
        } catch (Exception e) {
            throw new GeneralBadRequestException("Selección de Datos", "Parámetros de consulta incorrectos");
        }
    }
}
