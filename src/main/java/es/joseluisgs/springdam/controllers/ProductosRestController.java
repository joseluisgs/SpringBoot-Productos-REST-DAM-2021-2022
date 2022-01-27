package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@RestController
// Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/
@RequestMapping("/rest")
public class ProductosRestController {
    @Autowired
    ProductosRepository productosRepository;

    // test
    @GetMapping("/test")
    public String test() {
        return "Hola REST 2DAM. Todo OK";
    }

    // Obtener todos los productos
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> findAll() {
        List<Producto> productos = productosRepository.findAll();
        if (productos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay productos registrados");
        } else {
            return ResponseEntity.ok(productos);
        }
    }

    // Obtener un producto por id
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> findById(@PathVariable Long id) {
        Producto producto = productosRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con id: " + id);
        } else {
            return ResponseEntity.ok(producto);
        }
    }

    // Insertar producto
    @PostMapping("/productos")
    public ResponseEntity<Producto> save(@RequestBody Producto producto) {
        try {
            producto.setCreatedAt(LocalDateTime.now());
            Producto productoInsertado = productosRepository.save(producto);
            return ResponseEntity.ok(productoInsertado);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e.getMessage());
        }
    }

    // Actualiza producto por id
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productosRepository.findById(id).orElse(null);
            if (productoActualizado == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con id: " + id);
            } else {
                productoActualizado.setNombre(producto.getNombre());
                productoActualizado.setPrecio(producto.getPrecio());
                productoActualizado.setStock(producto.getStock());

                productoActualizado = productosRepository.save(productoActualizado);
                return ResponseEntity.ok(productoActualizado);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e.getMessage());
        }
    }

    // Borrar producto por id
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Producto> delete(@PathVariable Long id) {
        try {
            Producto producto = productosRepository.findById(id).orElse(null);
            if (producto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con id: " + id);
            } else {
                productosRepository.delete(producto);
                return ResponseEntity.ok(producto);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e.getMessage());
        }
    }

}
