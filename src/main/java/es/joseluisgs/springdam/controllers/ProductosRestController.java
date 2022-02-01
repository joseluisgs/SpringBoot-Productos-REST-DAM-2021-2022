package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.errors.GeneralBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoNotFoundException;
import es.joseluisgs.springdam.errors.productos.ProductosNotFoundException;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
// Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/
@RequestMapping("/rest")
public class ProductosRestController {
    @Autowired
    ProductosRepository productosRepository;

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
    public ResponseEntity<List<Producto>> findAll(@RequestParam(name = "limit") Optional<String> limit,
                                                  @RequestParam(name = "nombre") Optional<String> nombre) {
        List<Producto> productos = null;
        try {
            if (nombre.isPresent()) {
                productos = productosRepository.findByNombreContainsIgnoreCase(nombre.get());
            } else {
                productos = productosRepository.findAll();
            }

            if (limit.isPresent() && !productos.isEmpty() && productos.size() > Integer.parseInt(limit.get())) {

                return ResponseEntity.ok(productos.subList(0, Integer.parseInt(limit.get())));

            } else {
                if (!productos.isEmpty()) {
                    return ResponseEntity.ok(productos);
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
    public ResponseEntity<Producto> findById(@PathVariable Long id) {
        Producto producto = productosRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ProductoNotFoundException(id);
        } else {
            return ResponseEntity.ok(producto);
        }
    }

    // Insertar producto
    @PostMapping("/productos")
    public ResponseEntity<Producto> save(@RequestBody Producto producto) {
        try {
            producto.setCreatedAt(LocalDateTime.now());
            producto.setId(null); // Por si me llega un id
            // Comprobamos los campos obligatorios
            if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
                throw new ProductoBadRequestException("Nombre", "El nombre es obligatorio");
            }
            if (producto.getPrecio() < 0) {
                throw new ProductoBadRequestException("Precio", "El precio debe ser mayor que 0");
            }
            if (producto.getStock() < 0) {
                throw new ProductoBadRequestException("Stock", "El stock debe ser mayor o igual que 0");
            }
            Producto productoInsertado = productosRepository.save(producto);
            return ResponseEntity.ok(productoInsertado);
        } catch (Exception e) {
            throw new GeneralBadRequestException("Insertar", "Error al insertar el producto. Campos incorrectos");
        }
    }

    // Actualiza producto por id
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productosRepository.findById(id).orElse(null);
            if (productoActualizado == null) {
                throw new ProductoNotFoundException(id);
            } else {
                if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
                    throw new ProductoBadRequestException("Nombre", "El nombre es obligatorio");
                }
                if (producto.getPrecio() < 0) {
                    throw new ProductoBadRequestException("Precio", "El precio debe ser mayor que 0");
                }
                if (producto.getStock() < 0) {
                    throw new ProductoBadRequestException("Stock", "El stock debe ser mayor o igual que 0");
                }
                productoActualizado.setNombre(producto.getNombre());
                productoActualizado.setPrecio(producto.getPrecio());
                productoActualizado.setStock(producto.getStock());

                productoActualizado = productosRepository.save(productoActualizado);
                return ResponseEntity.ok(productoActualizado);
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException("Actualizar", "Error al actualizar el producto. Campos incorrectos");
        }
    }

    // Borrar producto por id
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Producto> delete(@PathVariable Long id) {
        try {
            Producto producto = productosRepository.findById(id).orElse(null);
            if (producto == null) {
                throw new ProductoNotFoundException(id);
            } else {
                productosRepository.delete(producto);
                return ResponseEntity.ok(producto);
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException("Eliminar", "Error al borrar el producto");
        }
    }

}
