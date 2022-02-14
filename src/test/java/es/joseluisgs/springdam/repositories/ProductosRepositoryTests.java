package es.joseluisgs.springdam.repositories;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosRepositoryTests {

    private final Producto producto = Producto.builder()
            .nombre("Producto de prueba")
            .precio(10.0)
            .stock(10)
            .build();
    // Repositorio a testear
    @Autowired
    private ProductosRepository productosRepository;

    @Test
    @Order(1)
    public void save() {
        productosRepository.save(producto);
        Producto res = productosRepository.findByNombre(producto.getNombre()).get(0);
        assertFalse(res.getNombre().isEmpty());
        assertNotNull(res);
    }

    @Test
    @Order(2)
    public void getAllProductos() {
        assertTrue(productosRepository.findAll().size() > 0);
    }

    @Test
    @Order(3)
    public void getProductoById() {
        Producto find = productosRepository.findByNombre(producto.getNombre()).get(0);
        Producto res = productosRepository.findById(find.getId()).get();
        assertNotNull(res);
        assertEquals(find.getId(), res.getId());
    }

    @Test
    @Order(4)
    public void updateProducto() {
        producto.setNombre("Producto de prueba update");
        productosRepository.save(producto);
        assertEquals(producto.getNombre(), productosRepository.findById(producto.getId()).get().getNombre());
    }

    @Test
    @Order(5)
    public void deleteProducto() {
        Producto find = productosRepository.findByNombre(producto.getNombre()).get(0);
        productosRepository.delete(find);
        assertNull(productosRepository.findById(find.getId()).orElse(null));

    }
}
