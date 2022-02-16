package es.joseluisgs.springdam.repositories;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

// Test de integración del repositorio
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// Levanto la BBDD en cada test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ProductosRepositoryTests {

    private final Producto producto = Producto.builder()
            .nombre("Producto de prueba")
            .id(-1L)
            .precio(10.0)
            .stock(10)
            .build();

    // Repositorio a testear
    @Autowired
    private ProductosRepository productosRepository;

    @Test
    @Order(1)
    public void save() {
        Producto res = productosRepository.save(producto);

        assertNotNull(res);
        assertEquals(producto.getNombre(), res.getNombre());
        assertEquals(producto.getPrecio(), res.getPrecio());
        assertEquals(producto.getStock(), res.getStock());
    }

    @Test
    @Order(2)
    public void getAllProductos() {
        // Porque yo he metido los datos en la BBDD
        assertTrue(productosRepository.findAll().size() > 0);
    }

    @Test
    @Order(3)
    public void getProductoById() {
        // Ya sé que salva el producto, por el test 1
        Producto res = productosRepository.save(producto);

        res = productosRepository.findById(res.getId()).get();

        assertNotNull(res);
        assertEquals(producto.getNombre(), res.getNombre());
        assertEquals(producto.getPrecio(), res.getPrecio());
        assertEquals(producto.getStock(), res.getStock());
    }

    @Test
    @Order(4)
    public void updateProducto() {
        Producto res = productosRepository.save(producto);
        res = productosRepository.findById(res.getId()).get();
        res.setNombre("Producto de prueba modificado");

        res = productosRepository.save(res);
        assertNotNull(res);
        assertEquals("Producto de prueba modificado", res.getNombre());
        assertEquals(producto.getPrecio(), res.getPrecio());
        assertEquals(producto.getStock(), res.getStock());
    }

    @Test
    @Order(5)
    public void deleteProducto() {
        Producto res = productosRepository.save(producto);
        res = productosRepository.findById(res.getId()).get();

        productosRepository.delete(res);
        assertNull(productosRepository.findById(res.getId()).orElse(null));

    }
}
